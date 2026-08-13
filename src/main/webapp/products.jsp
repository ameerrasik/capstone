<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products Catalogue - AmeerRasik Mart</title>
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
            <input type="text" name="search" placeholder="Search products..." value="${searchQuery}">
            <input type="hidden" name="category" value="${selectedCategory}">
            <input type="hidden" name="sortBy" value="${selectedSortBy}">
        </form>

        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/products" class="active"><i class="fa-solid fa-store"></i> Products</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:if test="${sessionScope.user.isBuyer()}">
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
        <div style="display: grid; grid-template-columns: 260px 1fr; gap: 2rem;">
            
            <!-- Sidebar Controls -->
            <aside class="card" style="align-self: start; padding: 1.5rem;">
                <h3 style="font-size: 1.1rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                    <i class="fa-solid fa-filter"></i> Filters & Sort
                </h3>

                <form action="${pageContext.request.contextPath}/products" method="GET">
                    <input type="hidden" name="search" value="${searchQuery}">
                    
                    <div class="form-group">
                        <label class="form-label">Category</label>
                        <select name="category" class="form-control" onchange="this.form.submit()">
                            <option value="ALL" ${selectedCategory == 'ALL' ? 'selected' : ''}>All Categories</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat}" ${selectedCategory == cat ? 'selected' : ''}>${cat}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Sort By</label>
                        <select name="sortBy" class="form-control" onchange="this.form.submit()">
                            <option value="newest" ${selectedSortBy == 'newest' ? 'selected' : ''}>Newest Arrivals</option>
                            <option value="price_asc" ${selectedSortBy == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="price_desc" ${selectedSortBy == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                        </select>
                    </div>
                </form>
            </aside>

            <!-- Product Grid Content -->
            <main>
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                    <div>
                        <h1 style="font-size: 1.6rem; font-weight: 800;">Marketplace Catalogue</h1>
                        <p style="color: var(--text-muted); font-size: 0.9rem;">
                            Showing <strong>${products.size()}</strong> items 
                            <c:if test="${selectedCategory != 'ALL'}">in <em>${selectedCategory}</em></c:if>
                        </p>
                    </div>
                </div>

                <c:if test="${empty products}">
                    <div class="card" style="text-align: center; padding: 4rem 2rem;">
                        <div style="font-size: 3rem; color: var(--text-muted); margin-bottom: 1rem;">
                            <i class="fa-solid fa-box-open"></i>
                        </div>
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">No Products Found</h3>
                        <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Try resetting your filters or search keywords.</p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Reset Filters</a>
                    </div>
                </c:if>

                <div class="grid grid-cols-3">
                    <c:forEach var="p" items="${products}">
                        <div class="card product-card">
                            <div class="product-image-wrap">
                                <span class="product-category-tag">${p.category}</span>
                                <img src="${p.imageUrl}" alt="${p.name}" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80'">
                            </div>
                            <div class="product-details">
                                <a href="${pageContext.request.contextPath}/product-details?id=${p.id}" class="product-name">${p.name}</a>
                                <span style="font-size: 0.78rem; color: var(--text-muted); margin-bottom: 0.5rem;">Sold by: <strong>${p.sellerName}</strong></span>
                                
                                <div class="product-meta">
                                    <span class="product-price">&#8377;<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></span>
                                    <span class="product-rating">
                                        <i class="fa-solid fa-star"></i> <fmt:formatNumber value="${p.averageRating}" pattern="0.0"/>
                                    </span>
                                </div>

                                <div style="margin-top: 1rem; display: flex; gap: 0.5rem;">
                                    <a href="${pageContext.request.contextPath}/product-details?id=${p.id}" class="btn btn-sm btn-outline" style="flex: 1;">
                                        Details
                                    </a>
                                    <c:if test="${empty sessionScope.user || sessionScope.user.isBuyer()}">
                                        <button type="button" class="btn btn-sm btn-primary" onclick="Cart.addItem('${p.id}', 1, '${pageContext.request.contextPath}')" ${!p.isInStock() ? 'disabled' : ''}>
                                            <i class="fa-solid fa-cart-plus"></i> ${p.isInStock() ? 'Add' : 'Out of Stock'}
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </main>

        </div>
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
</body>
</html>
