package com.swp391.hivtmss.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    private String CLOUD_NAME;
    @Value("${cloudinary.api_key}")
    private String API_KEY;
    @Value("${cloudinary.api_secret}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(getCloudinaryConfig());
    }

    @Bean
    public HashMap<String, String> getCloudinaryConfig() {
        HashMap<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return config;
    }
}
