package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ResponseItem {
    private int questionNumber;
    private List<String> answers;
    private int score;
}