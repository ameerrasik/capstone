# AmeerRasik Mart - MVP Status Dashboard

| Status | Feature / Milestone | Notes |
| :---: | :--- | :--- |
| [x] | Project Initialization | Maven structure, pom.xml, web.xml, CI configuration created |
| [x] | Database Schema & Seed Data | H2 SQL schema, indexes, FK constraints, realistic seed data |
| [x] | HikariCP Connection Pool | ServletContextListener initialization & graceful shutdown |
| [x] | Core Models & DTOs | User, Product, CartItem, Order, OrderItem, Review, Role, OrderStatus |
| [x] | DAO Layer (PreparedStatements) | UserDAO, ProductDAO, CartDAO, OrderDAO, ReviewDAO |
| [x] | Service Layer & Security | BCrypt hashing, input validation, cart & order transactions |
| [x] | Servlets & AuthFilter | Login, Register, Logout, Product, Cart, Checkout, Order, Seller, Admin |
| [x] | Design System & Styling | Custom CSS variables (Midnight Navy / Violet / Aqua), responsive layouts |
| [x] | Frontend Interactivity | Dynamic JS cart update, search/filter, toast notifications |
| [x] | Buyer E2E Flow | Browse -> Details -> Cart -> Checkout -> Mock Payment -> Order Confirmation |
| [x] | Seller Product Management | Create, edit, delete products, track seller sales |
| [x] | Admin Operations | View users, view orders, moderate product listings |
| [x] | Testing Suite | JUnit 5 unit & DAO integration tests, Mockito service tests |
| [x] | Diagrams & Documentation | ER diagram, Use Case diagram, Sequence diagram, README |
