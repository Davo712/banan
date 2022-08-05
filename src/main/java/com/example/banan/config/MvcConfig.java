package com.example.banan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webjars/**",
                "/img/**",
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/static/js/");
//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("file:///C:/Users/User/Desktop/ImagesForBanan/");
        registry.addResourceHandler("/mp3/**")
                .addResourceLocations("file:///C:/Users/User/Desktop/MusicsForBanan/");
        registry.addResourceHandler("/styles/**")
                .addResourceLocations("classpath:/static/");
    }


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("file:///C:/Users/User/Desktop/ImagesForBanan/");
//        registry.addResourceHandler("/mp3/**")
//                .addResourceLocations("file:///C:/Users/User/Desktop/MusicsForBanan/");
//        registry.addResourceHandler("/styles/**")
//                .addResourceLocations("classpath:/static/");
//    }

    public void addViewControllers(ViewControllerRegistry registry) {
    }


}