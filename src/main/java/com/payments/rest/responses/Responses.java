package com.payments.rest.responses;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

public class Responses {
    
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;

    public static Response responseEntity(final Object entity) {
        if (entity == null) {
            return notFound(entity);
        }

        return ok(entity);
    }

    public static <T> Response responseEntity(List<T> list, boolean allowEmpty) {

        if (list == null) {
            if (allowEmpty) {
                list = Collections.emptyList();
            } else {
                return notFound(list);
            }
        }

        return ok(list);
    }

    public static Response ok() {
        return ok("OK");
    }

    public static Response ok(final Object entity) {
        return response(entity,Response.Status.OK);
    }
    
    public static Response notFound(final Object response) {
        return response(response,Response.Status.NOT_FOUND);
    }
    
    private static Response response(final Object response, final Status status){
        ResponseBuilder builder = Response.status(status).type(MediaType.APPLICATION_JSON);
        if(response != null){
            builder.entity(response);
        }
        return builder.build();
    }

    public static  Response notFound() {
        return notFound("Entity not found");
    }
    
    public static Response unauthorized(final Object response){
        return response(response,Response.Status.UNAUTHORIZED);
    }
    
    public static Response unprocessableEntity(final Object response) {
        return Response.status(HTTP_UNPROCESSABLE_ENTITY).entity(response).build();
    }
}