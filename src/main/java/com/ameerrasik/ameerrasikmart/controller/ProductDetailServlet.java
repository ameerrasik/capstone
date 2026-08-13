package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.Review;
import com.ameerrasik.ameerrasikmart.service.ProductService;
import com.ameerrasik.ameerrasikmart.service.ProductServiceImpl;
import com.ameerrasik.ameerrasikmart.service.ReviewService;
import com.ameerrasik.ameerrasikmart.service.ReviewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for displaying full product details and associated buyer reviews.
 */
@WebServlet("/product-details")
public class ProductDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailServlet.class);
    private ProductService productService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl();
        this.reviewService = new ReviewServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Long productId = Long.parseLong(idStr.trim());
            Optional<Product> productOpt = productService.getProductById(productId);

            if (productOpt.isEmpty()) {
                req.setAttribute("errorMessage", "The requested product does not exist or has been removed.");
                req.getRequestDispatcher("/error/404.jsp").forward(req, resp);
                return;
            }

            Product product = productOpt.get();
            List<Review> reviews = reviewService.getReviewsByProduct(productId);

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);

            req.getRequestDispatcher("/product-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
