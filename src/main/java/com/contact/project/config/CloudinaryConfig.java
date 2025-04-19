package com.contact.project.config;

import java.util.HashMap;
import java.util.Map;

import com.contact.project.controller.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    public Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Value("${cloudinary.cloud_name}")
    private String cloudname;

    @Value("${cloudinary.api_key}")
    private String apikey;

    @Value("${cloudinary.api_secret}")
    private String apisecreat;

    @Bean
    public Cloudinary cloudinary() {

        logger.info("printing cloud name " + cloudname);

        Map<String, String> map = new HashMap<>();

        map.put("cloud_name", cloudname);

        map.put("api_key", apikey);

        map.put("api_secreat", apisecreat);

        logger.info("submiting cloud name cloud api key and secreat");

        return new Cloudinary(map);
    }
}
