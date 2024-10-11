package com.example.questionnaire.feignRelated;

import com.example.questionnaire.model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "amF2YXguY3J5cHRvLnNwZWMuU2VjcmV0S2V5U3BlY0A1ODgzMWZh";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    public String extractUsername(String token) throws SignatureException {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parserBuilder().setSigningKey(SECRET_KEY) .build().parse(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public AppUser extractUserInfo(String token) {
        Claims claims = extractAllClaims(token);

        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        boolean enabled = claims.get("enabled", Boolean.class);
        Date creationDate = claims.get("creationDate", Date.class);
        List<String> authoritiesList = (List<String>) claims.get("authorities");
        Collection<SimpleGrantedAuthority> authorities = authoritiesList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AppUser(userId, username, email, authorities, enabled, creationDate);
    }
}
