package com.joker.webchatting.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled","false");

        System.setProperty("spring.devtools.livereload.enabled","true");
        SpringApplication.run(Application.class, args);

    }
}