package com.istad.friendlyjwt.controller;

import com.istad.friendlyjwt.model.TokenDto;
import com.istad.friendlyjwt.model.request.LoginRequest;
import com.istad.friendlyjwt.security.utils.TokenGenerator;
import com.istad.friendlyjwt.service.TokenService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    /*private final TokenService tokenService;*/
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

   /* AuthenticationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
*/
  /*  @PostMapping("/token")
    public String getToken(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return token;
    }
*/
    /*@PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword()));

            String token = tokenService.generateToken(authentication);
            return token;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error Hx ";
        }

    }*/

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request){
        try{
            Authentication authentication = daoAuthenticationProvider.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.getUsername(), request.getPassword()
                    )
            );
            TokenDto response = tokenGenerator.tokenResponse(authentication);
            return ResponseEntity.ok(response);
        }catch (Exception ex ){
            ex.printStackTrace();
            throw new BadCredentialsException("Username and password is incorrect!! ");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity refreshToken(@RequestBody TokenDto request){

        Authentication authentication = jwtAuthenticationProvider.authenticate(
                new BearerTokenAuthenticationToken(request.getRefreshToken())
        );

        return ResponseEntity.ok(tokenGenerator.tokenResponse(authentication));

    }



}
