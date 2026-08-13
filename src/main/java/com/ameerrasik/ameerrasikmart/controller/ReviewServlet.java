package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.ReviewService;
import com.ameerrasik.ameerrasikmart.service.ReviewServiceImpl;
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
 * Controller for submitting product ratings and reviews.
 */
@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServlet.class);
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        this.reviewService = new ReviewServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String productIdStr = req.getParameter("productId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        try {
            Long productId = Long.parseLong(productIdStr);
            Integer rating = Integer.parseInt(ratingStr);

            reviewService.addReview(productId, user.getId(), rating, comment);
            req.getSession().setAttribute("successMessage", "Thank you! Your review has been published.");
            resp.sendRedirect(req.getContextPath() + "/product-details?id=" + productId);

        } catch (ValidationException e) {
            logger.warn("Review validation failed: {}", e.getMessage());
            req.getSession().setAttribute("errorMessage", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/product-details?id=" + productIdStr);
        } catch (Exception e) {
            logger.error("Unexpected error submitting review", e);
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
