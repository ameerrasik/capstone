# AmeerRasik Mart - Architectural Overview

## System Architecture

AmeerRasik Mart follows a strict layered MVC architecture using Java EE Servlets, JDBC, HikariCP, and H2 Database:

```
+-------------------------------------------------------------+
|               Browser / Presentation Layer                 |
|             (JSP, JSTL, HTML5, CSS3, Vanilla JS)            |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                    Security Filter Layer                    |
|                        (AuthFilter)                         |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|             Front Controller / Servlet Layer                |
|  (LoginServlet, ProductServlet, CartServlet, Checkout, etc)|
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                   Business Service Layer                    |
|  (UserService, ProductService, CartService, OrderService)  |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                   Data Access Object Layer                  |
|     (UserDAO, ProductDAO, CartDAO, OrderDAO, ReviewDAO)     |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                   Connection Pool Layer                     |
|                (HikariCP & DatabaseListener)                |
+-------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                      H2 Database Engine                     |
+-------------------------------------------------------------+
```

## Architectural Design Patterns

1. **DAO Pattern**: Decouples business logic from persistence logic. All SQL queries reside strictly inside DAO implementations.
2. **Front Controller Pattern**: Java Servlets intercept incoming HTTP requests, delegate processing to services, and route responses to appropriate JSPs.
3. **Singleton Connection Pool**: HikariCP maintains a thread-safe connection pool initialized via `ServletContextListener`.
4. **Data Transfer Objects (DTO)**: `CartItemDTO` aggregates cart data with real-time product pricing and stock details.
5. **Layered Authorization**: `AuthFilter` intercepts protected paths (`/admin/*`, `/seller/*`, `/buyer/*`) enforcing role-based boundaries.
