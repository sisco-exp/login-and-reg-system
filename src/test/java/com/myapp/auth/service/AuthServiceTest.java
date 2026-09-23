package com.myapp.auth.service;

import com.myapp.auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void registersUserAndHashesPassword() {
        authService.register("test@example.com", "mypassword");

        User user = authService.getUsers().get(0);
        assertEquals("test@example.com", user.getEmail());
        assertNotEquals("mypassword", user.getPassword());
        assertTrue(new BCryptPasswordEncoder().matches("mypassword", user.getPassword()));
    }

    @Test
    void rejectsDuplicateEmailIgnoringCase() {
        authService.register("test@example.com", "mypassword");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> authService.register("TEST@example.com", "anotherpassword"));

        assertEquals("Email is already registered.", exception.getMessage());
        assertEquals(1, authService.getUsers().size());
    }

    @Test
    void logsInWithCorrectCredentials() {
        authService.register("test@example.com", "mypassword");

        assertTrue(authService.login("test@example.com", "mypassword"));
    }

    @Test
    void rejectsWrongPassword() {
        authService.register("test@example.com", "mypassword");

        assertFalse(authService.login("test@example.com", "wrongpassword"));
    }

    @Test
    void rejectsUnknownEmail() {
        assertFalse(authService.login("unknown@example.com", "mypassword"));
    }

    @Test
    void rejectsNullAndBlankRegistrationValuesWithoutAddingUser() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(null, "mypassword"));
        assertThrows(IllegalArgumentException.class,
                () -> authService.register("test@example.com", " "));

        assertEquals(0, authService.getUsers().size());
    }

    @Test
    void rejectsNullAndBlankLoginValues() {
        assertFalse(authService.login(null, "mypassword"));
        assertFalse(authService.login("test@example.com", null));
        assertFalse(authService.login(" ", "mypassword"));
        assertFalse(authService.login("test@example.com", ""));
    }
}
