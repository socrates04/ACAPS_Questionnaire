package com.example.questionnaire.controller;

import com.example.questionnaire.model.AppUser;
import com.example.questionnaire.model.ResponseDataDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.questionnaire.exceptions.InvalidResponse;
import com.example.questionnaire.exceptions.QuestionnaireNotFound;
import com.example.questionnaire.model.Response;
import com.example.questionnaire.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/responses")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    // Get all responses
    @GetMapping
    public ResponseEntity<List<Response>> getAllResponses() {
        List<Response> responses = responseService.getAll();
        return ResponseEntity.ok(responses);
    }



    // Get responses for a specific questionnaire
    @GetMapping("/questionnaire/{questionnaireId}")
    public ResponseEntity<List<Response>> getResponsesForQuestionnaire(@PathVariable String questionnaireId) {
        List<Response> responses = responseService.getResponseForQuestionnaire(questionnaireId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveResponse(@Valid @RequestBody ResponseDataDTO responseDataDTO){
        Response response = responseDataDTO.getResponse();
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        response.setRespondentId(user.getUserId());

        try {
            return ResponseEntity.ok(responseService.save(response));
        }catch (Exception e){
            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("message","Couldn't save response.");
            errorResponse.put("error",e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // Submit a new response
    @PutMapping("/{tempoId}/submit")
    public ResponseEntity<?> submitResponse(@PathVariable String tempoId) {
        try {
            Response savedResponse = responseService.submit(tempoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResponse);
        } catch (QuestionnaireNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionnaire not found: " + e.getMessage());
        } catch (InvalidResponse e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid response: " + e.getMessage());
        }
    }

    // Get a specific response by ID
    @GetMapping("/{responseId}")
    public ResponseEntity<Response> getResponseById(@PathVariable String responseId) {
        return responseService.getAll().stream()
                .filter(response -> response.getId().equals(responseId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}