package com.contact.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {
    private String userName;
    private String email;
    private String password;
    private String about;
    private String phoneNumber;

    
    @Override
    public String toString() {
        return "UserForm [userName=" + userName + ", email=" + email + ", password=" + password + ", about=" + about
                + ", phoneNumber=" + phoneNumber + "]";
    }


    
}
