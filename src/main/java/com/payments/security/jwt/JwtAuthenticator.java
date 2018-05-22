package com.payments.security.jwt;

import java.util.HashSet;

import javax.annotation.Resource;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.payments.rest.responses.ErrorResponse;
import com.payments.rest.responses.Responses;


@Component(JwtAuthenticator.BEAN_NAME)
public class JwtAuthenticator {
	
	public static final String BEAN_NAME = "JwtAuthenticator";
	
	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticator.class);
	
    private static final String AUTH_PREFIX = "Bearer";

    private static final String INVALID_ACCESS_TOKEN = "Invalid access token";

    private static final String EXPIRED_ACCESS_TOEKN = "Access token expired";

    @Autowired
    private JwtTokenHandler tokenHandler;


    public boolean authenticate(final ContainerRequestContext requestContext)  {
    	
        final String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        final String accessToken = getToken(authorization);

        if (accessToken == null) {
            final ErrorResponse response = ErrorResponse.with("invalidToken", INVALID_ACCESS_TOKEN);
            requestContext.abortWith(Responses.unauthorized(response));
            LOG.debug("Access token not provided in request");
            return false;
        }

        LOG.debug("Parsing access token: {}",accessToken);
        final JwtToken token = tokenHandler.parse(accessToken);

        LOG.debug("Validating token : {}", token);

        if (token == null || !token.isAuth()) {
            final ErrorResponse response = ErrorResponse.with("invalidToken", INVALID_ACCESS_TOKEN);
            requestContext.abortWith(Responses.unauthorized(response));
            LOG.debug("Invalid access  token");
            return false;
        }

        if (token.isExpired()) {
            final ErrorResponse response = ErrorResponse.with("accessTokenExpired", EXPIRED_ACCESS_TOEKN);
            requestContext.abortWith(Responses.unauthorized(response));
            LOG.debug("Access token expired: {}", token.getExpiration());
            return false;
        }

        final SecurityContext context = new ApiSecurityContext(token.getSubject(), new HashSet<>());
        requestContext.setSecurityContext(context);
        
		return true;

    }

    private String getToken(final String header) {
        if (header == null || !header.startsWith(AUTH_PREFIX)) {
            return null;
        }

        String token = null;
        final String[] auth = header.split(" ");

        if (auth.length == 2) {
            token = auth[1];
        }

        return token;
    }
}
