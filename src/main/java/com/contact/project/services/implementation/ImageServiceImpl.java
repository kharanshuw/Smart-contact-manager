package com.contact.project.services.implementation;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.contact.project.controller.ContactController;
import com.contact.project.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;

    public Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactimagFile) {

        try {
            log.info("uploading image to cloudinary");
                byte[] data = new byte[contactimagFile.getInputStream().available()];

                contactimagFile.getInputStream().read(data);

                Map<String,String> map = new HashMap<>();

            
        } catch (Exception e) {

            e.printStackTrace();
            log.error("error occured while uploadImage");
            
        }
    }

}
