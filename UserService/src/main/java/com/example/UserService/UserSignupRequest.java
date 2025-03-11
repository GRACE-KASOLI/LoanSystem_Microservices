package com.example.UserService;

import com.example.UserService.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private Role role; // Accepts either ADMIN or MEMBER
}
