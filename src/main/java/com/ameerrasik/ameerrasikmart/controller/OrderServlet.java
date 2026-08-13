package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.User;
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
import java.util.List;
import java.util.Optional;

/**
 * Controller for buyer order history and order detail views.
 */
@WebServlet(urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String servletPath = req.getServletPath();
        if ("/order-details".equals(servletPath)) {
            handleOrderDetails(req, resp, user);
        } else {
            handleOrderHistory(req, resp, user);
        }
    }

    private void handleOrderHistory(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        List<Order> orders = orderService.getBuyerOrders(user.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/orders.jsp").forward(req, resp);
    }

    private void handleOrderDetails(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(idStr.trim());
            Optional<Order> orderOpt = orderService.getOrderById(orderId);

            if (orderOpt.isEmpty()) {
                req.setAttribute("errorMessage", "Order not found.");
                req.getRequestDispatcher("/orders.jsp").forward(req, resp);
                return;
            }

            Order order = orderOpt.get();
            // Security check: ensure order belongs to logged-in user (unless admin/seller)
            if (!user.isAdmin() && !order.getBuyerId().equals(user.getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized access to order details.");
                return;
            }

            req.setAttribute("order", order);
            req.getRequestDispatcher("/order-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }
}
