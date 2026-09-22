<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Wishlist - AmeerRasik Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <!-- Sticky Navigation Navbar -->
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <i class="fa-solid fa-bag-shopping" style="color: var(--accent);"></i>
            AmeerRasik<span style="color: var(--accent);">Mart</span>
        </a>

        <form action="${pageContext.request.contextPath}/products" method="GET" class="navbar-search">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" name="search" placeholder="Search products...">
        </form>

        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/products"><i class="fa-solid fa-store"></i> Products</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:if test="${sessionScope.user.isBuyer()}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/wishlist" class="active">
                                <i class="fa-solid fa-heart" style="color: var(--danger);"></i> Wishlist
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
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline"><i class="fa-solid fa-right-from-bracket"></i> Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/login.jsp"><i class="fa-solid fa-right-to-bracket"></i> Login</a></li>
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-sm btn-primary"><i class="fa-solid fa-user-plus"></i> Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
            <div>
                <h1 style="font-size: 1.8rem; font-weight: 800; display: flex; align-items: center; gap: 0.75rem;">
                    <i class="fa-solid fa-heart" style="color: var(--danger);"></i> My Wishlist
                    <c:if test="${not empty wishlistItems}">
                        <span class="badge badge-info" style="font-size: 0.85rem;">${wishlistItems.size()} items</span>
                    </c:if>
                </h1>
                <p style="color: var(--text-muted); font-size: 0.95rem;">Keep track of products you'd like to purchase later.</p>
            </div>
            <c:if test="${not empty wishlistItems}">
                <form action="${pageContext.request.contextPath}/wishlist" method="POST" onsubmit="return confirm('Clear your entire wishlist?');">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--danger);">
                        <i class="fa-solid fa-trash-can"></i> Clear All
                    </button>
                </form>
            </c:if>
        </div>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.errorMessage}
            </div>
            <% session.removeAttribute("errorMessage"); %>
        </c:if>
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i> ${sessionScope.successMessage}
            </div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>

        <c:choose>
            <c:when test="${empty wishlistItems}">
                <div class="card" style="text-align: center; padding: 4rem 2rem;">
                    <div style="font-size: 3.5rem; color: #FDA4AF; margin-bottom: 1rem;">
                        <i class="fa-regular fa-heart"></i>
                    </div>
                    <h2 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 0.5rem;">Your Wishlist is Empty</h2>
                    <p style="color: var(--text-muted); margin-bottom: 1.5rem; max-width: 480px; margin-left: auto; margin-right: auto;">
                        Explore our products and tap the heart icon on any item to save it to your personal wishlist.
                    </p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">
                        <i class="fa-solid fa-store"></i> Browse Products
                    </a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card" style="padding: 1.5rem;">
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Price</th>
                                    <th>Stock Status</th>
                                    <th style="text-align: right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${wishlistItems}">
                                    <tr id="wishlist-item-${item.productId}">
                                        <td>
                                            <div style="display: flex; align-items: center; gap: 1rem;">
                                                <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}">
                                                    <img src="${item.imageUrl}" alt="${item.productName}" style="width: 60px; height: 60px; object-fit: cover; border-radius: var(--radius-sm);" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80'">
                                                </a>
                                                <div>
                                                    <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" style="font-weight: 600; color: var(--text);">
                                                        ${item.productName}
                                                    </a>
                                                    <div style="font-size: 0.8rem; color: var(--text-muted);">${item.category}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td style="font-weight: 700; font-size: 1.05rem; color: var(--primary);">
                                            &#8377;<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.isInStock()}">
                                                    <span class="badge badge-success">
                                                        <i class="fa-solid fa-check"></i> In Stock (${item.availableStock})
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">
                                                        <i class="fa-solid fa-xmark"></i> Out of Stock
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="text-align: right;">
                                            <div style="display: inline-flex; gap: 0.5rem; align-items: center;">
                                                <c:choose>
                                                    <c:when test="${item.isInStock()}">
                                                        <button type="button" class="btn btn-sm btn-primary" onclick="Wishlist.moveToCart('${item.productId}', 1, '${pageContext.request.contextPath}')" title="Move to Shopping Cart">
                                                            <i class="fa-solid fa-cart-arrow-down"></i> Move to Cart
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button type="button" class="btn btn-sm btn-primary" disabled title="Product is currently out of stock">
                                                            Out of Stock
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>

                                                <button type="button" class="btn btn-sm btn-outline" style="color: var(--danger); border-color: var(--border);" onclick="Wishlist.removeItem('${item.productId}', '${pageContext.request.contextPath}')" title="Remove from Wishlist">
                                                    <i class="fa-solid fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Footer -->
    <footer class="footer">
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
