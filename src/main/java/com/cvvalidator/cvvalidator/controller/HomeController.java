package com.cvvalidator.cvvalidator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class HomeController {
    @GetMapping("")
    public ResponseEntity helloMessage()
    {
        return ResponseEntity.ok("Welcome to cv-validator API.");
    }
}
