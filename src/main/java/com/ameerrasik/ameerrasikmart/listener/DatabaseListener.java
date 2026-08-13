package com.ameerrasik.ameerrasikmart.listener;

import com.ameerrasik.ameerrasikmart.util.DBUtil;
import com.ameerrasik.ameerrasikmart.util.PasswordUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 * Listener that initializes HikariCP and seeds the H2 database on application startup.
 */
@WebListener
public class DatabaseListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseListener.class);
    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing AmeerRasik Mart Database & Connection Pool...");
        try {
            Properties props = new Properties();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                if (in != null) {
                    props.load(in);
                }
            }

            String driver = props.getProperty("db.driver", "org.h2.Driver");
            String url = props.getProperty("db.url", "jdbc:h2:mem:ameerrasikmart;DB_CLOSE_DELAY=-1;MODE=MySQL");
            String username = props.getProperty("db.username", "sa");
            String password = props.getProperty("db.password", "");

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.max-size", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.min-idle", "2")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("db.pool.connection-timeout", "30000")));
            config.setPoolName("AmeerRasikMart-HikariPool");

            dataSource = new HikariDataSource(config);
            DBUtil.setDataSource(dataSource);

            // Execute schema and seed scripts
            executeSqlScript("schema.sql");
            executeSqlScript("seed.sql");

            // Seed exact BCrypt passwords for demo users
            seedDemoPasswords();

            logger.info("AmeerRasik Mart Database initialization complete!");
        } catch (Exception e) {
            logger.error("Critical error during database initialization", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private void executeSqlScript(String resourceName) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                logger.warn("SQL script {} not found on classpath", resourceName);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                 Connection conn = DBUtil.getConnection();
                 Statement stmt = conn.createStatement()) {

                StringBuilder sqlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sqlBuilder.append(line).append("\n");
                    if (trimmed.endsWith(";")) {
                        String sql = sqlBuilder.toString().trim();
                        if (sql.endsWith(";")) {
                            sql = sql.substring(0, sql.length() - 1);
                        }
                        if (!sql.isEmpty()) {
                            stmt.execute(sql);
                        }
                        sqlBuilder.setLength(0);
                    }
                }
                logger.info("Successfully executed SQL script: {}", resourceName);
            }
        } catch (Exception e) {
            logger.error("Error executing SQL script: " + resourceName, e);
        }
    }

    private void seedDemoPasswords() {
        String updateSql = "UPDATE users SET password_hash = ? WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            // Admin
            pstmt.setString(1, PasswordUtil.hashPassword("Admin@123"));
            pstmt.setString(2, "admin@ameerrasikmart.com");
            pstmt.executeUpdate();

            // Sellers
            pstmt.setString(1, PasswordUtil.hashPassword("Seller@123"));
            pstmt.setString(2, "techstore@ameerrasikmart.com");
            pstmt.executeUpdate();

            pstmt.setString(1, PasswordUtil.hashPassword("Seller@123"));
            pstmt.setString(2, "fashionhub@ameerrasikmart.com");
            pstmt.executeUpdate();

            // Buyers
            pstmt.setString(1, PasswordUtil.hashPassword("Buyer@123"));
            pstmt.setString(2, "buyer@ameerrasikmart.com");
            pstmt.executeUpdate();

            pstmt.setString(1, PasswordUtil.hashPassword("Buyer@123"));
            pstmt.setString(2, "rahul@gmail.com");
            pstmt.executeUpdate();

            pstmt.setString(1, PasswordUtil.hashPassword("Buyer@123"));
            pstmt.setString(2, "priya@gmail.com");
            pstmt.executeUpdate();

            logger.info("Demo user BCrypt passwords initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to seed demo user passwords", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down AmeerRasik Mart Connection Pool...");
        DBUtil.closeDataSource();
    }
}
