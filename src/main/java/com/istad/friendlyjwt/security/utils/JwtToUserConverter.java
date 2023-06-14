package com.istad.friendlyjwt.security.utils;

import com.istad.friendlyjwt.model.User;
import com.istad.friendlyjwt.repository.UserRepository;
import com.istad.friendlyjwt.security.UserDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    // inject repo to get user information
    @Autowired
    UserRepository userRepository;
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        User user = userRepository.loadUserByUsername(source.getSubject());
        if(user!=null){
            UserDetailImpl userDetail = new UserDetailImpl(user);
            userDetail.setId(user.getId());
            userDetail.setUsername(user.getUsername());
            userDetail.setPassword(user.getPassword());

            return new UsernamePasswordAuthenticationToken(userDetail,source,userDetail.getAuthorities());
        }else{

            throw  new BadCredentialsException(" Invalid Token !!!");
        }

    }
}
