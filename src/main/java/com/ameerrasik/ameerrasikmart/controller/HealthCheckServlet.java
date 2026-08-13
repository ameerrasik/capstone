package com.ameerrasik.ameerrasikmart.controller;

import com.ameerrasik.ameerrasikmart.util.DBUtil;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Health check endpoint verifying active application and database connectivity.
 */
@WebServlet("/api/v1/health")
public class HealthCheckServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JsonObject json = new JsonObject();
        json.addProperty("status", "UP");

        boolean dbUp = false;
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            dbUp = stmt.execute("SELECT 1");
        } catch (Exception e) {
            logger.error("Health check DB connection test failed", e);
        }

        if (dbUp) {
            json.addProperty("db", "UP");
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            json.addProperty("db", "DOWN");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        resp.getWriter().write(json.toString());
    }
}
