package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.service.ProductService;
import com.ameerrasik.ameerrasikmart.service.ProductServiceImpl;
import com.ameerrasik.ameerrasikmart.service.WishlistService;
import com.ameerrasik.ameerrasikmart.service.WishlistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Controller for marketplace product browsing, search, category filter, and sorting.
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    private ProductService productService;
    private WishlistService wishlistService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl();
        this.wishlistService = new WishlistServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        String search = req.getParameter("search");
        String sortBy = req.getParameter("sortBy");

        List<Product> products = productService.getAllProducts(category, search, sortBy);
        List<String> categories = productService.getCategories();

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        Set<Long> wishlistProductIds = (user != null && user.isBuyer())
                ? wishlistService.getWishlistProductIds(user.getId())
                : Collections.emptySet();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("wishlistProductIds", wishlistProductIds);
        req.setAttribute("selectedCategory", category != null ? category : "ALL");
        req.setAttribute("searchQuery", search != null ? search : "");
        req.setAttribute("selectedSortBy", sortBy != null ? sortBy : "newest");

        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }
}
