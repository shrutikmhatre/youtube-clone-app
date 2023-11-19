package com.shrutik.projects.personal.youtubeclone.controller;

import com.shrutik.projects.personal.youtubeclone.service.UserRegistrationService;
import com.shrutik.projects.personal.youtubeclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;
    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String registerUser(Authentication authentication){
        Jwt jwt = (Jwt)authentication.getPrincipal();
        return userRegistrationService.registerUser(jwt.getTokenValue());

    }

    @PostMapping("/subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser( @PathVariable("userId") String userId){
        userService.subscribeUser(userId);
        return true;
    }
    @PostMapping("/unsubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean UnSubscribeUser( @PathVariable("userId") String userId){
        userService.unSubscribeUser(userId);
        return true;
    }
    @GetMapping("/{userId}/getUserVideoHistory")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getUserVideoHistory(@PathVariable("userId") String userId){
        return userService.getUserVideoHistory(userId);

    }

}
