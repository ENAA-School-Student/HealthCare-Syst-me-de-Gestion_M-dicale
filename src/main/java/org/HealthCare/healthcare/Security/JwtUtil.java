package org.HealthCare.healthcare.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpiration;


    public String genereteToken(String email, String role){
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256 , jwtSecret)
                .compact();
    }

    public String getUserFromToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            log.error("JWT validation error: {}" , e.getMessage());
        }
        return false;
    }
}
