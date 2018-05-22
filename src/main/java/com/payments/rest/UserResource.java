package com.payments.rest;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.payments.model.User;
import com.payments.rest.requests.CreateUserRequest;
import com.payments.rest.responses.UserResponse;
import com.payments.security.annotations.Secured;
import com.payments.service.UserService;

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(UserResource.PATH)
public class UserResource extends BaseResource {
	
	public final static String PATH = "/users";
	
	@Autowired
	private UserService userService;
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(CreateUserRequest request) {
		
		User user = new User();
		user.setEmail(request.getUsername());
		user.setPassword(request.getPassword());
		user.setUid(UUID.randomUUID().toString());
		user = userService.create(user);
		
		UserResponse response = new UserResponse();
		response.setUid(user.getUid());
		response.setUsername(user.getEmail());
		
        return Response.ok(response).build();
	}	
	
	@GET
	@Path("/")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get() {
		final User user = currentUser();
		
		UserResponse response = new UserResponse();
		response.setUid(user.getUid());
		response.setUsername(user.getEmail());
		
        return Response.ok(response).build();
	}	

}