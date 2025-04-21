package com.contact.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String picture;

    private String description;

    private boolean fevorite = false;

    private String facebookLink;

    private String instagramLink;

    private String cloudinaryImagename;

    /**
     * The user who owns this contact.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * List of social links associated with this contact.
     */
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SocialLink> links = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFevorite() {
        return fevorite;
    }

    public void setFevorite(boolean fevorite) {
        this.fevorite = fevorite;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SocialLink> getLinks() {
        return links;
    }

    public void setLinks(List<SocialLink> links) {
        this.links = links;
    }


    public String getCloudinaryImagename() {
        return cloudinaryImagename;
    }

    public void setCloudinaryImagename(String cloudinaryImagename) {
        this.cloudinaryImagename = cloudinaryImagename;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", fevorite=" + fevorite +
                ", facebookLink='" + facebookLink + '\'' +
                ", instagramLink='" + instagramLink + '\'' +
                ", cloudinaryImagename='" + cloudinaryImagename + '\'' +
                ", user=" + user +
                ", links=" + links +
                '}';
    }
}
