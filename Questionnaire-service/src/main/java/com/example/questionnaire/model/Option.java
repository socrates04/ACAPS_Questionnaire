package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class Option {
    private String name;
    private int grade;
    private Subsection subSection;

    Option(String name, int grade){ this.name=name; this.grade=grade;}
}
