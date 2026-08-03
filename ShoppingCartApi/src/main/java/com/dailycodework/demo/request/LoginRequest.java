package com.dailycodework.demo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {

     @NotBlank
    private String email;
     @NotBlank
    private String password;




}
