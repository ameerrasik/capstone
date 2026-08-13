package com.ameerrasik.ameerrasikmart.filter;

import com.ameerrasik.ameerrasikmart.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication and Role-based Authorization filter for AmeerRasik Mart.
 */
@WebFilter(urlPatterns = {"/admin/*", "/seller/*", "/buyer/*", "/cart", "/checkout", "/orders", "/order-details"})
public class AuthFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = requestURI.substring(contextPath.length());

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            logger.warn("Unauthorized access attempt to protected URI: {}", path);
            req.setAttribute("errorMessage", "Please log in to access this page.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // Role-based Access Control
        if (path.startsWith("/admin") && !user.isAdmin()) {
            logger.warn("User {} (Role: {}) attempted to access Admin path {}", user.getEmail(), user.getRole(), path);
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin privileges required.");
            return;
        }

        if (path.startsWith("/seller") && (!user.isSeller() && !user.isAdmin())) {
            logger.warn("User {} (Role: {}) attempted to access Seller path {}", user.getEmail(), user.getRole(), path);
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Seller account required.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("AuthFilter destroyed.");
    }
}
