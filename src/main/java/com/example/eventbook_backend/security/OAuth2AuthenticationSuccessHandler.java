package com.example.eventbook_backend.security;

import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.service.OAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2UserService oAuth2UserService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        User user = oAuth2UserService.processOAuth2User(oAuth2User);

        CustomerUserDetails userDetails = new CustomerUserDetails(user);

        String token = jwtService.generateToken(userDetails);

        String redirectUrl = "http://localhost:5173/oauth2/success?token="+token;

        response.sendRedirect(redirectUrl);

    }
}
