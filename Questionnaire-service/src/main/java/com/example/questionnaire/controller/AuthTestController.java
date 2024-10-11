package com.example.questionnaire.controller;

import com.example.questionnaire.feignRelated.AuthServiceFeignClient;
import com.example.questionnaire.model.AppUser;
import com.example.questionnaire.model.Questionnaire;
import com.example.questionnaire.service.QuestionnaireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class AuthTestController {

    private final AuthServiceFeignClient authServiceFeignClient;
    private final QuestionnaireService questionnaireService;

    public AuthTestController(AuthServiceFeignClient authServiceFeignClient, QuestionnaireService questionnaireService) {
        this.authServiceFeignClient = authServiceFeignClient;
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUsers(@PathVariable String username){
        try{
            AppUser user = authServiceFeignClient.getUserByUsername(username);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,String> msg = new HashMap<>();
            msg.put("Message","Failed to fetch user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }





}
