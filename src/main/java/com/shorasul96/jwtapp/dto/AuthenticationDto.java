package com.shorasul96.jwtapp.dto;

import lombok.Data;

@Data
public class AuthenticationDto {
    private String username;
    private String password;
}
