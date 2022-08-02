package com.example.banan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {




    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///C:/Users/User/Desktop/ImagesForBanan/");
        registry.addResourceHandler("/mp3/**")
                .addResourceLocations("file:///C:/Users/User/Desktop/MusicsForBanan/");
        registry.addResourceHandler("/styles/**")
                .addResourceLocations("classpath:/static/");
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/chat").setViewName("chat");
    }


}