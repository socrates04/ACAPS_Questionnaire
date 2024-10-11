package com.example.questionnaire.repo;

import com.example.questionnaire.QuestionnaireProjection;
import com.example.questionnaire.model.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuestionnaireRepo extends MongoRepository<Questionnaire,String>,CustomQuestionnaireRepo {
    Optional<Questionnaire> getQuestionnaireByQuestionnaireIdAndVersion(String questionnaireId, int version);

}