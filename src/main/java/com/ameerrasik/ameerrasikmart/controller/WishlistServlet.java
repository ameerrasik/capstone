package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.dto.ApiResponse;
import com.ameerrasik.ameerrasikmart.dto.WishlistItemDTO;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.WishlistService;
import com.ameerrasik.ameerrasikmart.service.WishlistServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing buyer wishlist operations (add, remove, toggle, move to cart, clear, view).
 */
@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(WishlistServlet.class);
    private WishlistService wishlistService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.wishlistService = new WishlistServiceImpl();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("count".equalsIgnoreCase(action)) {
            int count = wishlistService.getWishlistCount(user.getId());
            Map<String, Object> data = new HashMap<>();
            data.put("count", count);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(data));
            return;
        }

        List<WishlistItemDTO> wishlistItems = wishlistService.getWishlistItems(user.getId());
        req.setAttribute("wishlistItems", wishlistItems);
        req.setAttribute("wishlistCount", wishlistItems.size());
        req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            sendJsonOrRedirect(req, resp, false, "Please log in to manage your wishlist.", "/login.jsp", null);
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "toggle";
        }

        try {
            switch (action.toLowerCase()) {
                case "add":
                    handleAdd(req, resp, user);
                    break;
                case "remove":
                    handleRemove(req, resp, user);
                    break;
                case "toggle":
                    handleToggle(req, resp, user);
                    break;
                case "move_to_cart":
                    handleMoveToCart(req, resp, user);
                    break;
                case "clear":
                    wishlistService.clearWishlist(user.getId());
                    sendJsonOrRedirect(req, resp, true, "Wishlist cleared.", "/wishlist", null);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/wishlist");
            }
        } catch (ValidationException e) {
            logger.warn("Wishlist operation failed for user {}: {}", user.getId(), e.getMessage());
            sendJsonOrRedirect(req, resp, false, e.getMessage(), "/wishlist", null);
        } catch (Exception e) {
            logger.error("Unexpected error during wishlist operation", e);
            sendJsonOrRedirect(req, resp, false, "An unexpected error occurred.", "/wishlist", null);
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        wishlistService.addToWishlist(user.getId(), productId);
        int count = wishlistService.getWishlistCount(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("inWishlist", true);
        data.put("count", count);
        sendJsonOrRedirect(req, resp, true, "Item added to wishlist!", "/wishlist", data);
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        wishlistService.removeFromWishlist(user.getId(), productId);
        int count = wishlistService.getWishlistCount(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("inWishlist", false);
        data.put("count", count);
        sendJsonOrRedirect(req, resp, true, "Item removed from wishlist.", "/wishlist", data);
    }

    private void handleToggle(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        boolean inWishlist = wishlistService.toggleWishlist(user.getId(), productId);
        int count = wishlistService.getWishlistCount(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("inWishlist", inWishlist);
        data.put("count", count);
        String msg = inWishlist ? "Item added to wishlist!" : "Item removed from wishlist.";
        sendJsonOrRedirect(req, resp, true, msg, "/wishlist", data);
    }

    private void handleMoveToCart(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = 1;
        String qtyParam = req.getParameter("quantity");
        if (qtyParam != null && !qtyParam.trim().isEmpty()) {
            quantity = Integer.parseInt(qtyParam.trim());
        }

        wishlistService.moveToCart(user.getId(), productId, quantity);
        int count = wishlistService.getWishlistCount(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("movedToCart", true);
        data.put("count", count);
        sendJsonOrRedirect(req, resp, true, "Item moved to cart successfully!", "/wishlist", data);
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }

    private void sendJsonResponse(HttpServletResponse resp, int status, ApiResponse<?> response) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(response));
    }

    private void sendJsonOrRedirect(HttpServletRequest req, HttpServletResponse resp, boolean success,
                                    String message, String redirectUrl, Object data) throws IOException {
        String isAjax = req.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(isAjax) || (req.getHeader("Accept") != null && req.getHeader("Accept").contains("application/json"))) {
            if (success) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(data != null ? data : message));
            } else {
                sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ApiResponse.error("WISHLIST_ERROR", message));
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
