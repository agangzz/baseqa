

/* First created by JCasGen Mon Jul 08 17:12:14 EDT 2013 */
package org.oaqa.model.gerp;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.StringList;
import org.oaqa.model.core.OAQAAnnotation;


/** A higher level supertype for all GERPified annotations that defines the common attributes (G/E/R/P).
 * Updated by JCasGen Mon Jul 08 17:12:14 EDT 2013
 * XML source: C:/Users/yangz13/QA/baseqa/src/main/resources/edu/cmu/lti/oaqa/OAQATypes.xml
 * @generated */
public class GerpAnnotation extends OAQAAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GerpAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected GerpAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public GerpAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public GerpAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public GerpAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: generators

  /** getter for generators - gets An array of genenator names that nominee this feature structure to be the candidate of a certain targeted entity.
   * @generated */
  public StringList getGenerators() {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_generators == null)
      jcasType.jcas.throwFeatMissing("generators", "org.oaqa.model.gerp.GerpAnnotation");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_generators)));}
    
  /** setter for generators - sets An array of genenator names that nominee this feature structure to be the candidate of a certain targeted entity. 
   * @generated */
  public void setGenerators(StringList v) {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_generators == null)
      jcasType.jcas.throwFeatMissing("generators", "org.oaqa.model.gerp.GerpAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_generators, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: evidences

  /** getter for evidences - gets An array of evidences generated by evidencers for a particular GERP feature structure. Each element corresponds to the evidence from an evidencer.
   * @generated */
  public FSList getEvidences() {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_evidences == null)
      jcasType.jcas.throwFeatMissing("evidences", "org.oaqa.model.gerp.GerpAnnotation");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_evidences)));}
    
  /** setter for evidences - sets An array of evidences generated by evidencers for a particular GERP feature structure. Each element corresponds to the evidence from an evidencer. 
   * @generated */
  public void setEvidences(FSList v) {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_evidences == null)
      jcasType.jcas.throwFeatMissing("evidences", "org.oaqa.model.gerp.GerpAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_evidences, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: ranks

  /** getter for ranks - gets An array of ranks generated by rankers for a particular GERP feature structure. Each element corresponds to the rank from a ranker.
   * @generated */
  public FSList getRanks() {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_ranks == null)
      jcasType.jcas.throwFeatMissing("ranks", "org.oaqa.model.gerp.GerpAnnotation");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_ranks)));}
    
  /** setter for ranks - sets An array of ranks generated by rankers for a particular GERP feature structure. Each element corresponds to the rank from a ranker. 
   * @generated */
  public void setRanks(FSList v) {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_ranks == null)
      jcasType.jcas.throwFeatMissing("ranks", "org.oaqa.model.gerp.GerpAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_ranks, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: pruningDecisions

  /** getter for pruningDecisions - gets An array of purning decisions generated by pruners for a particular GERP feature structure. Each element corresponds to the pruning decision from a pruner. The final decision will be made by a multiplexer (or phrase driver).
   * @generated */
  public FSList getPruningDecisions() {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_pruningDecisions == null)
      jcasType.jcas.throwFeatMissing("pruningDecisions", "org.oaqa.model.gerp.GerpAnnotation");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_pruningDecisions)));}
    
  /** setter for pruningDecisions - sets An array of purning decisions generated by pruners for a particular GERP feature structure. Each element corresponds to the pruning decision from a pruner. The final decision will be made by a multiplexer (or phrase driver). 
   * @generated */
  public void setPruningDecisions(FSList v) {
    if (GerpAnnotation_Type.featOkTst && ((GerpAnnotation_Type)jcasType).casFeat_pruningDecisions == null)
      jcasType.jcas.throwFeatMissing("pruningDecisions", "org.oaqa.model.gerp.GerpAnnotation");
    jcasType.ll_cas.ll_setRefValue(addr, ((GerpAnnotation_Type)jcasType).casFeatCode_pruningDecisions, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    