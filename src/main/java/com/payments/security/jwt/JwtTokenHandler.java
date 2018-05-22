package com.payments.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.payments.time.DateTime;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

@Component(JwtTokenHandler.BEAN_NAME)
public final class JwtTokenHandler {
	
	public static final String BEAN_NAME = "JwtTokenHandler";
	
    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenHandler.class);
    
    private static final String ISSUER = "Payments";

    
    @Value("${jwt.secret}")	
    private  String secret;
    
    public JwtToken parse(final String jwt){
        if(jwt == null){
            return null;
        }
        
        final JwtParser parser = Jwts.parser().setSigningKey(secret);
        Claims claims= null;
        try{
            claims = parser.parseClaimsJws(jwt).getBody();
        }catch(ExpiredJwtException expired){
            claims = expired.getClaims();
        }catch (MalformedJwtException malformed){
            LOG.warn("Malformed token: {}", jwt);
            return null;
        }
        
        final JwtToken token = new JwtToken();
        token.setExpiration(claims.getExpiration());
        token.setSubject(claims.getSubject());
        token.setAudience(claims.getAudience());
        token.setToken(jwt);
        
        return token;
    }

    
    public JwtToken create(final String subject,int expiresInMinutes,final String audience){
        final Date now = new Date();
        final Date expiration = DateTime.afterMinutes(expiresInMinutes);
        final  String jwtToken = Jwts.builder() //
                .setSubject(subject) //
                .setAudience(audience)
                .setIssuedAt(now) //
                .setIssuer(ISSUER) //
                .setExpiration(expiration) //
                .signWith(SignatureAlgorithm.HS512, secret)//
                .compact();
        
  
 
        final  JwtToken  token =  new JwtToken();
        token.setExpiration(expiration);
        token.setSubject(subject);
        token.setAudience(audience);
        token.setToken(jwtToken);
        return token;
    }
}