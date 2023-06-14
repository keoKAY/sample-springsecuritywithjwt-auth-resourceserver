package com.istad.friendlyjwt.security;

import com.istad.friendlyjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var authenticatedUser   = userRepository.loadUserByUsername(username);

        if(authenticatedUser!=null){
            System.out.println("*********************************************");
            System.out.println("User from the database is : "+authenticatedUser);
            System.out.println("*********************************************");

            UserDetailImpl user= new UserDetailImpl(authenticatedUser);
            user.setId(authenticatedUser.getId());
            user.setUsername(authenticatedUser.getUsername());
            user.setPassword(authenticatedUser.getPassword());
            return user;
        }
        return null;
    }
}
