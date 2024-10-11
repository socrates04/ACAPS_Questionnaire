package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppRole {
    private String name;
    private Integer id;

    public int getId(){return id;}
}
