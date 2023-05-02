package com.example.shppyad23.config;

import com.example.shppyad23.User.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.SessionRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class CookieAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        var token = httpServletRequest.getSession(false);
        if (token != null) {
            var session = sessionRepository.findById(token.getId());
            if (session != null) {
                String authenticated_user = session.getAttribute("authenticated_user");
                if (authenticated_user != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(authenticated_user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("auth");
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}