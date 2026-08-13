package com.ameerrasik.ameerrasikmart.util;

import com.zaxxer.hikari.HikariDataSource;
import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database utility providing access to the HikariCP connection pool.
 */
public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static HikariDataSource dataSource;

    private DBUtil() {
    }

    /**
     * Initializes the static HikariDataSource instance.
     *
     * @param ds The configured HikariDataSource instance
     */
    public static synchronized void setDataSource(HikariDataSource ds) {
        dataSource = ds;
    }

    /**
     * Retrieves a database connection from the connection pool.
     *
     * @return Active Connection object
     * @throws DatabaseException if connection fails
     */
    public static Connection getConnection() throws DatabaseException {
        if (dataSource == null) {
            throw new DatabaseException("HikariCP DataSource is not initialized");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to obtain database connection from pool", e);
            throw new DatabaseException("Unable to acquire database connection", e);
        }
    }

    /**
     * Closes the connection pool.
     */
    public static synchronized void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource shut down successfully.");
        }
    }
}
