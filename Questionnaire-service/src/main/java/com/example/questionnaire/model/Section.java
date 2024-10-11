package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class    Section {
    private String title;
    private int validationScore=0;
    private List<Question> questions = List.of();

    public Section(String title, int validationScore) {
        this.title = title;
        this.validationScore = validationScore;
    }

    public void addQuestion(Question q){ questions.add(q); }
}
