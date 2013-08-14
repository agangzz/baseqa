package edu.cmu.lti.oaqa.gerp.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.JCasIterator;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCopier;
import org.oaqa.model.gerp.GerpMeta;
import org.uimafit.component.JCasMultiplier_ImplBase;
import org.uimafit.descriptor.OperationalProperties;
import org.uimafit.factory.AnalysisEngineFactory;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.cmu.lti.oaqa.core.data.TopWrapper;
import edu.cmu.lti.oaqa.core.data.WrapperHelper;
import edu.cmu.lti.oaqa.core.data.WrapperIndexer;
import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.phase.BasePhase;
import edu.cmu.lti.oaqa.gerp.data.EvidenceWrapper;
import edu.cmu.lti.oaqa.gerp.data.GerpMetaWrapper;
import edu.cmu.lti.oaqa.gerp.data.Gerpable;
import edu.cmu.lti.oaqa.gerp.data.GerpableList;
import edu.cmu.lti.oaqa.gerp.data.PruningDecisionWrapper;
import edu.cmu.lti.oaqa.gerp.data.RankWrapper;

/**
 * Extended from {@link edu.cmu.lti.oaqa.ecd.phase.BasePhase}, which instead of using
 * <code>options</code> for integrating components, generators, evidencers, rankers and pruners
 * inherited from {@link AbstractGenerator}, {@link AbstractEvidencer}, {@link AbstractRanker}, or
 * {@link AbstractPruner} should be listed in separated fields in the descriptor. Moreover, the type
 * of the {@link TOP} or {@link Annotation} should be specified for the parameter <code>type</code>.
 * Other options, e.g. <code>name</code>, <code>option-timeout</code>,
 * <code>lazy-load-options</code> are preserved.
 * <p>
 * Each combination of TOPs generated by previous {@link GerpPhase}s (
 * {@link JCasMultiplier_ImplBase} implementations), claimed by
 * {@link AbstractGenerator#getRequiredInputTypes()} as input arguments, will be stored in different
 * {@link CAS}es and processed by the {@link #process(JCas)} separately, and further passed along
 * the pipeline within the analysis engine for evidencing, ranking and pruning. The number of
 * {@link CAS}es output from the {@link #next()} method of each GerpPhase instance is equal to the
 * number of <code>generator</code>s defined in the descriptor.
 * 
 * @author Zi Yang <ziy@cs.cmu.edu>
 * 
 */
