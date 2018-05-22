package com.payments.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.payments.model.User;
import com.payments.rest.requests.CredentialsRequest;
import com.payments.rest.responses.AuthResponse;
import com.payments.rest.responses.ErrorResponse;
import com.payments.rest.responses.Responses;
import com.payments.security.jwt.JwtToken;
import com.payments.security.jwt.JwtTokenHandler;
import com.payments.service.UserService;



@Path(AuthResource.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource  {

    private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);
    
    public final static String PATH = "/authorize";
    
    @Autowired
    private UserService userService;

 
    @Autowired
    private JwtTokenHandler tokenHandler;


    @POST
    @Path("/")
    public Response response(@Valid @NotNull final CredentialsRequest request) {
        final String username = request.getUsername();
        final String password = request.getPassword();
        
        final User user = this.userService.findByCredentials(username, password);

        LOG.debug("Try authentication with username: {} ",username);
        
        if (user == null) {
            final ErrorResponse response = ErrorResponse.with("badCredentials", "Bad  credentials");
            return Responses.unauthorized(response);
        }
        
        LOG.debug("User with username {} is authenticated",username);
        
        final JwtToken token = this.tokenHandler.create(username,30,JwtToken.AUD_AUTH);
        final AuthResponse response = new AuthResponse();
        response.setAccessToken(token.getToken());
        response.setUsername(user.getEmail());
        response.setUserId(user.getId());
        response.setType("Bearer");
        
        
        LOG.debug("Generated access token with username {} expires: {} ",username,token.getExpiration());
        
        return Responses.ok(response);
    }

}