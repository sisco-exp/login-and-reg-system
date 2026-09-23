# User Authentication System Plan

## Goal

Create a simple Java 17 Maven Spring Boot REST API for user registration and
login. Store registered users in memory and hash passwords with BCrypt.

## Maven Setup

1. Create a Maven Spring Boot project using Java 17.
2. Add these dependencies to `pom.xml`:
   - `spring-boot-starter-web`
   - `spring-security-crypto`
   - `spring-boot-starter-test` with test scope
3. Configure the Maven compiler and Spring Boot plugin for Java 17.
4. Use `mvn spring-boot:run` to start the application and `mvn test` to run
   the tests.

## File Structure

```text
src
├── main
│   ├── java/com/myapp/auth
│   │   ├── AuthApplication.java
│   │   ├── controller/AuthController.java
│   │   ├── model/User.java
│   │   └── service/AuthService.java
│   └── resources/application.properties
└── test
    └── java/com/myapp/auth/service/AuthServiceTest.java
```

## Implementation Steps

1. Create `AuthApplication.java` with `@SpringBootApplication` and the
   standard `main` method.

2. Create `User.java` with `email` and `password` string fields, a constructor,
   and getters. Store only the BCrypt-hashed password.

3. Create `AuthService.java` as a Spring `@Service`.
   - Add `private final List<User> users = new ArrayList<>();`.
   - Create a `BCryptPasswordEncoder`.
   - Reject null or blank email and password values during registration.
   - Check the list for an existing email before adding a user.
   - Hash the password with BCrypt before storing it.
   - During login, find the user by email and call
     `passwordEncoder.matches(rawPassword, storedPassword)`.
   - Return failure when the email is unknown or the password is incorrect.

4. Create `AuthController.java` as a `@RestController`.
   - Add `POST /register` and read `email` and `password` from the JSON body.
   - Return `200 OK` with `"User registered successfully."` on success.
   - Return an appropriate error status for invalid input or duplicate email.
   - Add `POST /login`.
   - Return `200 OK` with `"Login successful"` for valid credentials.
   - Return `401 Unauthorized` for invalid credentials.

5. Keep `application.properties` minimal. Set only the application name or
   server port if needed.

6. Create `AuthServiceTest.java` using JUnit 5. Test:
   - Successful registration.
   - Password is stored hashed, not as plain text.
   - Duplicate email registration is rejected.
   - Duplicate checks behave consistently for email case.
   - Successful login with correct credentials.
   - Failed login with a wrong password.
   - Failed login with an unknown email.
   - Null and blank email or password values.
   - Failed registration does not add a user.

7. Run `mvn test` and fix any compilation or test failures.

8. Optionally start the app with `mvn spring-boot:run` and test both endpoints
   with curl or Postman.

## API Examples

### Register

`POST /register`

```json
{
  "email": "test@example.com",
  "password": "mypassword"
}
```

Success response:

```text
User registered successfully.
```

### Login

`POST /login`

```json
{
  "email": "test@example.com",
  "password": "mypassword"
}
```

Success response:

```text
Login successful
```

## Scope

Keep the implementation simple. Do not add a database, JWT, sessions, email
verification, password reset, or unnecessary validation layers.
