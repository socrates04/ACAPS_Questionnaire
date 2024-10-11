package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "questionnaire")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Questionnaire {
    @Id
    private String id;// for identification in the database
    private String questionnaireId = UUID.randomUUID().toString(); // for identification across multiple versions
    private int version=0;// for identifying the version

    private String title;
    private Date creationDate = new Date();
    private boolean validated=false;
    private Date validationDate;
    private boolean distributed = false;
    private int validationScore;
    private int nbrSubmissions=0;
    private Date lastUpdated = new Date();

    private String validatorId;
    private String validatorName;
    private long creatorId;
    private String creatorName;


    private List<Section> sections = new ArrayList<Section>();

    public Questionnaire(String title, long creatorId, String creatorName, int validationScore) {
        this.title = title;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.validationScore=validationScore;
    }

    public boolean addSection(Section section){
        // testing the uniqueness of the section title
        Optional<Section> fs = sections.stream()
                .filter(section1 -> section1.getTitle().equalsIgnoreCase(section.getTitle()))
                .findFirst();
        if (fs.isPresent()) return false;
        sections.add(section);
        return true;
    }

    public void updateVersion(){
        setId(null);// to create a new entry in the db
        version++;
    }

    // tests if the questionnaire meets the validation standards
    public boolean testForValidation(){
        if (sections.size() == 0) return false;
        for (Section s:sections ) {
            if (s.getQuestions().size()==0) return false;
        }
        return true;
    }

    public Optional<Section> getSection(String title){
        return sections.stream()
                .filter(section1 -> section1.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    public void removeSection(String title){
        sections.stream()
                .filter(s -> s.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .ifPresent(section -> sections.remove(section));
    }

    public void addSubmission(){nbrSubmissions++;}
}
