package com.example.eventbook_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        final String jwt;
        final String email;

        //1. check authorization header
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        //2. extract jwt
        jwt = authHeader.substring(7);
        try{
            //3. extract email from jwt
            email = jwtService.extractUsername(jwt);

            //4.continue only if email exists
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

                //5.Load user
                UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);

                //6.validate the jwt
                if(jwtService.isTokenValid(jwt, (CustomerUserDetails) userDetails)){

                    //7. create authenticated token
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //8. add request details
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //9. store authentication
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }catch (Exception ignored){
            // Invalid JWT → request remains unauthenticated
        }

        filterChain.doFilter(request,response);

    }
}
