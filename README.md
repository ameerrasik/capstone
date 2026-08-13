# AmeerRasik Mart

**AmeerRasik Mart** is a modern, high-performance, multi-seller e-commerce marketplace web application built from absolute zero using Java 17, Java Servlets (javax.servlet), H2 Database, HikariCP connection pooling, JDBC, JSP/JSTL, and custom Vanilla CSS/JS.

---

## 🚀 Technical Features

### 🛒 Buyer Features
- **User Authentication**: Secure registration and BCrypt password authentication.
- **Product Browsing & Search**: Search catalogue by keywords, filter by category, and sort by price or recency.
- **Product Details & Ratings**: High-res image display, stock availability badges, and customer ratings/reviews.
- **Shopping Cart**: Real-time cart total calculation, quantity updates, stock limit enforcement, and item removal.
- **Transactional Checkout**: Server-side cart/stock validation, transactional order placement, stock reduction, and cart clearing.
- **Mock Payment Confirmation**: Payment simulation for Cards, UPI, and Cash on Delivery.
- **Order History & Tracking**: Detailed order receipts and status tracking (`CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`).

### 🏪 Seller Features
- **Seller Portal Dashboard**: Sales revenue metrics, active product count, and incoming customer orders.
- **Product Management (CRUD)**: Create, view, update stock, and delete seller product listings.
- **Fulfillment Management**: Update order shipment and delivery status for customer orders.

### 🛡️ Admin Features
- **Control Center**: Marketplace volume metrics, user registry auditing, global order oversight, and product catalog moderation.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Language & Platform** | Java 17, Apache Tomcat 9.0.x |
| **Servlet Specification** | Servlet API 4.0 (`javax.servlet.*`) |
| **Database & Pooling** | H2 Database Engine, HikariCP 5.1.0, JDBC |
| **Frontend** | JSP 2.3, JSTL 1.2, HTML5, Vanilla CSS3, Vanilla JS (Fetch/AJAX) |
| **Security & Hashing** | HttpSession, jBCrypt 0.4 |
| **JSON & Logging** | Gson 2.10.1, SLF4J, Logback |
| **Testing** | JUnit 5 Jupiter, Mockito 5.5 |
| **Build & CI** | Apache Maven, GitHub Actions |

---

## 🔑 Demo Accounts & Test Credentials

The database automatically initializes schema and seed data on application startup.

| Role | Email Address | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@ameerrasikmart.com` | `Admin@123` | Full system moderation & user oversight |
| **Seller 1** | `techstore@ameerrasikmart.com` | `Seller@123` | Manage electronics & tech inventory |
| **Seller 2** | `fashionhub@ameerrasikmart.com` | `Seller@123` | Manage apparel & fashion items |
| **Buyer 1** | `buyer@ameerrasikmart.com` | `Buyer@123` | Shopping cart & checkout flow |

---

## 🏗️ Architecture & Project Structure

```
ameerrasik-mart/
├── pom.xml
├── README.md
├── .env.example
├── .gitignore
├── .github/
│   └── workflows/
│       └── build.yml
├── docs/
│   ├── architecture.md
│   ├── er-diagram.md
│   ├── use-case.md
│   ├── sequence-diagram.md
│   ├── mvp-demo-script.md
│   └── MVP_STATUS.md
├── db/
│   └── migrations/
│       └── V1__init_schema.sql
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/ameerrasik/ameerrasikmart/
    │   │       ├── controller/   (Login, Product, Cart, Checkout, Order, Seller, Admin)
    │   │       ├── service/      (UserService, ProductService, CartService, OrderService)
    │   │       ├── dao/          (UserDAO, ProductDAO, CartDAO, OrderDAO, ReviewDAO)
    │   │       ├── model/        (User, Product, CartItem, Order, OrderItem, Review)
    │   │       ├── dto/          (CartItemDTO, ApiResponse)
    │   │       ├── filter/       (AuthFilter)
    │   │       ├── listener/     (DatabaseListener - HikariCP pool)
    │   │       ├── util/         (DBUtil, PasswordUtil)
    │   │       └── exception/    (Custom exception definitions)
    │   ├── resources/
    │   │   ├── application.properties
    │   │   ├── schema.sql
    │   │   ├── seed.sql
    │   │   └── logback.xml
    │   └── webapp/
    │       ├── index.jsp
    │       ├── login.jsp
    │       ├── register.jsp
    │       ├── products.jsp
    │       ├── product-details.jsp
    │       ├── cart.jsp
    │       ├── checkout.jsp
    │       ├── order-success.jsp
    │       ├── orders.jsp
    │       ├── order-details.jsp
    │       ├── css/
    │       │   └── design-system.css
    │       ├── js/
    │       │   ├── api.js
    │       │   ├── cart.js
    │       │   ├── toast.js
    │       │   └── validation.js
    │       ├── seller/
    │       │   └── dashboard.jsp
    │       ├── admin/
    │       │   └── dashboard.jsp
    │       └── WEB-INF/
    │           └── web.xml
    └── test/
        └── java/com/ameerrasik/ameerrasikmart/
            ├── dao/
            └── service/
```

---

## ⚡ How to Build & Run

### Quick One-Command Run (Embedded Server):
```bash
$env:JAVA_HOME="C:\tools\jdk-17.0.20+8"
$env:PATH="C:\tools\apache-maven-3.9.6\bin;C:\tools\jdk-17.0.20+8\bin;" + $env:PATH
mvn jetty:run
```
Then open: `http://localhost:8080/ameerrasikmart/`

### Build Production WAR Package:
```bash
mvn clean package
```

### Run Unit & Integration Tests:
```bash
mvn test
```

### Health Check Endpoint:
```
GET /api/v1/health
```
Response:
```json
{
  "status": "UP",
  "db": "UP"
}
```

---

## 📊 System Diagrams

- **[Entity Relationship Diagram](docs/er-diagram.md)**
- **[Use Case Diagram](docs/use-case.md)**
- **[Sequence Diagram](docs/sequence-diagram.md)**
- **[Architecture Document](docs/architecture.md)**
- **[Faculty Demo Script](docs/mvp-demo-script.md)**

---

## 📜 License

Academic Open Source - Developed for AmeerRasik Mart MVP Review.
