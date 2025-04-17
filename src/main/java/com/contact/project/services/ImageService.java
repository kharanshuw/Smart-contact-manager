package com.contact.project.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    public String uploadImage(MultipartFile contactimagFile);

}
