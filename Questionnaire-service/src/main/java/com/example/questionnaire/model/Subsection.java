package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@AllArgsConstructor@NoArgsConstructor
public class Subsection {
    private String triggerAnswer;
    private List<Question> questions;

    public void addQuestion(Question q){
        questions.add(q);
    }
}
