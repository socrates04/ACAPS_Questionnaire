package com.example.questionnaire;

import java.util.Date;

public interface QuestionnaireProjection {
    String getId();
    String getTitle();
    Date getCreationDate();
    int getNbrSubmissions();
    boolean getValidated();
}
