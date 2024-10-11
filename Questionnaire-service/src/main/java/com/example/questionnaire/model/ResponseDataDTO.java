package com.example.questionnaire.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataDTO {
    private String id;
    @NotNull(message = "The questionnaire must be referenced.")
    private String questionnaireId;
    @NotNull(message = "The version of the questionnaire if mandatory.")
    private Integer version;

    private String respondentId;

    private List<SectionResponse> sections;

    public ResponseDataDTO(String questionnaireId, String respondentId, List<SectionResponse> sections) {
        this.questionnaireId = questionnaireId;
        this.respondentId = respondentId;
        this.sections = sections;
    }

    public Response getResponse(){
        return new Response(this);
    }
}
