package com.istad.friendlyjwt.controller;


import com.istad.friendlyjwt.security.UserDetailImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @GetMapping("/home")
    public String homepage(Authentication authentication){
        var user = authentication.getPrincipal();
       logger.info("User is : {}",authentication.getPrincipal());
       logger.info("User is : {}",authentication.getCredentials());
       logger.info("User is : {}",authentication.getDetails());
       logger.info("User is : {}",authentication.getAuthorities());
        return   " Hello  "+authentication.getName();
    }


    @GetMapping("/blockadmin")
    public String roleBasedMethod(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            System.out.println("Role of user : "+authentication.getAuthorities());

           List<String> loginRoles =  authentication.getAuthorities()
                    .stream().map(Object::toString)
                    .toList();
            System.out.println("Login Roles :"+loginRoles);
         List<String> adminRole =   loginRoles.stream().filter(
                   e->e.equalsIgnoreCase("scope_admin")
           ).toList();

         if(adminRole.size()==0){
             return "Not an admin, cannot do this action! ";
         }else {
             return "Successfully block another admin ";
         }
        }
        return null;
    }

    @GetMapping("/testing")
    public  String testing(){

        System.out.println("hello Testing !");
        return "Hello Testing!!";
    }


    @GetMapping("/welcome")
    public String welcomePage(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailImpl user = (UserDetailImpl) authentication.getPrincipal();
        System.out.println("Information of logined user : "+user);
        List<String> userRoles = user.getAuthorities().stream()
                .map(Object::toString).toList();

        System.out.println("User Roles is : "+userRoles);
        if(userRoles.contains("ADMIN")){
            return "Welcome to the Admin Dashboard !! ";
        }else {
            return "Welcome to the user Feed !! ";

        }


    }
}
