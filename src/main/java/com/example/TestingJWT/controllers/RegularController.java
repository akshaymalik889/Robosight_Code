package com.example.TestingJWT.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regular")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
//@Secured("ROLE_REGULAR")  // so only user having role REGULAR Can access these Routes
public class RegularController {



}
