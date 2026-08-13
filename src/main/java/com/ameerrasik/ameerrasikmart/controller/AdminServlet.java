package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.OrderStatus;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.*;
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
 * Controller for Admin Dashboard, user management, global order tracking, and product moderation.
 */
@WebServlet(urlPatterns = {"/admin/dashboard", "/admin/product/delete", "/admin/order/update-status"})
public class AdminServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminServlet.class);
    private UserService userService;
    private ProductService productService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User admin = getAuthenticatedAdmin(req);
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<User> users = userService.getAllUsers();
        List<Product> products = productService.getAllProducts(null, null, "newest");
        List<Order> orders = orderService.getAllOrders();

        BigDecimal totalMarketplaceRevenue = BigDecimal.ZERO;
        for (Order order : orders) {
            totalMarketplaceRevenue = totalMarketplaceRevenue.add(order.getTotalAmount());
        }

        req.setAttribute("users", users);
        req.setAttribute("products", products);
        req.setAttribute("orders", orders);
        req.setAttribute("totalMarketplaceRevenue", totalMarketplaceRevenue);

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User admin = getAuthenticatedAdmin(req);
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String path = req.getServletPath();
        try {
            if ("/admin/product/delete".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                productService.deleteProduct(productId, admin);
                req.getSession().setAttribute("successMessage", "Product ID " + productId + " moderated/removed successfully.");
            } else if ("/admin/order/update-status".equals(path)) {
                Long orderId = Long.parseLong(req.getParameter("orderId"));
                String statusStr = req.getParameter("status");
                orderService.updateOrderStatus(orderId, OrderStatus.fromString(statusStr));
                req.getSession().setAttribute("successMessage", "Order ID " + orderId + " status updated.");
            }
        } catch (Exception e) {
            logger.error("Admin action failed", e);
            req.getSession().setAttribute("errorMessage", "Admin action failed: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }

    private User getAuthenticatedAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        User user = (User) session.getAttribute("user");
        return (user != null && user.isAdmin()) ? user : null;
    }
}
