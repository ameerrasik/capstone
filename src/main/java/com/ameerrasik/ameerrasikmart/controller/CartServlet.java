package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.dto.ApiResponse;
import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.CartService;
import com.ameerrasik.ameerrasikmart.service.CartServiceImpl;
import com.google.gson.Gson;
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
 * Controller for managing shopping cart operations (add, update, remove, clear, view).
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);
    private CartService cartService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartServiceImpl();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<CartItemDTO> cartItems = cartService.getCartItems(user.getId());
        BigDecimal cartTotal = cartService.getCartTotal(user.getId());

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", cartTotal);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            sendJsonOrRedirect(req, resp, false, "Please log in to manage your cart.", "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "add";
        }

        try {
            switch (action.toLowerCase()) {
                case "add":
                    handleAdd(req, resp, user);
                    break;
                case "update":
                    handleUpdate(req, resp, user);
                    break;
                case "remove":
                    handleRemove(req, resp, user);
                    break;
                case "clear":
                    cartService.clearCart(user.getId());
                    sendJsonOrRedirect(req, resp, true, "Cart cleared.", "/cart");
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/cart");
            }
        } catch (ValidationException e) {
            logger.warn("Cart operation failed for user {}: {}", user.getId(), e.getMessage());
            sendJsonOrRedirect(req, resp, false, e.getMessage(), "/cart");
        } catch (Exception e) {
            logger.error("Unexpected error during cart operation", e);
            sendJsonOrRedirect(req, resp, false, "An unexpected error occurred.", "/cart");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity") != null ? req.getParameter("quantity") : "1");

        cartService.addToCart(user.getId(), productId, quantity);
        sendJsonOrRedirect(req, resp, true, "Item added to cart successfully!", "/cart");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        cartService.updateQuantity(user.getId(), productId, quantity);
        sendJsonOrRedirect(req, resp, true, "Cart updated successfully.", "/cart");
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        cartService.removeFromCart(user.getId(), productId);
        sendJsonOrRedirect(req, resp, true, "Item removed from cart.", "/cart");
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }

    private void sendJsonOrRedirect(HttpServletRequest req, HttpServletResponse resp, boolean success, String message, String redirectUrl) throws IOException {
        String isAjax = req.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(isAjax) || req.getHeader("Accept") != null && req.getHeader("Accept").contains("application/json")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (success) {
                resp.getWriter().write(gson.toJson(ApiResponse.success(message)));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(ApiResponse.error("CART_ERROR", message)));
            }
        } else {
            if (!success) {
                req.getSession().setAttribute("errorMessage", message);
            } else {
                req.getSession().setAttribute("successMessage", message);
            }
            resp.sendRedirect(req.getContextPath() + redirectUrl);
        }
    }
}
