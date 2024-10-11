package com.example.questionnaire.feignRelated;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private String token ;

    private final RestTemplate restTemplate;

    private final JwtUtil jwtUtil = new JwtUtil();

    public FeignClientInterceptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //System.out.println("Interceptor intercepting request.");
        if (token == null || jwtUtil.isTokenExpired(token)) {
            //System.out.println("No valid token found.");
            token = fetchToken();
        }
        requestTemplate.header("Authorization", "Bearer " + token);
    }

    private String fetchToken() {
        //System.out.println("trying to fetch token...");

        try{
            ResponseEntity<String> response = restTemplate.postForEntity("http://auth-service/login",
                    new AuthRequest("APP_SERVICE","test"), String.class);
            //System.out.println("Request sent. and got response.");

            if (response.getStatusCode() != HttpStatus.OK) {
                //System.out.println("Questionnaire service Authentication failed. Couldn't get token. response :"
                  //      +response.getStatusCode());
            }
            if (response.getHeaders().containsKey("Authorization")) {
                String newToken = response.getHeaders().get("Authorization").get(0);
                //System.out.println("Got token: "+newToken);
                return newToken;
            }
        }catch (HttpClientErrorException ex){
            ex.printStackTrace();
        }

        return token;

    }
}
