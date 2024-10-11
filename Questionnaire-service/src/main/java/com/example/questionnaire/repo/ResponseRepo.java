package com.example.questionnaire.repo;

import com.example.questionnaire.model.Response;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface ResponseRepo extends MongoRepository<Response,String> {
    List<Response> findResponseByQuestionnaireId(String questionnaireId);
    Optional<Response> getResponsesByQuestionnaireIdAndRespondentId(String questionnaireId, long     respondentId);

    List<Response> getResponsesByRespondentId(long RespondentId);
}

