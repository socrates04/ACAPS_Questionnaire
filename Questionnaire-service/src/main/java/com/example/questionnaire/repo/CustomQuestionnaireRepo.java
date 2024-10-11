package com.example.questionnaire.repo;

import com.example.questionnaire.model.Questionnaire;
import com.example.questionnaire.model.QuestionnaireDTO;

import java.util.List;
import java.util.Optional;

public interface CustomQuestionnaireRepo {
    List<QuestionnaireDTO> findHighestVersionForEachQuestionnaire();
    List<QuestionnaireDTO> findAllVersionsByQuestionnaireId(String questionnaireId);
    boolean isMostRecentVersion(String questionnaireId, String questionnaireDocumentId);
    Optional<Questionnaire> getMostRecentVersion(String questionnaireId);
}
