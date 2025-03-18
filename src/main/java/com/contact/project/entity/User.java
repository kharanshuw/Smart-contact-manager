package com.contact.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.contact.project.domain.Providers;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @Column(nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String email;

    
    private String password;

    private String about;

    private String profilePic;

    private String phoneNumber;

    private boolean enabled = false;

    private boolean emailVerified = false;

    private boolean phoneVerified = false;

    private Providers provider = Providers.SELF;

    private String providerUserId;

}
