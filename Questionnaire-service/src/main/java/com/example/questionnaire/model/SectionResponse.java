package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class SectionResponse {
    private String title;
    private List<ResponseItem> answers;
    private int score;
    private boolean validated;
}
