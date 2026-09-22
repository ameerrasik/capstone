<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #ARM-${order.id} Details - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/wishlist" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-heart" style="color: var(--danger);"></i> Wishlist</a>
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-arrow-left"></i> Back to Orders</a>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
            <div>
                <h1 style="font-size: 1.8rem; font-weight: 800;">Order Details</h1>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Reference: <strong>#ARM-${order.id}</strong> &bull; Placed on ${order.createdAt}</p>
            </div>
            <div>
                <c:choose>
                    <c:when test="${order.status == 'CONFIRMED'}"><span class="badge badge-success" style="font-size: 0.9rem;">Status: Confirmed</span></c:when>
                    <c:when test="${order.status == 'SHIPPED'}"><span class="badge badge-info" style="font-size: 0.9rem;">Status: Shipped</span></c:when>
                    <c:when test="${order.status == 'DELIVERED'}"><span class="badge badge-success" style="font-size: 0.9rem;">Status: Delivered</span></c:when>
                    <c:otherwise><span class="badge badge-warning" style="font-size: 0.9rem;">Status: ${order.status}</span></c:otherwise>
                </c:choose>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 340px; gap: 2rem;">
            
            <!-- Order Items List -->
            <div class="card" style="padding: 1.5rem;">
                <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                    Items Purchased (${order.items.size()})
                </h3>

                <div style="display: flex; flex-direction: column; gap: 1rem;">
                    <c:forEach var="item" items="${order.items}">
                        <div style="display: flex; align-items: center; justify-content: space-between; padding-bottom: 1rem; border-bottom: 1px solid var(--border);">
                            <div style="display: flex; align-items: center; gap: 1rem;">
                                <img src="${item.productImage}" alt="${item.productName}" style="width: 60px; height: 60px; object-fit: cover; border-radius: var(--radius-sm);" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80'">
                                <div>
                                    <h4 style="font-size: 0.98rem; font-weight: 600; margin-bottom: 0.2rem;">${item.productName}</h4>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);">${item.productCategory}</span>
                                    <div style="font-size: 0.85rem; color: var(--text-muted); margin-top: 0.2rem;">
                                        &#8377;<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/> &times; ${item.quantity} units
                                    </div>
                                </div>
                            </div>
                            <div style="font-size: 1.05rem; font-weight: 700; color: var(--primary);">
                                &#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Receipt Summary Sidebar -->
            <div class="card" style="align-self: start; padding: 1.5rem;">
                <h3 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                    Payment Summary
                </h3>

                <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.95rem;">
                    <span style="color: var(--text-muted);">Buyer Account</span>
                    <strong>${order.buyerName}</strong>
                </div>

                <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.95rem;">
                    <span style="color: var(--text-muted);">Buyer Email</span>
                    <span style="font-size: 0.85rem;">${order.buyerEmail}</span>
                </div>

                <div style="display: flex; justify-content: space-between; margin-bottom: 1.25rem; font-size: 0.95rem;">
                    <span style="color: var(--text-muted);">Delivery Charge</span>
                    <span style="color: var(--success); font-weight: 600;">FREE</span>
                </div>

                <div style="display: flex; justify-content: space-between; padding-top: 1rem; border-top: 1px solid var(--border); font-size: 1.25rem; font-weight: 800; color: var(--primary);">
                    <span>Total Amount</span>
                    <span>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                </div>
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
