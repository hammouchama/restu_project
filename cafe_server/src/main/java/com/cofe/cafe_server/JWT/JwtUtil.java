package com.cofe.cafe_server.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service

public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;


    public String extractUsername(String token){
        return extractClamis(token,Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClamis(token ,Claims::getExpiration);
    }
    public <T> T extractClamis(String token , Function<Claims,T> claimsResolv){
        final Claims claims=extractAllClaims(token);
        return claimsResolv.apply(claims);
    }
    public String genreateToken(String username,String role){
        Map<String ,Object> cliams=new HashMap<>();
        cliams.put("role",role);
       return createToken(cliams,username);
    }
    private String createToken(Map<String ,Object> claims,String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +100 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }
  /*  public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }*/
  public Claims extractAllClaims(String token) {
      return Jwts.parser()
              .setSigningKey(secret)
              .parseClaimsJws(token)  // Use parseClaimsJws instead of parseClaimsJwt
              .getBody();
  }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());

    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
