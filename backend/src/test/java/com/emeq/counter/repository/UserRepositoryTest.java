package com.emeq.counter.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionSystemException;

import com.emeq.counter.model.User;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void setup(@Autowired UserRepository repository) {
        final User user = new User();
        user.setEmail("admin@emeq.com");
        user.setEncryptedPassword("very-strong-password");
        repository.save(user);
    }

    @AfterAll
    public static void tear(@Autowired UserRepository repository) {
        repository.deleteAll();
    }

    @Test
    public void userShouldFindByEmail() {
        String email = "admin@emeq.com";
        Optional<User> user = userRepository.findByEmail(email);

        assertTrue(user.isPresent());
        assertThat(user.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void userShouldNotFindByWrongEmail() {
        String email = "wrongAdmin@emeq.com";
        Optional<User> user = userRepository.findByEmail(email);

        assertFalse(user.isPresent());
    }

    @Test
    public void emailShouldBeUniq() {
        final User user = new User();
        user.setEmail("admin@emeq.com");
        user.setEncryptedPassword("bad-password");

        assertThrows(NonTransientDataAccessException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    public void emailShouldBeCorrectFormat() {
        final User user = new User();
        user.setEmail("its_not_email.com");
        user.setEncryptedPassword("bad-password");

        assertThrows(TransactionSystemException.class, () -> {
            userRepository.save(user);
        });
    }
}
