package com.netflix.clone.netflix_clone_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String BEARER = "Bearer ";
    private final String AUTHORIZATION = "Authorization";
    private final String TOKEN = "token";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractJwtToken(request);
        String username = null;
        if(token != null) {
            username = jwtUtil.getUsernameFromToken(token);
        }

        if(shouldProcessAuthentication(username)) {
            processAuthentication(request, token, username);
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        final String requestURI = request.getRequestURI();

        if(authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            return authorizationHeader.substring(BEARER.length());
        } else if((requestURI.contains("/api/files/video/") || requestURI.contains("api/files/image/"))
        && request.getParameter(TOKEN) != null) {
            return request.getParameter(TOKEN);
        } else {
            return null;
        }
    }

    private boolean shouldProcessAuthentication(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;  //“Has any Authentication object already been set for this request/thread?”
    }

    private void processAuthentication(HttpServletRequest request, String token, String username) {
        if(jwtUtil.validateToken(token)) {
            UserDetails userDetails = createUserDetailsFromToken(token, username);
            setAuthenticationInContext(request, userDetails);
        }
    }

    private UserDetails createUserDetailsFromToken(String token, String username) {
        String role = jwtUtil.getRoleFromToken(token);
        return User.builder()
                .username(username)
                .password("")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role)))
                .build();
    }

    private void setAuthenticationInContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
