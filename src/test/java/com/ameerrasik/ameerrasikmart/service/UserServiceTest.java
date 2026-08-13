package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.UserDAO;
import com.ameerrasik.ameerrasikmart.exception.AuthenticationException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Role;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = Mockito.mock(UserDAO.class);
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    void testRegisterUserSuccess() {
        when(userDAO.findByEmail("newbuyer@test.com")).thenReturn(Optional.empty());
        when(userDAO.createUser(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(10L);
            return u;
        });

        User user = userService.registerUser("New Buyer", "newbuyer@test.com", "Password@123", "Password@123", Role.BUYER);
        assertNotNull(user);
        assertEquals(10L, user.getId());
        assertEquals("newbuyer@test.com", user.getEmail());
    }

    @Test
    void testRegisterUserPasswordMismatch() {
        assertThrows(ValidationException.class, () -> {
            userService.registerUser("Test", "test@test.com", "pass123", "different", Role.BUYER);
        });
    }

    @Test
    void testLoginSuccess() {
        String hash = PasswordUtil.hashPassword("Password@123");
        User user = new User(1L, "Test", "buyer@test.com", hash, Role.BUYER, null);
        when(userDAO.findByEmail("buyer@test.com")).thenReturn(Optional.of(user));

        User loggedIn = userService.loginUser("buyer@test.com", "Password@123");
        assertNotNull(loggedIn);
        assertEquals("buyer@test.com", loggedIn.getEmail());
    }

    @Test
    void testLoginInvalidPassword() {
        String hash = PasswordUtil.hashPassword("Password@123");
        User user = new User(1L, "Test", "buyer@test.com", hash, Role.BUYER, null);
        when(userDAO.findByEmail("buyer@test.com")).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class, () -> {
            userService.loginUser("buyer@test.com", "WrongPassword");
        });
    }
}
