package com.example.questionnaire.config;

import com.example.questionnaire.feignRelated.ErrorDecoderImpl;
import com.example.questionnaire.feignRelated.FeignClientInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor feignClientInterceptor(RestTemplate restTemplate) {
        return new FeignClientInterceptor(restTemplate);
    }

    //@Bean
    public ErrorDecoder errorDecoder(){
        return new ErrorDecoderImpl();
    }
}
