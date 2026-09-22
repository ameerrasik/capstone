<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - AmeerRasik Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <i class="fa-solid fa-bag-shopping" style="color: var(--accent);"></i>
            AmeerRasik<span style="color: var(--accent);">Mart</span>
        </a>
        <div class="nav-menu">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-store"></i> Products</a>
            <a href="${pageContext.request.contextPath}/wishlist" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-heart" style="color: var(--danger);"></i> Wishlist</a>
            <a href="${pageContext.request.contextPath}/cart" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-cart-shopping"></i> Cart</a>
        </div>
    </nav>

    <div class="container">
        <h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 1.5rem;">Order History</h1>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="card" style="text-align: center; padding: 4rem 2rem;">
                    <div style="font-size: 3.5rem; color: var(--text-muted); margin-bottom: 1rem;">
                        <i class="fa-solid fa-box-open"></i>
                    </div>
                    <h2 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 0.5rem;">No Orders Placed Yet</h2>
                    <p style="color: var(--text-muted); margin-bottom: 1.5rem;">You haven't placed any orders on AmeerRasik Mart yet.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">Start Shopping</a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card" style="padding: 1.5rem;">
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Order Reference</th>
                                    <th>Date Placed</th>
                                    <th>Total Items</th>
                                    <th>Total Amount</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${orders}">
                                    <tr>
                                        <td><strong>#ARM-${o.id}</strong></td>
                                        <td style="color: var(--text-muted);">${o.createdAt}</td>
                                        <td>${o.items.size()} items</td>
                                        <td style="font-weight: 700; color: var(--primary);">&#8377;<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${o.status == 'CONFIRMED'}"><span class="badge badge-success">Confirmed</span></c:when>
                                                <c:when test="${o.status == 'SHIPPED'}"><span class="badge badge-info">Shipped</span></c:when>
                                                <c:when test="${o.status == 'DELIVERED'}"><span class="badge badge-success">Delivered</span></c:when>
                                                <c:when test="${o.status == 'CANCELLED'}"><span class="badge badge-danger">Cancelled</span></c:when>
                                                <c:otherwise><span class="badge badge-warning">${o.status}</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/order-details?id=${o.id}" class="btn btn-sm btn-outline">
                                                View Details
                                            </a>
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
        </div>
    </footer>

</body>
</html>
