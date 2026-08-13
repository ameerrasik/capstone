package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of ProductDAO.
 */
public class ProductDAOImpl implements ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, "
                   + "u.name as seller_name, COALESCE(AVG(r.rating), 0.0) as avg_rating "
                   + "FROM products p "
                   + "LEFT JOIN users u ON p.seller_id = u.id "
                   + "LEFT JOIN reviews r ON p.id = r.product_id "
                   + "WHERE p.id = ? "
                   + "GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding product by ID: " + id, e);
            throw new DatabaseException("Database error finding product by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll(String category, String search, String sortBy) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, "
          + "u.name as seller_name, COALESCE(AVG(r.rating), 0.0) as avg_rating "
          + "FROM products p "
          + "LEFT JOIN users u ON p.seller_id = u.id "
          + "LEFT JOIN reviews r ON p.id = r.product_id WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category.trim())) {
            sql.append(" AND LOWER(p.category) = LOWER(?) ");
            params.add(category.trim());
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String searchPattern = "%" + search.trim().toLowerCase() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append(" GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name ");

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY p.price DESC ");
        } else {
            sql.append(" ORDER BY p.created_at DESC ");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching/filtering products", e);
            throw new DatabaseException("Database error retrieving products", e);
        }
        return products;
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, "
                   + "u.name as seller_name, COALESCE(AVG(r.rating), 0.0) as avg_rating "
                   + "FROM products p "
                   + "LEFT JOIN users u ON p.seller_id = u.id "
                   + "LEFT JOIN reviews r ON p.id = r.product_id "
                   + "WHERE p.seller_id = ? "
                   + "GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name "
                   + "ORDER BY p.id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding products for seller ID: " + sellerId, e);
            throw new DatabaseException("Database error retrieving seller products", e);
        }
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, product.getSellerId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStockQty());
            pstmt.setString(6, product.getCategory());
            pstmt.setString(7, product.getImageUrl());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                } else {
                    throw new DatabaseException("Creating product failed, no ID obtained.");
                }
            }
            return product;
        } catch (SQLException e) {
            logger.error("Error creating product: " + product.getName(), e);
            throw new DatabaseException("Database error creating product", e);
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStockQty());
            pstmt.setString(5, product.getCategory());
            pstmt.setString(6, product.getImageUrl());
            pstmt.setLong(7, product.getId());
            pstmt.setLong(8, product.getSellerId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating product ID: " + product.getId(), e);
            throw new DatabaseException("Database error updating product", e);
        }
    }

    @Override
    public boolean deleteProduct(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting product ID: " + id, e);
            throw new DatabaseException("Database error deleting product", e);
        }
    }

    @Override
    public boolean reduceStock(Long productId, int quantity, Connection conn) {
        String sql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setLong(2, productId);
            pstmt.setInt(3, quantity);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.error("Error reducing stock for product ID: " + productId, e);
            throw new DatabaseException("Database error reducing stock", e);
        }
    }

    @Override
    public boolean updateStock(Long productId, int newStock) {
        String sql = "UPDATE products SET stock_qty = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newStock);
            pstmt.setLong(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating stock for product ID: " + productId, e);
            throw new DatabaseException("Database error updating stock", e);
        }
    }

    @Override
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM products ORDER BY category ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            logger.error("Error fetching categories", e);
            throw new DatabaseException("Database error fetching categories", e);
        }
        return categories;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setSellerName(rs.getString("seller_name"));
        p.setAverageRating(rs.getDouble("avg_rating"));
        return p;
    }
}
