package com.istad.friendlyjwt.controller;

import com.istad.friendlyjwt.model.request.LoginRequest;
import com.istad.friendlyjwt.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class AuthenticationController {

    private final TokenService tokenService;

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    AuthenticationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public String getToken(Authentication authentication) {

        String token = tokenService.generateToken(authentication);
        return token;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword()));

            String token = tokenService.generateToken(authentication);
            return token;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error Hx ";
        }

    }
}
