package com.example.questionnaire.exceptions;

public class UnvalidatedQuestionnaireException extends RuntimeException {
    public UnvalidatedQuestionnaireException(String message) {
        super(message);
    }
}
