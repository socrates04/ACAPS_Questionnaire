package com.example.questionnaire.feignRelated;


import com.example.questionnaire.model.AppUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthServiceFeignClient {
    @GetMapping("/users/{username}")
    AppUser getUserByUsername(@PathVariable String username);

}
