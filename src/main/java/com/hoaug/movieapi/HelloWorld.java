package com.hoaug.movieapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @GetMapping("api/v1/ping")
    public String Ping() {
        return "Pong pong pong";
    }
}
