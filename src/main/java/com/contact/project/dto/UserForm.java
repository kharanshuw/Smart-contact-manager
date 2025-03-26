package com.contact.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @Pattern(regexp = "^[a-zA-Z0-9_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    /**
     * ^ matches the start of the string
     * [a-zA-Z0-9._%+-]+ matches one or more of the following characters:
     * a-z (lowercase letters)
     * A-Z (uppercase letters)
     * 0-9 (digits)
     * _ (underscore)
     * @ matches the @ symbol
     * [a-zA-Z0-9.-]+ matches one or more of the following characters:
     * a-z (lowercase letters)
     * A-Z (uppercase letters)
     * 0-9 (digits)
     * - (hyphen)
     * . (dot)
     * \. matches the dot (.) character (it needs to be escaped with a backslash
     * because . has a special meaning in regex)
     * [a-zA-Z]{2,} matches the domain extension (it must be at least 2 characters
     * long, and can only contain letters)
     * $ matches the end of the string
     */
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    /**
     * At least 8 characters
     * At least 1 uppercase letter
     * At least 1 lowercase letter
     * At least 1 digit
     * At least 1 special character (such as !, @, #, etc.)
     * No whitespace characters
     */
    private String password;

    @Size(max = 500, message = "About must not exceed 255 characters")
    @NotBlank(message = "About is required")
    private String about;

    // pattern ^\\d{10}$ matches exactly 10 digits (for a phone number).
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String phoneNumber;

}
