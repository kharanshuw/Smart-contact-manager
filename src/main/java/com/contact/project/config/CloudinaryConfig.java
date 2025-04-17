package com.contact.project.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.name}")
    private String cloudname;

    @Value("${cloudinary.apikey}")
    private String apikey;

    @Value("${cloudinary.secreat}")
    private String apisecreat;

    @Bean
    public Cloudinary cloudinary() {

        Map<String, String> map = new HashMap<>();

        map.put("cloud_name", cloudname);

        map.put("api_key", apikey);

        map.put("api_secreat", apisecreat);

        return new Cloudinary();
    }
}
