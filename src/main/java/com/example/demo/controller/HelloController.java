package com.example.demo.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloController {

  @GetMapping("/hello")
  public String home() {
    return "Hello Docker World version 2.0";
  }

}