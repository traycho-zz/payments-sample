package com.payments.exceptions.mapper;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;



@Provider
public class ValidationExceptionMapper extends BaseValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(ValidationException exception) {
        return buildResponse(exception, MediaType.APPLICATION_JSON, HTTP_UNPROCESSABLE_ENTITY);
    }

}