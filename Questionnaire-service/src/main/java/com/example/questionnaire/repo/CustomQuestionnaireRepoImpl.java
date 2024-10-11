package com.example.questionnaire.repo;


import com.example.questionnaire.model.Questionnaire;
import com.example.questionnaire.model.QuestionnaireDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

public class CustomQuestionnaireRepoImpl implements CustomQuestionnaireRepo{

    private final MongoTemplate mongoTemplate;

    public CustomQuestionnaireRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<QuestionnaireDTO> findHighestVersionForEachQuestionnaire() {
        SortOperation sort = Aggregation.sort(Sort.by("questionnaireId").ascending().and(Sort.by("version").descending()));
        GroupOperation group = Aggregation.group("questionnaireId")
                .first(Aggregation.ROOT).as("docWithMaxVersion");
        ProjectionOperation project = Aggregation.project()
                .and("docWithMaxVersion._id").as("_id")
                .and("docWithMaxVersion.questionnaireId").as("questionnaireId")
                .and("docWithMaxVersion.version").as("version")
                .and("docWithMaxVersion.title").as("title")
                .and("docWithMaxVersion.nbrSubmissions").as("nbrSubmissions")
                .and("docWithMaxVersion.validated").as("validated")
                .and("docWithMaxVersion.published").as("published")
                .and("docWithMaxVersion.creationDate").as("creationDate");

        Aggregation aggregation = Aggregation.newAggregation(sort, group, project);
        AggregationResults<QuestionnaireDTO> results = mongoTemplate.aggregate(aggregation, "questionnaire", QuestionnaireDTO.class);

        return results.getMappedResults();
    }

    @Override
    public List<QuestionnaireDTO> findAllVersionsByQuestionnaireId(String questionnaireId) {
        // Create the matching stage
        MatchOperation match = Aggregation.match(Criteria.where("questionnaireId").is(questionnaireId));

        // Optional: Add sorting by version, in descending or ascending order
        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "version"));

        // Optional: Projection to return only specific fields
        ProjectionOperation project = Aggregation.project("_id", "questionnaireId", "version", "title", "nbrSubmissions", "creationDate");

        // Build the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(match, sort, project);

        // Execute the aggregation
        AggregationResults<QuestionnaireDTO> results = mongoTemplate.aggregate(aggregation, "questionnaire", QuestionnaireDTO.class);

        return results.getMappedResults();
    }

    @Override
    public boolean isMostRecentVersion(String questionnaireId, String questionnaireDocumentId) {
        // Step 1: Retrieve the version of the current questionnaire document
        MatchOperation matchById = Aggregation.match(Criteria.where("_id").is(questionnaireDocumentId));
        ProjectionOperation projectCurrentVersion = Aggregation.project("version");

        Aggregation aggregationCurrentVersion = Aggregation.newAggregation(matchById, projectCurrentVersion);
        AggregationResults<QuestionnaireDTO> currentVersionResult = mongoTemplate
                .aggregate(aggregationCurrentVersion, "questionnaire", QuestionnaireDTO.class);

        if (currentVersionResult.getMappedResults().isEmpty()) {
            throw new IllegalArgumentException("No questionnaire found with the given id");
        }

        int currentVersion = currentVersionResult.getMappedResults().get(0).getVersion();

        // Step 2: Retrieve the highest version for the given questionnaireId
        MatchOperation matchByQuestionnaireId = Aggregation.match(Criteria.where("questionnaireId").is(questionnaireId));
        SortOperation sortByVersionDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "version"));
        LimitOperation limit = Aggregation.limit(1); // Only get the highest version
        ProjectionOperation projectMaxVersion = Aggregation.project("version");

        Aggregation aggregationHighestVersion = Aggregation.newAggregation(matchByQuestionnaireId, sortByVersionDesc, limit, projectMaxVersion);
        AggregationResults<QuestionnaireDTO> highestVersionResult = mongoTemplate.
                aggregate(aggregationHighestVersion, "questionnaire", QuestionnaireDTO.class);

        if (!highestVersionResult.getMappedResults().isEmpty()) {
            int highestVersion = highestVersionResult.getMappedResults().get(0).getVersion();

            // Step 3: Compare the current version with the highest version
            return currentVersion == highestVersion;
        }
        return false;
    }

    @Override
    public Optional<Questionnaire> getMostRecentVersion(String questionnaireId){
        MatchOperation matchByQuestionnaireId = Aggregation.match(Criteria.where("questionnaireId").is(questionnaireId));
        SortOperation sortByVersionDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "version"));
        LimitOperation limit = Aggregation.limit(1); // Only get the highest version

        Aggregation aggregation = Aggregation
                .newAggregation(matchByQuestionnaireId,sortByVersionDesc,limit);
        AggregationResults<Questionnaire> mostRecentVersion = mongoTemplate
                .aggregate(aggregation,"questionnaire",Questionnaire.class);

        if(!mostRecentVersion.getMappedResults().isEmpty())
            return Optional.of(mostRecentVersion.getMappedResults().get(0));

        return Optional.empty();
    }
}

