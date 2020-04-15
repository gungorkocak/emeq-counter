package com.emeq.counter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity(name = "users")
@Table(
    name = "users",
    indexes = { @Index(name="user_email_index", columnList = "email", unique = true) }
)
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String encryptedPassword;
}
