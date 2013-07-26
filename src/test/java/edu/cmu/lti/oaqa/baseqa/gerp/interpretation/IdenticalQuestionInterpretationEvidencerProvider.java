package edu.cmu.lti.oaqa.baseqa.gerp.interpretation;

import edu.cmu.lti.oaqa.baseqa.data.kb.InterpretationWrapper;
import edu.cmu.lti.oaqa.baseqa.gerp.interpretation.AbstractQuestionInterpretationEvidencerProvider;
import edu.cmu.lti.oaqa.gerp.data.DefaultEvidenceWrapper;
import edu.cmu.lti.oaqa.gerp.data.EvidenceWrapper;

public class IdenticalQuestionInterpretationEvidencerProvider extends
        AbstractQuestionInterpretationEvidencerProvider {

  @Override
  protected EvidenceWrapper<?, ?> evidence(InterpretationWrapper gerpable) {
    return new DefaultEvidenceWrapper(1.0f);
  }

}