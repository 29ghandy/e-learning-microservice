package org.example.userservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/GADALLAH")
public class gadallah {
    @GetMapping("/")
    public String gadallah() {
        return "GADALLAH mglden neek";
    }
}
