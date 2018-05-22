package com.payments.security.jwt;

import java.util.Date;

public final class JwtToken {
    
    public static final String AUD_AUTH = "AUTH";
    
    private String token;
    
    private String subject;
    
    private String audience;
    
    private Date expiration;

    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
    
    public  boolean isExpired(){
    	Date now = new Date();
        return expiration != null && now.after(expiration);
    }
   
    
    public boolean isValid(){
        return audience != null && !isExpired();
    }
    
    public boolean isNotValid(){
        return !isValid();
    }
    
    public boolean isAuth(){
        return AUD_AUTH.equals(this.audience);
    }
}