package com.payments.rest.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ErrorResponse {

    private String code;

    private String message;

    private List<ErrorEntry> errors;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<ErrorEntry> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorEntry> errors) {
        this.errors = errors;
    }
    
    
    public static ErrorResponse with(final String code, final String message){
        final ErrorResponse response = new ErrorResponse();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}