package com.contact.project.entity;

import com.contact.project.domain.Providers;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NotBlank(message = "userName must not be empty or null")
    @Size(min = 3, max = 50, message = "userName must be between 3 and 50 characters long")
    private String userName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    @Pattern(regexp = "^[a-zA-Z0-9_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Size(max = 500, message = "About must not exceed 255 characters")
    @NotBlank(message = "About is required")
    private String about;

    private String profilePic;

    // pattern ^\\d{10}$ matches exactly 10 digits (for a phone number).
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String phoneNumber;

    private boolean enabled = true;


    private boolean emailVerified = false;


    private boolean phoneVerified = false;

    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;

    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Providers getProvider() {
        return provider;
    }

    public void setProvider(Providers provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    /**
     * this method is used to add new contact in existing list of contact for this user
     *
     * @param contact
     * @return
     */
    public User addContact(Contact contact) {
        contacts.add(contact);
        contact.setUser(this);
        return this;
    }


    /**
     * this method is used to remove existing contact from list of contact for this user
     *
     * @param contact
     * @return
     */
    public User removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setUser(this);
        return this;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", email='" + email + '\''
                + ", password='" + password + '\'' + ", about='" + about + '\'' + ", profilePic='"
                + profilePic + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", enabled="
                + enabled + ", emailVerified=" + emailVerified + ", phoneVerified=" + phoneVerified
                + ", provider=" + provider + ", providerUserId='" + providerUserId + '\''
                + ", roleList=" + roleList + '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list of roles[USER,ADMIN]
        // Collection of SimpGrantedAuthority[roles{ADMIN,USER}]

        Collection<SimpleGrantedAuthority> roles = roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;

    }
}
