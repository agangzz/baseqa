

/* First created by JCasGen Sat Apr 11 19:49:33 EDT 2015 */
package edu.cmu.lti.oaqa.type.kb;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.StringList;


/** A superclass for EntityConcept and RelationConcept.
 * Updated by JCasGen Sun Apr 19 19:46:50 EDT 2015
 * XML source: /home/yangzi/QA/baseqa/src/main/resources/baseqa/type/OAQATypes.xml
 * @generated */
public class Concept extends TOP {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Concept.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Concept() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Concept(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Concept(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: names

  /** getter for names - gets All name variants (preferred/default name, synonyms, lexicial variants, etc) of the concept.
   * @generated
   * @return value of the feature 
   */
  public StringList getNames() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_names == null)
      jcasType.jcas.throwFeatMissing("names", "edu.cmu.lti.oaqa.type.kb.Concept");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Concept_Type)jcasType).casFeatCode_names)));}
    
  /** setter for names - sets All name variants (preferred/default name, synonyms, lexicial variants, etc) of the concept. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setNames(StringList v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_names == null)
      jcasType.jcas.throwFeatMissing("names", "edu.cmu.lti.oaqa.type.kb.Concept");
    jcasType.ll_cas.ll_setRefValue(addr, ((Concept_Type)jcasType).casFeatCode_names, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: uris

  /** getter for uris - gets Array of uris that identify this named entity. There may be more than one uri if this named entity is ambiguous.
   * @generated
   * @return value of the feature 
   */
  public StringList getUris() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_uris == null)
      jcasType.jcas.throwFeatMissing("uris", "edu.cmu.lti.oaqa.type.kb.Concept");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Concept_Type)jcasType).casFeatCode_uris)));}
    
  /** setter for uris - sets Array of uris that identify this named entity. There may be more than one uri if this named entity is ambiguous. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setUris(StringList v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_uris == null)
      jcasType.jcas.throwFeatMissing("uris", "edu.cmu.lti.oaqa.type.kb.Concept");
    jcasType.ll_cas.ll_setRefValue(addr, ((Concept_Type)jcasType).casFeatCode_uris, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: ids

  /** getter for ids - gets A list of IDs (e.g. UI in UMLS) associated with this concept.
   * @generated
   * @return value of the feature 
   */
  public StringList getIds() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_ids == null)
      jcasType.jcas.throwFeatMissing("ids", "edu.cmu.lti.oaqa.type.kb.Concept");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Concept_Type)jcasType).casFeatCode_ids)));}
    
  /** setter for ids - sets A list of IDs (e.g. UI in UMLS) associated with this concept. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setIds(StringList v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_ids == null)
      jcasType.jcas.throwFeatMissing("ids", "edu.cmu.lti.oaqa.type.kb.Concept");
    jcasType.ll_cas.ll_setRefValue(addr, ((Concept_Type)jcasType).casFeatCode_ids, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: mentions

  /** getter for mentions - gets A list of ConceptMentions (text spans) that might be surface forms to this concept.
   * @generated
   * @return value of the feature 
   */
  public FSList getMentions() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_mentions == null)
      jcasType.jcas.throwFeatMissing("mentions", "edu.cmu.lti.oaqa.type.kb.Concept");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Concept_Type)jcasType).casFeatCode_mentions)));}
    
  /** setter for mentions - sets A list of ConceptMentions (text spans) that might be surface forms to this concept. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setMentions(FSList v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_mentions == null)
      jcasType.jcas.throwFeatMissing("mentions", "edu.cmu.lti.oaqa.type.kb.Concept");
    jcasType.ll_cas.ll_setRefValue(addr, ((Concept_Type)jcasType).casFeatCode_mentions, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: types

  /** getter for types - gets A list of concept types that the concept belongs to.
   * @generated
   * @return value of the feature 
   */
  public FSList getTypes() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_types == null)
      jcasType.jcas.throwFeatMissing("types", "edu.cmu.lti.oaqa.type.kb.Concept");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Concept_Type)jcasType).casFeatCode_types)));}
    
  /** setter for types - sets A list of concept types that the concept belongs to. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setTypes(FSList v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_types == null)
      jcasType.jcas.throwFeatMissing("types", "edu.cmu.lti.oaqa.type.kb.Concept");
    jcasType.ll_cas.ll_setRefValue(addr, ((Concept_Type)jcasType).casFeatCode_types, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    