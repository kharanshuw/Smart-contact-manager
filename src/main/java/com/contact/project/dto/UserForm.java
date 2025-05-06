package com.contact.project.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "userName must not be empty or null")
    @Size(min = 3, max = 50, message = "userName must be between 3 and 50 characters long")
    private String userName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    @Pattern(regexp = "^[a-zA-Z0-9_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email address must follow standard pattern")


    /**
     * ^ matches the start of the string [a-zA-Z0-9._%+-]+ matches one or more of the following
     * characters: a-z (lowercase letters) A-Z (uppercase letters) 0-9 (digits) _ (underscore) @
     * matches the @ symbol [a-zA-Z0-9.-]+ matches one or more of the following characters: a-z
     * (lowercase letters) A-Z (uppercase letters) 0-9 (digits) - (hyphen) . (dot) \. matches the
     * dot (.) character (it needs to be escaped with a backslash because . has a special meaning in
     * regex) [a-zA-Z]{2,} matches the domain extension (it must be at least 2 characters long, and
     * can only contain letters) $ matches the end of the string
     */
    private String email;


    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Invalid password must follow standard pattern")
    /**
     * At least 8 characters At least 1 uppercase letter At least 1 lowercase letter At least 1
     * digit At least 1 special character (such as !, @, #, etc.) No whitespace characters
     */
    private String password;

    @Size(max = 500, message = "About must not exceed 255 characters")
    @NotBlank(message = "About is required")
    private String about;

    // pattern ^\\d{10}$ matches exactly 10 digits (for a phone number).
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number must be of 10 digit strictly")
    private String phoneNumber;


    public String getUserName() {
        return userName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
