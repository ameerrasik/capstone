package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.model.Role;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {
    private UserDAO userDAO;
    private HikariDataSource dataSource;

    @BeforeAll
    void setUpDB() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test_userdao;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setUsername("sa");
        config.setPassword("");
        dataSource = new HikariDataSource(config);
        DBUtil.setDataSource(dataSource);

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE users (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), email VARCHAR(150) UNIQUE, password_hash VARCHAR(255), role VARCHAR(20), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
        userDAO = new UserDAOImpl();
    }

    @AfterAll
    void tearDown() {
        DBUtil.closeDataSource();
    }

    @Test
    void testCreateAndFindUser() {
        User user = new User(null, "Test Buyer", "testbuyer@test.com", "hash123", Role.BUYER, null);
        User created = userDAO.createUser(user);

        assertNotNull(created.getId());
        assertEquals("Test Buyer", created.getName());

        Optional<User> found = userDAO.findByEmail("testbuyer@test.com");
        assertTrue(found.isPresent());
        assertEquals(Role.BUYER, found.get().getRole());
    }
}
