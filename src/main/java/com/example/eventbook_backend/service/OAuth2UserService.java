package com.example.eventbook_backend.service;

import com.example.eventbook_backend.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserService {

    User processOAuth2User(OAuth2User oAuth2User);
}
