<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - AmeerRasik Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <i class="fa-solid fa-bag-shopping" style="color: var(--accent);"></i>
            AmeerRasik<span style="color: var(--accent);">Mart</span>
            <span class="brand-badge" style="background: var(--danger);">Admin</span>
        </a>
        <div class="nav-menu">
            <span style="color: #94A3B8; font-size: 0.85rem;">Logged in as: <strong>${sessionScope.user.name}</strong></span>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-store"></i> Storefront</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
        </div>
    </nav>

    <div class="container">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 1.8rem; font-weight: 800;">Administrator Control Center</h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">Global platform oversight, user auditing, order management, and catalog moderation</p>
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

        <!-- Marketplace Stats Grid -->
        <div class="grid grid-cols-4" style="margin-bottom: 2.5rem;">
            <div class="card" style="display: flex; align-items: center; gap: 1rem;">
                <div style="width: 48px; height: 48px; background: #EEF2FF; color: var(--secondary); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.3rem;">
                    <i class="fa-solid fa-users"></i>
                </div>
                <div>
                    <span style="font-size: 0.78rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Total Users</span>
                    <h3 style="font-size: 1.4rem; font-weight: 800;">${users.size()} Users</h3>
                </div>
            </div>

            <div class="card" style="display: flex; align-items: center; gap: 1rem;">
                <div style="width: 54px; height: 54px; background: #ECFEFF; color: var(--accent); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.3rem;">
                    <i class="fa-solid fa-boxes-stacked"></i>
                </div>
                <div>
                    <span style="font-size: 0.78rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Catalog Products</span>
                    <h3 style="font-size: 1.4rem; font-weight: 800;">${products.size()} Products</h3>
                </div>
            </div>

            <div class="card" style="display: flex; align-items: center; gap: 1rem;">
                <div style="width: 48px; height: 48px; background: #F0FDF4; color: var(--success); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.3rem;">
                    <i class="fa-solid fa-receipt"></i>
                </div>
                <div>
                    <span style="font-size: 0.78rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Total Orders</span>
                    <h3 style="font-size: 1.4rem; font-weight: 800;">${orders.size()} Orders</h3>
                </div>
            </div>

            <div class="card" style="display: flex; align-items: center; gap: 1rem;">
                <div style="width: 48px; height: 48px; background: #FEF3C7; color: var(--warning); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.3rem;">
                    <i class="fa-solid fa-sack-dollar"></i>
                </div>
                <div>
                    <span style="font-size: 0.78rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Marketplace Volume</span>
                    <h3 style="font-size: 1.4rem; font-weight: 800;">&#8377;<fmt:formatNumber value="${totalMarketplaceRevenue}" pattern="#,##0.00"/></h3>
                </div>
            </div>
        </div>

        <!-- Registered Users Table -->
        <div class="card" style="margin-bottom: 2.5rem; padding: 1.5rem;">
            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                <i class="fa-solid fa-users" style="color: var(--secondary);"></i> Registered Users (${users.size()})
            </h3>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Created Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>#USR-${u.id}</td>
                                <td><strong>${u.name}</strong></td>
                                <td>${u.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.role == 'ADMIN'}"><span class="badge badge-danger">ADMIN</span></c:when>
                                        <c:when test="${u.role == 'SELLER'}"><span class="badge badge-info">SELLER</span></c:when>
                                        <c:otherwise><span class="badge badge-success">BUYER</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="font-size: 0.85rem; color: var(--text-muted);">${u.createdAt}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Product Catalog Moderation Table -->
        <div class="card" style="margin-bottom: 2.5rem; padding: 1.5rem;">
            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                <i class="fa-solid fa-shield-halved" style="color: var(--danger);"></i> Product Catalog Moderation (${products.size()})
            </h3>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Product Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Seller</th>
                            <th>Moderation</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>#PRD-${p.id}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/product-details?id=${p.id}" target="_blank" style="font-weight: 600;">${p.name}</a>
                                </td>
                                <td><span class="badge badge-info">${p.category}</span></td>
                                <td style="font-weight: 700;">&#8377;<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                <td>${p.stockQty}</td>
                                <td>${p.sellerName}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/product/delete" method="POST" onsubmit="return confirm('Moderator action: Delete this product listing?');">
                                        <input type="hidden" name="productId" value="${p.id}">
                                        <button type="submit" class="btn btn-sm btn-danger"><i class="fa-solid fa-ban"></i> Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Marketplace Orders Oversight -->
        <div class="card" style="padding: 1.5rem;">
            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                <i class="fa-solid fa-receipt" style="color: var(--success);"></i> Global Marketplace Orders (${orders.size()})
            </h3>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Buyer</th>
                            <th>Date</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th>Admin Override Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td><strong>#ARM-${o.id}</strong></td>
                                <td>${o.buyerName} (${o.buyerEmail})</td>
                                <td style="font-size: 0.85rem; color: var(--text-muted);">${o.createdAt}</td>
                                <td style="font-weight: 700; color: var(--primary);">&#8377;<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${o.status == 'CONFIRMED'}"><span class="badge badge-success">Confirmed</span></c:when>
                                        <c:when test="${o.status == 'SHIPPED'}"><span class="badge badge-info">Shipped</span></c:when>
                                        <c:when test="${o.status == 'DELIVERED'}"><span class="badge badge-success">Delivered</span></c:when>
                                        <c:otherwise><span class="badge badge-warning">${o.status}</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/order/update-status" method="POST" style="display: flex; gap: 0.4rem;">
                                        <input type="hidden" name="orderId" value="${o.id}">
                                        <select name="status" class="form-control" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;">
                                            <option value="CONFIRMED" ${o.status == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                            <option value="SHIPPED" ${o.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                            <option value="DELIVERED" ${o.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                            <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                        </select>
                                        <button type="submit" class="btn btn-sm btn-outline">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-bottom">
            <span>&copy; 2026 AmeerRasik Mart. All rights reserved.</span>
        </div>
    </footer>

</body>
</html>