@OperationalProperties(outputsNewCases = true)
public class GerpPhase<T extends TOP, W extends Gerpable & TopWrapper<T>> extends
        JCasMultiplier_ImplBase {

  /*
   * Field name constants copied from BasePhase
   */
  public static final String QA_INTERNAL_PHASEID = "__.qa.internal.phaseid.__";

  public static final String TIMEOUT_KEY = "option-timeout";

  public static final String LAZY_LOAD_KEY = "lazy-load-options";

  private Map<String, Object> confs;

  private String gerpableClassName;

  private int gerpableType;

  private GerpMetaWrapper gerpMeta;

  private AnalysisEngine generatorSubPhase, evidencerSubPhase, rankerSubPhase, prunerSubPhase;

  private JCas mergedJcas;

  private GerpableList<T, W> gerpables = new GerpableList<T, W>();

  private int gerpableIdx = 0;

  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    confs = getConfigurationTuples(context, "persistence-provider", "name", QA_INTERNAL_PHASEID,
            TIMEOUT_KEY, LAZY_LOAD_KEY, BaseExperimentBuilder.EXPERIMENT_UUID_PROPERTY,
            BaseExperimentBuilder.STAGE_ID_PROPERTY, "generators", "evidencers", "rankers",
            "pruners");
    try {
      gerpableClassName = (String) context.getConfigParameterValue("type");
      Class<? extends TOP> topClass = Class.forName(gerpableClassName).asSubclass(TOP.class);
      gerpableType = (Integer) topClass.getDeclaredField("type").get(null);
    } catch (Exception e) {
      throw new ResourceInitializationException(e);
    }
    gerpMeta = new GerpMetaWrapper(gerpableClassName,
            toClassNames((String) confs.get("generators")),
            toClassNames((String) confs.get("evidencers")),
            toClassNames((String) confs.get("rankers")),
            toClassNames((String) confs.get("pruners")));
  }

  private static CharMatcher matcher = CharMatcher.anyOf("./ :");

  // TODO Since the vbar in the descritpor should be removed and replaced with the YAML list
  // syntax, the returned value should be String array (String[])
  private static List<String> toClassNames(String options) {
    List<String> names = Lists.newArrayList();
    for (String option : options.split("\\n+")) {
      names.add(option.substring(matcher.lastIndexIn(option) + 1));
    }
    return names;
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    Map<String, Object> subPhaseConfs = copyTuples(confs, "persistence-provider",
            QA_INTERNAL_PHASEID, TIMEOUT_KEY, LAZY_LOAD_KEY,
            BaseExperimentBuilder.EXPERIMENT_UUID_PROPERTY, BaseExperimentBuilder.STAGE_ID_PROPERTY);
    mergedJcas = aJCas;
    WrapperHelper.unwrap(new WrapperIndexer(), gerpMeta, mergedJcas).addToIndexes(mergedJcas);
    JCasIterator jcasIter;
    // create generation subphase
    subPhaseConfs.put("name", confs.get("name") + "|GENERATION");
    subPhaseConfs.put("options", confs.get("generators"));
    generatorSubPhase = createBasePhase(subPhaseConfs);
    // execute generation subphase
    jcasIter = generatorSubPhase.processAndOutputNewCASes(mergedJcas);
    // merge generated gerpables
    mergeGerpables(jcasIter);
    // create evidencing subphase
    subPhaseConfs.put("name", confs.get("name") + "|EVIDENCING");
    subPhaseConfs.put("options", confs.get("evidencers"));
    evidencerSubPhase = createBasePhase(subPhaseConfs);
    // execute evidencing subphase
    jcasIter = evidencerSubPhase.processAndOutputNewCASes(mergedJcas);
    // merge evidences
    mergeEvidences(jcasIter);
    // create ranking subphase
    subPhaseConfs.put("name", confs.get("name") + "|RANKING");
    subPhaseConfs.put("options", confs.get("rankers"));
    rankerSubPhase = createBasePhase(subPhaseConfs);
    // execute ranking subphase
    jcasIter = rankerSubPhase.processAndOutputNewCASes(mergedJcas);
    // merge ranks
    mergeRanks(jcasIter);
    // create pruning subphase
    subPhaseConfs.put("name", confs.get("name") + "|PRUNING");
    subPhaseConfs.put("options", confs.get("pruners"));
    prunerSubPhase = createBasePhase(subPhaseConfs);
    // execute pruning subphase
    jcasIter = prunerSubPhase.processAndOutputNewCASes(mergedJcas);
    // merge pruning decisions
    mergePruningDecisions(jcasIter);
    // post processing
    ultimatePrune();
    removeAllTops(mergedJcas, GerpMeta.type);
  }

  private static AnalysisEngine createBasePhase(Map<String, Object> confs)
          throws AnalysisEngineProcessException {
    AnalysisEngineDescription aeDescription;
    try {
      aeDescription = AnalysisEngineFactory.createPrimitiveDescription(BasePhase.class,
              BaseExperimentBuilder.getParamList(confs));
    } catch (ResourceInitializationException e) {
      throw new AnalysisEngineProcessException(e);
    }
    try {
      return AnalysisEngineFactory.createAggregate(aeDescription);
    } catch (ResourceInitializationException e) {
      throw new AnalysisEngineProcessException(e);
    }
  }

  private void mergeGerpables(JCasIterator jcasIter) throws AnalysisEngineProcessException {
    while (jcasIter.hasNext()) {
      JCas jcas = jcasIter.next();
      TOP top = Iterables.getOnlyElement(getAllTops(jcas, gerpableType));
      @SuppressWarnings("unchecked")
      W gerpable = (W) WrapperHelper.wrap(new WrapperIndexer(), top);
      gerpable.setGerpMeta(gerpMeta);
      gerpables.add(gerpable);
      jcas.release();
    }
    WrapperIndexer indexer = new WrapperIndexer();
    for (W gerpable : gerpables.getGerpables()) {
      T top = WrapperHelper.unwrap(indexer, gerpable, mergedJcas);
      top.addToIndexes(mergedJcas);
    }
  }

  private void mergeEvidences(JCasIterator jcasIter) throws AnalysisEngineProcessException {
    Map<W, EvidenceWrapper<?, ?>> gerpable2evidences = Maps.newHashMap();
    while (jcasIter.hasNext()) {
      JCas jcas = jcasIter.next();
      for (TOP top : getAllTops(jcas, gerpableType)) {
        @SuppressWarnings("unchecked")
        W gerpable = (W) WrapperHelper.wrap(new WrapperIndexer(), top);
        gerpable2evidences.put(gerpable, gerpable.getEvidences().get(0));
      }
      gerpables.addAllEvidences(gerpable2evidences);
      jcas.release();
    }
    removeAllTops(mergedJcas, gerpableType);
    WrapperIndexer indexer = new WrapperIndexer();
    for (W gerpable : gerpables.getGerpables()) {
      T top = WrapperHelper.unwrap(indexer, gerpable, mergedJcas);
      top.addToIndexes(mergedJcas);
    }
  }

  private void mergeRanks(JCasIterator jcasIter) throws AnalysisEngineProcessException {
    Map<W, RankWrapper> gerpable2ranks = Maps.newHashMap();
    while (jcasIter.hasNext()) {
      JCas jcas = jcasIter.next();
      for (TOP top : getAllTops(jcas, gerpableType)) {
        @SuppressWarnings("unchecked")
        W gerpable = (W) WrapperHelper.wrap(new WrapperIndexer(), top);
        gerpable2ranks.put(gerpable, gerpable.getRanks().get(0));
      }
      gerpables.addAllRanks(gerpable2ranks);
      jcas.release();
    }
    removeAllTops(mergedJcas, gerpableType);
    WrapperIndexer indexer = new WrapperIndexer();
    for (W gerpable : gerpables.getGerpables()) {
      T top = WrapperHelper.unwrap(indexer, gerpable, mergedJcas);
      top.addToIndexes(mergedJcas);
    }
  }

  private void mergePruningDecisions(JCasIterator jcasIter) throws AnalysisEngineProcessException {
    Map<W, PruningDecisionWrapper> gerpable2pruningDecisions = Maps.newHashMap();
    while (jcasIter.hasNext()) {
      JCas jcas = jcasIter.next();
      for (TOP top : getAllTops(jcas, gerpableType)) {
        @SuppressWarnings("unchecked")
        W gerpable = (W) WrapperHelper.wrap(new WrapperIndexer(), top);
        gerpable2pruningDecisions.put(gerpable, gerpable.getPruningDecisions().get(0));
      }
      gerpables.addAllPruningDecisions(gerpable2pruningDecisions);
      jcas.release();
    }
    removeAllTops(mergedJcas, gerpableType);
  }

  private void ultimatePrune() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean hasNext() throws AnalysisEngineProcessException {
    if (gerpableIdx < gerpables.size()) {
      return true;
    } else {
      mergedJcas.release();
      return false;
    }
  }

  @Override
  public AbstractCas next() throws AnalysisEngineProcessException {
    JCas output = getEmptyJCas();
    CasCopier.copyCas(mergedJcas.getCas(), output.getCas(), true);
    T top = WrapperHelper.unwrap(new WrapperIndexer(), gerpables.get(gerpableIdx++), output);
    top.addToIndexes(output);
    return output;
  }

  @Override
  public void collectionProcessComplete() throws AnalysisEngineProcessException {
    super.collectionProcessComplete();
    generatorSubPhase.collectionProcessComplete();
    evidencerSubPhase.collectionProcessComplete();
    rankerSubPhase.collectionProcessComplete();
    prunerSubPhase.collectionProcessComplete();
  }

  public static Collection<TOP> getAllTops(JCas jcas, int type) {
    List<TOP> tops = Lists.newArrayList();
    FSIterator<TOP> topIter = jcas.getJFSIndexRepository().getAllIndexedFS(type);
    while (topIter.hasNext()) {
      tops.add(topIter.next());
    }
    return tops;
  }

  public static void removeAllTops(JCas jcas, int type) {
    FSIterator<TOP> topIter = jcas.getJFSIndexRepository().getAllIndexedFS(type);
    Set<TOP> tops = Sets.newHashSet();
    while (topIter.hasNext()) {
      tops.add(topIter.next());
    }
    for (TOP top : tops) {
      top.removeFromIndexes(jcas);
    }
  }

  private static Map<String, Object> getConfigurationTuples(UimaContext context, String... keys) {
    Map<String, Object> tuples = Maps.newLinkedHashMap();
    for (String key : keys) {
      tuples.put(key, context.getConfigParameterValue(key));
    }
    return tuples;
  }

  private static <K, V> Map<K, V> copyTuples(Map<K, V> map, K... keys) {
    Map<K, V> ret = Maps.newLinkedHashMap();
    for (K key : keys) {
      ret.put(key, map.get(key));
    }
    return ret;
  }

  public static void printCasIndexes(JCas jcas) {
    Iterator<FSIndex<TOP>> indexes = jcas.getJFSIndexRepository().getIndexes();
    while (indexes.hasNext()) {
      FSIndex<TOP> index = indexes.next();
      System.out.println(index.getType().getName());
      FSIterator<TOP> it = index.iterator();
      while (it.hasNext()) {
        System.out.println(" - " + it.next());
      }
    }
  }

}