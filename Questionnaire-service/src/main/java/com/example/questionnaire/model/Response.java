package com.example.questionnaire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Document("response")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Response {
    private String id;

    private long respondentId;

    private String questionnaireId;

    private Integer version;

    private List<SectionResponse> sections;

    private boolean submitted=false;
    private Date submittedDate= new Date();

    private Date lastUpdate= new Date();

    private int totalScore;
    private boolean validated;

    public Response(String questionnaireId, List<SectionResponse> sections) {
        this.questionnaireId = questionnaireId;
        this.sections = sections;
    }

    public Response(ResponseDataDTO dto){
        id = dto.getId();
        version= dto.getVersion();
        questionnaireId = dto.getQuestionnaireId();
        sections = dto.getSections();
    }

    public void update(Response response){
        version= response.getVersion();
        sections = response.getSections();
        lastUpdate=new Date();
    }

}

