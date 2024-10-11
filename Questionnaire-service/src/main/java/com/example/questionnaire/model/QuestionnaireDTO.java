package com.example.questionnaire.model;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionnaireDTO {
    private String _id;
    private String questionnaireId;
    private String title;
    private Date creationDate;
    private int nbrSubmissions;
    private boolean validated;
    private  boolean published;
    private int version;
}

