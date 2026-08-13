package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.CartService;
import com.ameerrasik.ameerrasikmart.service.CartServiceImpl;
import com.ameerrasik.ameerrasikmart.service.OrderService;
import com.ameerrasik.ameerrasikmart.service.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for checkout flow and mock payment confirmation.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<CartItemDTO> cartItems = cartService.getCartItems(user.getId());
        if (cartItems.isEmpty()) {
            req.setAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        BigDecimal cartTotal = cartService.getCartTotal(user.getId());

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", cartTotal);
        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String paymentMethod = req.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "Mock Card";
        }

        try {
            Order order = orderService.processCheckout(user.getId(), paymentMethod);
            logger.info("Checkout successful for user: {}. Order ID: {}", user.getEmail(), order.getId());

            req.getSession().setAttribute("latestOrder", order);
            req.getSession().setAttribute("paymentMethod", paymentMethod);
            resp.sendRedirect(req.getContextPath() + "/order-success.jsp");

        } catch (ValidationException e) {
            logger.warn("Checkout validation error for user {}: {}", user.getId(), e.getMessage());
            req.setAttribute("errorMessage", e.getMessage());

            List<CartItemDTO> cartItems = cartService.getCartItems(user.getId());
            BigDecimal cartTotal = cartService.getCartTotal(user.getId());
            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartTotal", cartTotal);

            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        } catch (Exception e) {
            logger.error("Unexpected checkout failure for user " + user.getId(), e);
            req.setAttribute("errorMessage", "An error occurred during checkout transaction. Please try again.");
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        }
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }
}
