package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Question {
    private int number;
    private String text;
    private QuestionType type;
    private boolean required=true;
    private List<Option> choices= List.of();

    public Question(int number, String text, QuestionType type) {
        this.number = number;
        this.text = text;
        this.type = type;
    }
    public Question(int number, String text, QuestionType type,boolean required){
        this.number = number;
        this.text = text;
        this.type = type;
        this.required = required;
    }

    public void addOption(String name, int grade){
        choices.add(new Option(name,grade));
    }

}
