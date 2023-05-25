package com.istad.friendlyjwt.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String homepage(){
        return   " Hello ! This is home page ! ";
    }
}
