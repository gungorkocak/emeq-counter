package com.emeq.counter.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.emeq.counter.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
