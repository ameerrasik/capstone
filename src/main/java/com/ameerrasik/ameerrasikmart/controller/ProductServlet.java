package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.service.ProductService;
import com.ameerrasik.ameerrasikmart.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for marketplace product browsing, search, category filter, and sorting.
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        String search = req.getParameter("search");
        String sortBy = req.getParameter("sortBy");

        List<Product> products = productService.getAllProducts(category, search, sortBy);
        List<String> categories = productService.getCategories();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("selectedCategory", category != null ? category : "ALL");
        req.setAttribute("searchQuery", search != null ? search : "");
        req.setAttribute("selectedSortBy", sortBy != null ? sortBy : "newest");

        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }
}
