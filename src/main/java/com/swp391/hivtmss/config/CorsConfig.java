package com.swp391.hivtmss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(
                        "http://localhost:3000/", "https://swp-391-hivtmss.vercel.app/", "https://swp391.tinhvv.xyz"
                )
                .allowedHeaders("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}
