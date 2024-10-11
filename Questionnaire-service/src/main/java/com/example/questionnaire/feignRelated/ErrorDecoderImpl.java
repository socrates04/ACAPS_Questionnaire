package com.example.questionnaire.feignRelated;

import com.example.questionnaire.exceptions.BadRequestException;
import com.example.questionnaire.exceptions.InternalServerErrorException;
import com.example.questionnaire.exceptions.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class ErrorDecoderImpl implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                // Handle 400 Bad Request
                return new BadRequestException("Bad Request.");
            case 404:
                // Handle 404 Not Found
                String msg = "Not Found.";
                System.out.println(msg);
                return new NotFoundException(msg);
            case 500:
                // Handle 500 Internal Server Error
                return new InternalServerErrorException("Internal Server Error.");
            default:
                return new Exception("Generic error.");
        }
    }
}
