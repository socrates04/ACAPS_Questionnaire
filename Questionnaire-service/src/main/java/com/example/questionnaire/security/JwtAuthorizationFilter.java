package com.example.questionnaire.security;

import com.example.questionnaire.feignRelated.JwtUtil;
import com.example.questionnaire.model.AppUser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,@NotNull HttpServletResponse response,@NotNull FilterChain chain
    ) throws ServletException, IOException {
        System.out.println("Authorization filtering...");

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Header: "+authorizationHeader);
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            System.out.println("Got Token: "+ jwt);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                try{
                    AppUser user = jwtUtil.extractUserInfo(jwt);
                    System.out.println("Updating security context with userInfo: "+ user.toString());
                    // updating the security context
                    if (!jwtUtil.isTokenExpired(jwt)) {
                        System.out.println("Creating AuthenticationToken ... ");
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        System.out.println("Context updated .");
                    }
                }catch (Exception ex){
                    System.out.println("Error trying to extract Principal info.");
                    ex.printStackTrace();
                }
            }
        }else{
            System.out.println("No Authorization header found.");
        }
        System.out.println("Passing request to next filter ...");
        chain.doFilter(request,response);
    }
}
