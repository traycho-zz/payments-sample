package com.payments.exceptions.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.validation.Path.Node;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.util.StringUtils;

import com.payments.exceptions.ApiValidationException;
import com.payments.rest.responses.ErrorEntry;
import com.payments.rest.responses.ErrorResponse;



public class BaseValidationExceptionMapper {
	
	/**
     * Unprocessable entity status code.
     */
    protected static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    
    protected static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    
    protected static final Map<Class<?>,String> CONSTANT_TO_ERROR_CODE = new HashMap<>();
    
    static {
        CONSTANT_TO_ERROR_CODE.put(NotNull.class, "requiredField");
        //CONSTANT_TO_ERROR_CODE.put(RequestBody.class, "emptyBody");
    }
    
    
    public  Response buildResponse(final ValidationException exception, String mediaType, int status) {
        Object entity = exception;
        
        if(exception instanceof ConstraintViolationException){
            entity = buildErrorResponse((ConstraintViolationException) exception);
        }else if(exception instanceof ApiValidationException){
            entity = buildErrorResponse((ApiValidationException) exception);
        }
        
        final ResponseBuilder builder = Response.status(status).entity(entity);
        return builder.type(mediaType).build();
    }
    
    
    private  ErrorResponse buildErrorResponse(final ApiValidationException exception){
        final ErrorResponse response = new ErrorResponse();
        response.setCode("validationFailed");
        response.setMessage(exception.getMessage());
        response.setCode(exception.getCode());
        return response;
    }
    
    private  ErrorResponse buildErrorResponse(final ConstraintViolationException exception){
        final ErrorResponse response = new ErrorResponse();
        response.setCode("validationFailed");
        response.setMessage(VALIDATION_FAILED_MESSAGE);
        
        final Set<ConstraintViolation<?>> constraints =  exception.getConstraintViolations();
        int errorsSize = constraints.size();
        
        if(errorsSize > 0 ){
            final List<ErrorEntry> errors = new ArrayList<>(errorsSize);
            for(final ConstraintViolation<?> constaint : constraints){
                final ErrorEntry error = new ErrorEntry();
                final Class<?> annotation = constaint.getConstraintDescriptor().getAnnotation().annotationType();

                final String errorCode = CONSTANT_TO_ERROR_CODE.get(annotation);
                
                error.setCode(errorCode);
                error.setMessage(constaint.getMessage());
                
                final String field = getFieldName(constaint.getPropertyPath());
                if(field == null || !StringUtils.isEmpty(field)){
                    error.setField(field);
                }
                errors.add(error);
            }
            response.setErrors(errors);
        }
        
        return response;
    }
    
    
    private String getFieldName(final Path path){
    	String fieldName = null;
    	Iterator<Node> iterator = path.iterator();
    	while(iterator.hasNext()){
    		Node node = iterator.next();
    		fieldName = node.getName();
    	}
    	return fieldName;
    	
    }
}