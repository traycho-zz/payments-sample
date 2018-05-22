package com.payments.security.jwt;

import java.security.Principal;
import java.util.Set;

import javax.ws.rs.core.SecurityContext;

public class ApiSecurityContext implements SecurityContext{
    
    private final String AUTH_SCHEMA = "TOKEN_AUTH";
    
    private final String username;
    
    private final Set<String> roles;
    
    public ApiSecurityContext(final String username, final Set<String> roles){
        this.username = username;
        this.roles = roles;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            
            @Override
            public String getName() {
                return username;
            }
        };
    }

    @Override
    public boolean isUserInRole(final String role) {
       return this.roles.contains(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return AUTH_SCHEMA;
    }
}