package com.payments.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.payments.exceptions.mapper.ValidationExceptionMapper;
import com.payments.rest.AuthResource;
import com.payments.rest.CardResource;
import com.payments.rest.PaymentResource;
import com.payments.rest.UserResource;
import com.payments.security.auth.AuthenticationFilter;

@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		register(CardResource.class);
		register(UserResource.class);
		register(AuthResource.class);
		register(PaymentResource.class);
		register(ValidationExceptionMapper.class);
		register(AuthenticationFilter.class);
	}
}
