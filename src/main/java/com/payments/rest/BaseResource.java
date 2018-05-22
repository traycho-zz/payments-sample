package com.payments.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.payments.exceptions.ApiValidationException;
import com.payments.model.User;
import com.payments.service.UserService;

public class BaseResource {
	
	@Context 
	private SecurityContext securityContext;
	
	/**
	 * User service.
	 */
	@Autowired
	protected UserService userService;


	protected User currentUser() {
		final String username = this.securityContext.getUserPrincipal().getName();
		return findUser(username);
	}
	
	
	protected User findUser(String username){
		User user = this.userService.findByEmail(username);
		if(user == null){
			throw new ApiValidationException("entityNotFound","Not existing user with username: " + username);
		}
		return user;
	}

}
