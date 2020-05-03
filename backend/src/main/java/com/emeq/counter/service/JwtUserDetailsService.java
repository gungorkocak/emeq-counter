package com.emeq.counter.service;

import com.emeq.counter.dto.UserDTO;
import com.emeq.counter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> create(final UserDTO userDTO) {
        final com.emeq.counter.model.User user = new com.emeq.counter.model.User();
        user.setEmail(userDTO.getEmail());
        user.setEncryptedPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}