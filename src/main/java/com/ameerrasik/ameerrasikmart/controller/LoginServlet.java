package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.exception.AuthenticationException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller handling user authentication and session management.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            redirectUserByRole(resp, req.getContextPath(), user);
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.loginUser(email, password);

            // Security: Regenerate session ID upon successful authentication
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession newSession = req.getSession(true);
            newSession.setMaxInactiveInterval(30 * 60); // 30 minutes explicit timeout
            newSession.setAttribute("user", user);

            logger.info("Session created for user: {} with Role: {}", user.getEmail(), user.getRole());
            redirectUserByRole(resp, req.getContextPath(), user);

        } catch (AuthenticationException | ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (Exception e) {
            logger.error("Unexpected error during login", e);
            req.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void redirectUserByRole(HttpServletResponse resp, String contextPath, User user) throws IOException {
        if (user.isAdmin()) {
            resp.sendRedirect(contextPath + "/admin/dashboard");
        } else if (user.isSeller()) {
            resp.sendRedirect(contextPath + "/seller/dashboard");
        } else {
            resp.sendRedirect(contextPath + "/products");
        }
    }
}
