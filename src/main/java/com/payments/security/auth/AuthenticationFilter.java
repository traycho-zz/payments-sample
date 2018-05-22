package com.payments.security.auth;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.payments.security.annotations.Secured;
import com.payments.security.jwt.JwtAuthenticator;



@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Autowired
	private JwtAuthenticator authenticator;

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		Method method = resourceInfo.getResourceMethod();
		LOG.debug("Handling request");

		if (method != null && method.isAnnotationPresent(Secured.class)) {
			LOG.debug("Handling seecured request for method {}", method.getName());
			authenticator.authenticate(requestContext);
		}

	}

}
