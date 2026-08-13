<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-box"></i> View All Orders</a>
        </div>
    </nav>

    <div class="container" style="max-width: 680px; padding: 3rem 1.5rem;">
        <div class="card" style="text-align: center; padding: 3rem 2rem;">
            <div style="width: 72px; height: 72px; background: #D1FAE5; color: var(--success); border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1.5rem; font-size: 2.2rem;">
                <i class="fa-solid fa-circle-check"></i>
            </div>
            
            <h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 0.5rem;">Order Placed Successfully!</h1>
            <p style="color: var(--text-muted); font-size: 1rem; margin-bottom: 2rem;">
                Thank you for your purchase. Your order has been confirmed and sent to our marketplace sellers.
            </p>

            <c:set var="order" value="${sessionScope.latestOrder}"/>
            <c:if test="${not empty order}">
                <div style="background: var(--surface-alt); border-radius: var(--radius-sm); padding: 1.5rem; text-align: left; margin-bottom: 2rem;">
                    <div style="display: flex; justify-content: space-between; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem; margin-bottom: 1rem;">
                        <span>Order ID: <strong>#ARM-${order.id}</strong></span>
                        <span class="badge badge-success">${order.status}</span>
                    </div>

                    <div style="font-size: 0.9rem; margin-bottom: 0.5rem; display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Payment Method:</span>
                        <strong>${sessionScope.paymentMethod}</strong>
                    </div>

                    <div style="font-size: 0.9rem; margin-bottom: 1rem; display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Total Amount Paid:</span>
                        <strong style="color: var(--primary); font-size: 1.1rem;">&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong>
                    </div>

                    <div style="border-top: 1px dashed var(--border); padding-top: 0.75rem;">
                        <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-muted); display: block; margin-bottom: 0.5rem;">Items Included:</span>
                        <c:forEach var="item" items="${order.items}">
                            <div style="font-size: 0.88rem; display: flex; justify-content: space-between; margin-bottom: 0.3rem;">
                                <span>${item.productName} (x${item.quantity})</span>
                                <span>&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary btn-lg">
                    <i class="fa-solid fa-box"></i> View Order History
                </a>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-lg">
                    Continue Shopping
                </a>
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
