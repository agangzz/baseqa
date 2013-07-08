package edu.cmu.lti.oaqa.baseqa.data.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.EmptyFSList;
import org.apache.uima.jcas.cas.EmptyStringList;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.NonEmptyFSList;
import org.apache.uima.jcas.cas.NonEmptyStringList;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class WrapperHelper {

  public static List<String> wrapStringList(StringList list) {
    List<String> wrappers = new ArrayList<String>();
    StringList tail = list;
    while (tail instanceof NonEmptyStringList) {
      wrappers.add(((NonEmptyStringList) tail).getHead());
      tail = ((NonEmptyStringList) tail).getTail();
    }
    return wrappers;
  }

  public static StringList unwrapStringList(List<String> wrappers, JCas jcas) {
    StringList list = new EmptyStringList(jcas);
    StringList tail;
    for (String wrapper : Lists.reverse(wrappers)) {
      tail = list;
      list = new NonEmptyStringList(jcas);
      ((NonEmptyStringList) list).setHead(wrapper);
      ((NonEmptyStringList) list).setTail(tail);
    }
    return list;
  }

  public static List<String> wrapStringArray(StringArray array) {
    List<String> wrappers = new ArrayList<String>(array.size());
    for (int i = 0; i < array.size(); i++) {
      wrappers.add(array.get(i));
    }
    return wrappers;
  }

  public static StringArray unwrapStringArray(List<String> wrappers, JCas jcas) {
    StringArray array = new StringArray(jcas, wrappers.size());
    int i = 0;
    for (String wrapper : wrappers) {
      array.set(i++, wrapper);
    }
    return array;
  }

  public static <T extends TOP, W extends TopWrapper<T>> List<W> wrapTopList(FSList list,
          Class<W> wrapperClass) throws AnalysisEngineProcessException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    List<W> wrappers = new ArrayList<W>();
    FSList tail = list;
    while (tail instanceof NonEmptyFSList) {
      TOP head = ((NonEmptyFSList) tail).getHead();
      wrappers.add(matchSubclassAndWrap(head, wrapperClass));
      tail = ((NonEmptyFSList) tail).getTail();
    }
    return wrappers;
  }

  public static <T extends TOP, W extends TopWrapper<T>> FSList unwrapTopList(List<W> wrappers,
          JCas jcas) throws AnalysisEngineProcessException {
    FSList list = new EmptyFSList(jcas);
    FSList tail;
    for (W wrapper : Lists.reverse(wrappers)) {
      tail = list;
      list = new NonEmptyFSList(jcas);
      ((NonEmptyFSList) list).setHead(wrapper.unwrap(jcas));
      ((NonEmptyFSList) list).setTail(tail);
    }
    return list;
  }

  public static <T extends TOP, W extends TopWrapper<T>> List<W> wrapTopArray(FSArray array,
          Class<W> wrapperClass) throws AnalysisEngineProcessException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    List<W> wrappers = new ArrayList<W>(array.size());
    for (int i = 0; i < array.size(); i++) {
      wrappers.add(matchSubclassAndWrap((TOP) array.get(i), wrapperClass));
    }
    return wrappers;
  }

  public static <T extends TOP, W extends TopWrapper<T>> FSArray unwrapTopArray(List<W> wrappers,
          JCas jcas) throws AnalysisEngineProcessException {
    FSArray array = new FSArray(jcas, wrappers.size());
    int i = 0;
    for (W wrapper : wrappers) {
      array.set(i, wrapper.unwrap(jcas));
    }
    return array;
  }

  public static <T extends Annotation, W extends AnnotationWrapper<T>> List<W> wrapAnnotationList(
          FSList list, Class<W> wrapperClass) throws AnalysisEngineProcessException,
          InstantiationException, IllegalAccessException, ClassNotFoundException {
    List<W> wrappers = new ArrayList<W>();
    FSList tail = list;
    while (tail instanceof NonEmptyFSList) {
      TOP head = ((NonEmptyFSList) tail).getHead();
      wrappers.add(matchSubclassAndWrap(head, wrapperClass));
      tail = ((NonEmptyFSList) tail).getTail();
    }
    return wrappers;
  }

  public static <T extends Annotation, W extends AnnotationWrapper<T>> FSList unwrapAnnotationList(
          List<W> wrappers, JCas jcas) throws AnalysisEngineProcessException {
    FSList list = new EmptyFSList(jcas);
    FSList tail;
    for (W wrapper : Lists.reverse(wrappers)) {
      tail = list;
      list = new NonEmptyFSList(jcas);
      ((NonEmptyFSList) list).setHead(wrapper.unwrap(jcas));
      ((NonEmptyFSList) list).setTail(tail);
    }
    return list;
  }

  public static <T extends Annotation, W extends AnnotationWrapper<T>> List<W> wrapAnnotationArray(
          FSArray array, Class<W> wrapperClass) throws AnalysisEngineProcessException,
          InstantiationException, IllegalAccessException, ClassNotFoundException {
    List<W> wrappers = new ArrayList<W>(array.size());
    for (int i = 0; i < array.size(); i++) {
      wrappers.add(matchSubclassAndWrap((TOP) array.get(i), wrapperClass));
    }
    return wrappers;
  }

  public static <T extends Annotation, W extends AnnotationWrapper<T>> FSArray unwrapAnnotationArray(
          List<W> wrappers, JCas jcas) throws AnalysisEngineProcessException {
    FSArray array = new FSArray(jcas, wrappers.size());
    int i = 0;
    for (W wrapper : wrappers) {
      array.set(i, wrapper.unwrap(jcas));
    }
    return array;
  }

  public static <T extends TOP, W extends TopWrapper<T>> Set<? extends W> wrapAllFromJCas(
          JCas jcas, Class<W> clazz) throws InstantiationException, IllegalAccessException,
          IllegalArgumentException, NoSuchFieldException, SecurityException,
          AnalysisEngineProcessException, ClassNotFoundException {
    int type = clazz.newInstance().getTypeClass().getField("type").getInt(null);
    Set<W> tops = Sets.newHashSet();
    for (TOP top : ImmutableList.copyOf(jcas.getJFSIndexRepository().getAllIndexedFS(type))) {
      tops.add(matchSubclassAndWrap(top, clazz));
    }
    return tops;
  }

  public static <T extends TOP, W extends TopWrapper<T>> W wrap(T top, Class<W> wrapperClass)
          throws InstantiationException, IllegalAccessException, AnalysisEngineProcessException {
    W inst = wrapperClass.newInstance();
    inst.wrap(top);
    return inst;
  }

  @SuppressWarnings("unchecked")
  public static <T extends TOP, W extends TopWrapper<T>> W matchSubclassAndWrap(TOP top,
          Class<W> superClass) throws AnalysisEngineProcessException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    // FIXME Different TopWrapper implementations may have different ways to store the actual
    // implementation subclass in TOP, the following method is only used for OAQATop. A method needs
    // to be defined in the interface class, e.g. Class<? extends TopWrapper<?
    // extends T>> getWrapperClass(TOP top);
    Feature feature = top.getType().getFeatureByBaseName("implementingWrapper");
    String className = top.getFeatureValueAsString(feature);
    Class<? extends W> clazz = Class.forName(className).asSubclass(superClass);
    return wrap((T) top, clazz);
  }
}