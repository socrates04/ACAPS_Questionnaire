package com.example.questionnaire.controller;

import com.example.questionnaire.exceptions.EditingOldQuestionnaire;
import com.example.questionnaire.exceptions.QuestionnaireNotFound;
import com.example.questionnaire.exceptions.QuestionnaireValidationException;
import com.example.questionnaire.exceptions.UnvalidatedQuestionnaireException;
import com.example.questionnaire.model.AppUser;
import com.example.questionnaire.model.Questionnaire;
import com.example.questionnaire.model.Section;
import com.example.questionnaire.repo.QuestionnaireRepo;
import com.example.questionnaire.service.QuestionnaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireRepo questionnaireRepo, QuestionnaireService questionnaireService) {
        this.questionnaireRepo = questionnaireRepo;
        this.questionnaireService = questionnaireService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(questionnaireService.getLatestVersionQuestionnaires());
    }

    @GetMapping("/{questionnaireId}")
    public ResponseEntity<?> getLastVersionQuestionnaire(@PathVariable String questionnaireId){
        try {
            return ResponseEntity.ok(questionnaireService.getMostRecentVersion(questionnaireId));
        }catch (QuestionnaireNotFound e){
            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("message", "couldn't fetch Questionnaire.");
            errorResponse.put("error","Not questionnaire matches the given questionnaireId");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/{questionnaireId}/versions")
    public ResponseEntity<?> getAllVersionForQuestionnaire(@PathVariable String questionnaireId){
        return ResponseEntity.ok(questionnaireService.getAllVersionsByQuestionnaireId(questionnaireId));
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<?> getQuestionnaire(@PathVariable String id){
        try {
            return  ResponseEntity.ok (questionnaireService.getQuestionnaireById(id));
        }catch (QuestionnaireNotFound e){
            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("message", "couldn't fetch Questionnaire.");
            errorResponse.put("error","Not questionnaire matches the given Id");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/{questionnaireId}/versions/{version}")
    public ResponseEntity<?> getQuestionnaireByVersion(@PathVariable String questionnaireId, @PathVariable int version ){
        try{
            Questionnaire q = questionnaireService.getQuestionnaireVersion(questionnaireId, version);
            return ResponseEntity.ok(q);
        }catch (QuestionnaireNotFound e){
            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Could not fetch the questionnaire.");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/{questionnaireId}/title={newTitle}")
    public ResponseEntity<?> changeTitle(@PathVariable String questionnaireId, @PathVariable String newTitle){
        try {
            return ResponseEntity.ok(questionnaireService.changeTitle(questionnaireId,newTitle));
        }catch (QuestionnaireNotFound e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Couldn't change the title.");
            errorResponse.put("message", "No record for the given questionnaireId is found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/{id}/validation={state}")
    public ResponseEntity<?> questionnaireValidation(@PathVariable String id, @PathVariable boolean state){
        try{
            Questionnaire newQ = questionnaireService.setValidation(id,state);
            AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            newQ.setValidatorName(user.getUsername());
            questionnaireService.save(newQ);
            if (newQ==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(newQ);
        }catch (QuestionnaireValidationException e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validation failed.");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PutMapping("/{id}/publication={state}")
    public ResponseEntity<?> questionnairePublication(@PathVariable String id, @PathVariable boolean state) {
        try{
            Questionnaire newQ = questionnaireService.setPublication(id,state);
            if (newQ==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(newQ);
        }catch (UnvalidatedQuestionnaireException e){
            // Sending a detailed error response with a message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Questionnaire is not validated.");
            errorResponse.put("message", "You cannot publish an unvalidated questionnaire.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // adding or modifying a section with a given title in a questionnaire with a given QuestionnaireId
    @PutMapping("/{questionnaireId}/sections")
    public ResponseEntity<?> modifySection(@PathVariable String questionnaireId, @RequestBody Section section){
        try{
            Questionnaire questionnaire= questionnaireService.updateSections(questionnaireId,section);
            return ResponseEntity.ok(questionnaire);
        }catch (QuestionnaireNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (EditingOldQuestionnaire e){
            Map<String, String> errorResponse=new HashMap<>();
            errorResponse.put("message","Couldn't update questionnaire.");
            errorResponse.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // removing a section from a questionnaire
    @PutMapping("/{questionnaireId}/sections/{sectionTitle}")
    public ResponseEntity<?> removeSection(@PathVariable String questionnaireId,@PathVariable String sectionTitle){
        try{
            return ResponseEntity.ok(questionnaireService.removeSection(questionnaireId,sectionTitle));
        }catch (QuestionnaireNotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // create a new questionnaire
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Questionnaire questionnaire){
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        questionnaire.setCreatorId(user.getUserId());
        questionnaire.setCreatorName(user.getUsername());
        Questionnaire q = questionnaireRepo.insert(questionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(q);
    }

    @DeleteMapping("/{_id}")
    public ResponseEntity<?> delete(@PathVariable String _id){
        try {
            questionnaireService.remove(_id);
        }catch (QuestionnaireNotFound e){
            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("message","failed to delete");
            errorResponse.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.noContent().build();
    }
}


