package com.myapp.auth.service;

import com.myapp.auth.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private final List<User> users = new ArrayList<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public synchronized void register(String email, String password) {
        validateInput(email, password);

        if (users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            throw new IllegalStateException("Email is already registered.");
        }

        users.add(new User(email, passwordEncoder.encode(password)));
    }

    public synchronized boolean login(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            return false;
        }

        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    List<User> getUsers() {
        return List.copyOf(users);
    }

    private void validateInput(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            throw new IllegalArgumentException("Email and password are required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
