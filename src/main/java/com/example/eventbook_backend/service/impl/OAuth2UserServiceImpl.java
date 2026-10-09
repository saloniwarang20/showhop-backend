package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.entity.User;
import com.example.eventbook_backend.enums.AuthProvider;
import com.example.eventbook_backend.enums.Role;
import com.example.eventbook_backend.repository.UserRepository;
import com.example.eventbook_backend.service.OAuth2UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DialectOverride;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User processOAuth2User(OAuth2User oAuth2User){

        String email = oAuth2User.getAttribute("email");

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Google Account does not provide an email");
        }

        email = email.trim().toLowerCase();

        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String profileImage = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");

        User existingUser = userRepository.findByEmail(email)
                .orElse(null);

        if(existingUser != null){

            if(existingUser.getAuthProvider() == AuthProvider.LOCAL){
                throw new IllegalArgumentException("An account already exists with this email." + "Please login using email and password.");
            }
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setProfileImage(profileImage);
            existingUser.setProviderId(providerId);

            return userRepository.save(existingUser);
        }

        User newUser = User.builder()
                .firstName(firstName != null ? firstName : "User")
                .lastName(lastName)
                .email(email)
                .password(null)
                .profileImage(profileImage)
                .authProvider(AuthProvider.GOOGLE)
                .providerId(providerId)
                .role(Role.USER)
                .enabled(true)
                .build();

        return userRepository.save(newUser);

    }
}
