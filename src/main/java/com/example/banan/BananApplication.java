package com.example.banan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@SpringBootApplication
public class BananApplication {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SpringApplication.run(BananApplication.class, args);
    }

}
