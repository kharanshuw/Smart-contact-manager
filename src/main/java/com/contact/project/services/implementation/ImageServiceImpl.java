package com.contact.project.services.implementation;


import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
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


    /**
     * Uploads an image file to Cloudinary and returns the URL of the uploaded image.
     * 
     * This method converts the provided image file into a byte array, uploads it to Cloudinary
     * using the specified filename as the public ID, and generates the URL for the uploaded image
     * using the public ID. If an exception occurs during the upload, it logs the error and returns
     * `null`.
     * 
     * @param contactImageFile The image file to be uploaded (must be a `MultipartFile`).
     * @param filename The unique filename to be used as the public ID for the image.
     * @return The URL of the uploaded image, or `null` if the upload fails.
     */
    public String uploadImage(MultipartFile contactimagFile, String filename) {

        try {
            log.info("uploading image to cloudinary...");

            // Convert the file into a byte array
            log.debug("Converting file to byte array...");

            byte[] data = new byte[contactimagFile.getInputStream().available()];

            log.debug("Byte array created successfully for file: {}",
                    contactimagFile.getOriginalFilename());


            contactimagFile.getInputStream().read(data);

            // Define Cloudinary upload options


            Map<String, String> map = new HashMap<>();

            map.put("public_id", filename);
            log.debug("Upload options configured: {}", map.toString());

            // Upload image to Cloudinary
            log.info("Uploading image to Cloudinary...");

            cloudinary.uploader().upload(data, map);

            log.info("Image uploaded successfully with public ID: {}", filename);


            // Generate and return the image URL

            return this.getUrlFromPublicId(filename);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error occured while uploadImage");
            throw new RuntimeException("error occured while uploadImage " + e.getMessage());
        }
    }



    /**
     * Generates a URL for an image on Cloudinary using its public ID.
     * 
     * This method uses Cloudinary's `Transformation` class to create a URL for the image with
     * specific transformations applied (such as resizing to 500x500 pixels and cropping to fit). It
     * logs the generated URL for debugging purposes.
     * 
     * @param publicId The public ID of the image on Cloudinary.
     * @return The URL of the image with applied transformations.
     */
    public String getUrlFromPublicId(String publicId) {

        log.info("Generating Cloudinary image URL for public ID: {}", publicId);


        String cloudimageurl = cloudinary.url()
                .transformation(new Transformation<>().width(500).height(500).crop("fill"))
                .generate(publicId);

        log.info("Generated Cloudinary image URL: {}", cloudimageurl);


        return cloudimageurl;
    }

}
