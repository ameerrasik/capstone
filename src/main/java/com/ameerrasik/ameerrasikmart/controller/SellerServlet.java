package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.OrderStatus;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.OrderService;
import com.ameerrasik.ameerrasikmart.service.OrderServiceImpl;
import com.ameerrasik.ameerrasikmart.service.ProductService;
import com.ameerrasik.ameerrasikmart.service.ProductServiceImpl;
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
 * Controller for Seller Dashboard, product CRUD management, and incoming order oversight.
 */
@WebServlet(urlPatterns = {"/seller/dashboard", "/seller/product/create", "/seller/product/update", "/seller/product/delete", "/seller/order/update-status"})
public class SellerServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SellerServlet.class);
    private ProductService productService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User seller = getAuthenticatedSeller(req);
        if (seller == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<Product> products = productService.getProductsBySeller(seller.getId());
        List<Order> orders = orderService.getSellerOrders(seller.getId());
        List<String> categories = productService.getCategories();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Order order : orders) {
            totalRevenue = totalRevenue.add(order.getTotalAmount());
        }

        req.setAttribute("products", products);
        req.setAttribute("orders", orders);
        req.setAttribute("categories", categories);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("totalProductsCount", products.size());
        req.setAttribute("totalOrdersCount", orders.size());

        req.getRequestDispatcher("/seller/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User seller = getAuthenticatedSeller(req);
        if (seller == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String path = req.getServletPath();
        try {
            if ("/seller/product/create".equals(path)) {
                handleCreateProduct(req, seller);
                req.getSession().setAttribute("successMessage", "Product created successfully!");
            } else if ("/seller/product/update".equals(path)) {
                handleUpdateProduct(req, seller);
                req.getSession().setAttribute("successMessage", "Product updated successfully!");
            } else if ("/seller/product/delete".equals(path)) {
                handleDeleteProduct(req, seller);
                req.getSession().setAttribute("successMessage", "Product deleted successfully.");
            } else if ("/seller/order/update-status".equals(path)) {
                handleUpdateOrderStatus(req);
                req.getSession().setAttribute("successMessage", "Order status updated.");
            }
        } catch (ValidationException e) {
            logger.warn("Seller operation validation failure: {}", e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            logger.error("Seller operation error", e);
            req.getSession().setAttribute("errorMessage", "An error occurred while processing your request.");
        }

        resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
    }

    private void handleCreateProduct(HttpServletRequest req, User seller) {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        int stockQty = Integer.parseInt(req.getParameter("stockQty"));
        String category = req.getParameter("category");
        String imageUrl = req.getParameter("imageUrl");

        Product product = new Product(null, seller.getId(), name, description, price, stockQty, category, imageUrl, null);
        productService.createProduct(product, seller);
    }

    private void handleUpdateProduct(HttpServletRequest req, User seller) {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        int stockQty = Integer.parseInt(req.getParameter("stockQty"));
        String category = req.getParameter("category");
        String imageUrl = req.getParameter("imageUrl");

        Product product = new Product(id, seller.getId(), name, description, price, stockQty, category, imageUrl, null);
        productService.updateProduct(product, seller);
    }

    private void handleDeleteProduct(HttpServletRequest req, User seller) {
        Long id = Long.parseLong(req.getParameter("id"));
        productService.deleteProduct(id, seller);
    }

    private void handleUpdateOrderStatus(HttpServletRequest req) {
        Long orderId = Long.parseLong(req.getParameter("orderId"));
        String statusStr = req.getParameter("status");
        OrderStatus status = OrderStatus.fromString(statusStr);
        orderService.updateOrderStatus(orderId, status);
    }

    private User getAuthenticatedSeller(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        User user = (User) session.getAttribute("user");
        return (user != null && (user.isSeller() || user.isAdmin())) ? user : null;
    }
}
