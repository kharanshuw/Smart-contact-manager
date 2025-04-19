package com.contact.project.services.implementation;


import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.contact.project.controller.ContactController;
import com.contact.project.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;

    public Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);


    public ImageServiceImpl(@Value("${cloudinary.cloud_name}") String cloudName,
                            @Value("${cloudinary.api_key}") String apiKey,
                            @Value("${cloudinary.api_secret}") String apiSecret) {

       
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(config);

    }


    public String uploadImage(MultipartFile contactimagFile, String filename) {

        try {
            log.info("uploading image to cloudinary...");

            byte[] data = new byte[contactimagFile.getInputStream().available()];

            contactimagFile.getInputStream().read(data);

            Map<String, String> map = new HashMap<>();

            map.put("public_id", filename);

            cloudinary.uploader().upload(data, map);

            return this.getUrlFromPublicId(filename);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error occured while uploadImage");
            return null;
        }
    }


    public String getUrlFromPublicId(String publicId) {

        String cloudimageurl = cloudinary.url().transformation(new Transformation<>().width(500).height(500).crop("fill")).generate(publicId);

        log.info("printing cloud image url " + cloudimageurl);

        return cloudimageurl;
    }

}
