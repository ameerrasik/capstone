package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Role;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.UserService;
import com.ameerrasik.ameerrasikmart.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller handling new account registrations (BUYER / SELLER).
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String roleStr = req.getParameter("role");

        Role role = Role.fromString(roleStr);

        try {
            User registered = userService.registerUser(name, email, password, confirmPassword, role);
            logger.info("Successfully registered user: {}", registered.getEmail());

            req.setAttribute("successMessage", "Registration successful! Please log in with your credentials.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);

        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("role", roleStr);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } catch (Exception e) {
            logger.error("Unexpected error during user registration", e);
            req.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
