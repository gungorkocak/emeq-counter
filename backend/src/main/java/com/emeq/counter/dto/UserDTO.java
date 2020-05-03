package com.emeq.counter.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserDTO(){ }

    public UserDTO(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    @Email
    private String email;

    @NotBlank
    private String password;
}