package com.emeq.counter.controller;

import javax.validation.Valid;

import com.emeq.counter.dto.UserDTO;
import com.emeq.counter.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class JwtAuthenticationController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<Object> signUp(@Valid @RequestBody UserDTO request) {
        return userDetailsService.create(request);
    }
}