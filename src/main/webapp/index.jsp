<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AmeerRasik Mart - Premium Multi-Seller Marketplace</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <!-- Sticky Navigation Navbar -->
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <i class="fa-solid me-1 fa-bag-shopping" style="color: var(--accent);"></i>
            AmeerRasik<span style="color: var(--accent);">Mart</span>
            <span class="brand-badge">MVP</span>
        </a>

        <form action="${pageContext.request.contextPath}/products" method="GET" class="navbar-search">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" name="search" placeholder="Search across thousands of marketplace products...">
        </form>

        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/products"><i class="fa-solid fa-store"></i> Products</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:if test="${sessionScope.user.isBuyer()}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/wishlist">
                                <i class="fa-solid fa-heart"></i> Wishlist
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/cart">
                                <i class="fa-solid fa-cart-shopping"></i> Cart
                            </a>
                        </li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/orders"><i class="fa-solid fa-box"></i> Orders</a></li>
                    </c:if>
                    <c:if test="${sessionScope.user.isSeller()}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/seller/dashboard"><i class="fa-solid fa-chart-line"></i> Seller Portal</a></li>
                    </c:if>
                    <c:if test="${sessionScope.user.isAdmin()}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa-solid fa-user-shield"></i> Admin Panel</a></li>
                    </c:if>
                    <li class="nav-item">
                        <span style="color: #94A3B8; font-size: 0.85rem; padding: 0 0.5rem;">Hi, <strong>${sessionScope.user.name}</strong></span>
                    </li>
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/login.jsp"><i class="fa-solid fa-right-to-bracket"></i> Login</a></li>
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-sm btn-primary"><i class="fa-solid fa-user-plus"></i> Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>

    <!-- Hero Section -->
    <header style="background: linear-gradient(135deg, var(--primary-dark), #1E1B4B); color: #FFFFFF; padding: 4.5rem 1.5rem; text-align: center; border-bottom: 1px solid var(--border);">
        <div style="max-width: 800px; margin: 0 auto;">
            <span class="badge badge-info" style="margin-bottom: 1.25rem;">Modern Indian E-Commerce Marketplace</span>
            <h1 style="font-size: 3rem; font-weight: 800; letter-spacing: -0.03em; line-height: 1.15; margin-bottom: 1.25rem;">
                Discover Top Products From Verified Independent Sellers
            </h1>
            <p style="font-size: 1.15rem; color: #94A3B8; margin-bottom: 2rem; font-weight: 400;">
                AmeerRasik Mart connects buyers and sellers in a unified, lightning-fast marketplace platform built on high-performance Java Servlets and JDBC architecture.
            </p>
            <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-lg btn-primary">
                    <i class="fa-solid fa-bag-shopping"></i> Explore Catalog
                </a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-lg btn-outline" style="color: #FFFFFF; border-color: rgba(255,255,255,0.3);">
                    <i class="fa-solid fa-store"></i> Become a Seller
                </a>
            </div>
        </div>
    </header>

    <div class="container">
        <!-- Categories Section -->
        <section style="margin-bottom: 4rem;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <div>
                    <h2 style="font-size: 1.6rem; font-weight: 700;">Browse Top Categories</h2>
                    <p style="color: var(--text-muted); font-size: 0.92rem;">Find everything you need organized by department</p>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline">View All Categories &rarr;</a>
            </div>

            <div class="grid grid-cols-4">
                <a href="${pageContext.request.contextPath}/products?category=Electronics" class="card" style="text-align: center; padding: 2rem 1rem;">
                    <div style="width: 60px; height: 60px; background: #EEF2FF; color: var(--secondary); border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem; font-size: 1.5rem;">
                        <i class="fa-solid fa-laptop"></i>
                    </div>
                    <h3 style="font-size: 1.05rem; font-weight: 600; color: var(--text);">Electronics</h3>
                    <span style="font-size: 0.8rem; color: var(--text-muted);">Mice, Keyboards & Audio</span>
                </a>

                <a href="${pageContext.request.contextPath}/products?category=Fashion" class="card" style="text-align: center; padding: 2rem 1rem;">
                    <div style="width: 60px; height: 60px; background: #F0FDF4; color: var(--success); border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem; font-size: 1.5rem;">
                        <i class="fa-solid fa-shirt"></i>
                    </div>
                    <h3 style="font-size: 1.05rem; font-weight: 600; color: var(--text);">Fashion</h3>
                    <span style="font-size: 0.8rem; color: var(--text-muted);">Apparel & Hoodies</span>
                </a>

                <a href="${pageContext.request.contextPath}/products?category=Accessories" class="card" style="text-align: center; padding: 2rem 1rem;">
                    <div style="width: 60px; height: 60px; background: #FEF3C7; color: var(--warning); border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem; font-size: 1.5rem;">
                        <i class="fa-solid fa-briefcase"></i>
                    </div>
                    <h3 style="font-size: 1.05rem; font-weight: 600; color: var(--text);">Accessories</h3>
                    <span style="font-size: 0.8rem; color: var(--text-muted);">Backpacks & Wallets</span>
                </a>

                <a href="${pageContext.request.contextPath}/products?category=Home" class="card" style="text-align: center; padding: 2rem 1rem;">
                    <div style="width: 60px; height: 60px; background: #ECFEFF; color: var(--accent); border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem; font-size: 1.5rem;">
                        <i class="fa-solid fa-house-laptop"></i>
                    </div>
                    <h3 style="font-size: 1.05rem; font-weight: 600; color: var(--text);">Home & Living</h3>
                    <span style="font-size: 0.8rem; color: var(--text-muted);">Lamps & Flasks</span>
                </a>
            </div>
        </section>

        <!-- Why Choose Us -->
        <section style="background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius-lg); padding: 3rem 2rem; text-align: center; margin-bottom: 4rem;">
            <h2 style="font-size: 1.8rem; font-weight: 700; margin-bottom: 0.5rem;">Why AmeerRasik Mart?</h2>
            <p style="color: var(--text-muted); max-width: 600px; margin: 0 auto 2.5rem;">Engineered for high performance, reliability, and modern shopping experience.</p>

            <div class="grid grid-cols-3">
                <div>
                    <div style="font-size: 2rem; color: var(--secondary); margin-bottom: 1rem;"><i class="fa-solid fa-shield-halved"></i></div>
                    <h3 style="font-size: 1.1rem; font-weight: 600; margin-bottom: 0.4rem;">Secure Transactions</h3>
                    <p style="font-size: 0.88rem; color: var(--text-muted);">BCrypt password encryption, PreparedStatements against SQL injection, and session safety.</p>
                </div>
                <div>
                    <div style="font-size: 2rem; color: var(--accent); margin-bottom: 1rem;"><i class="fa-solid fa-bolt"></i></div>
                    <h3 style="font-size: 1.1rem; font-weight: 600; margin-bottom: 0.4rem;">Instant Multi-Seller Checkout</h3>
                    <p style="font-size: 0.88rem; color: var(--text-muted);">HikariCP connection pooling for fast database operations and real-time inventory management.</p>
                </div>
                <div>
                    <div style="font-size: 2rem; color: var(--success); margin-bottom: 1rem;"><i class="fa-solid fa-truck-fast"></i></div>
                    <h3 style="font-size: 1.1rem; font-weight: 600; margin-bottom: 0.4rem;">Order Fulfillment</h3>
                    <p style="font-size: 0.88rem; color: var(--text-muted);">Direct seller order tracking, status updates, and transparent buyer order history.</p>
                </div>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-content">
            <div>
                <div class="footer-brand">AmeerRasik Mart</div>
                <p style="font-size: 0.9rem; line-height: 1.6;">
                    A modern multi-seller marketplace web application built with Java 17, Servlets, JDBC, and H2 database.
                </p>
            </div>
            <div>
                <h4 style="color: #FFFFFF; font-size: 0.95rem; margin-bottom: 1rem;">Quick Links</h4>
                <ul style="list-style: none; font-size: 0.88rem; display: flex; flex-direction: column; gap: 0.5rem;">
                    <li><a href="${pageContext.request.contextPath}/products" style="color: #94A3B8;">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/login.jsp" style="color: #94A3B8;">Login Account</a></li>
                    <li><a href="${pageContext.request.contextPath}/register.jsp" style="color: #94A3B8;">Register Account</a></li>
                </ul>
            </div>
            <div>
                <h4 style="color: #FFFFFF; font-size: 0.95rem; margin-bottom: 1rem;">Demo Accounts</h4>
                <p style="font-size: 0.82rem; color: #94A3B8; line-height: 1.5;">
                    <strong>Admin:</strong> admin@ameerrasikmart.com<br>
                    <strong>Seller:</strong> techstore@ameerrasikmart.com<br>
                    <strong>Buyer:</strong> buyer@ameerrasikmart.com
                </p>
            </div>
            <div>
                <h4 style="color: #FFFFFF; font-size: 0.95rem; margin-bottom: 1rem;">System Health</h4>
                <a href="${pageContext.request.contextPath}/api/v1/health" target="_blank" class="btn btn-sm btn-outline" style="color: var(--accent); border-color: var(--accent);">
                    <i class="fa-solid fa-heart-pulse"></i> Check Health API
                </a>
            </div>
        </div>
        <div class="footer-bottom">
            <span>&copy; 2026 AmeerRasik Mart. All rights reserved.</span>
            <span>com.ameerrasik.ameerrasikmart</span>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script src="${pageContext.request.contextPath}/js/api.js"></script>
    <script src="${pageContext.request.contextPath}/js/cart.js"></script>
    <script src="${pageContext.request.contextPath}/js/wishlist.js"></script>
</body>
</html>
