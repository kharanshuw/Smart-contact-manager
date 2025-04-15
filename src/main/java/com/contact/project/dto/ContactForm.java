package com.contact.project.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String description;

    private boolean fevorite = false;

    private String facebookLink;

    private String instagramLink;

    private MultipartFile profileImage;

}
