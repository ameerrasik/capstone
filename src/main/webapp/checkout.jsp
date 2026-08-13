<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout & Mock Payment - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/cart" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-arrow-left"></i> Return to Cart</a>
        </div>
    </nav>

    <div class="container">
        <h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 1.5rem;">Checkout & Mock Payment</h1>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="POST">
            <div style="display: grid; grid-template-columns: 1fr 380px; gap: 2rem;">
                
                <!-- Left Column: Shipping & Payment Method -->
                <div>
                    <!-- Shipping Address Simulation -->
                    <div class="card" style="margin-bottom: 2rem; padding: 2rem;">
                        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                            <i class="fa-solid fa-location-dot" style="color: var(--secondary);"></i> Shipping Address
                        </h2>

                        <div class="form-group">
                            <label class="form-label">Full Name</label>
                            <input type="text" class="form-control" value="${sessionScope.user.name}" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Delivery Street Address</label>
                            <input type="text" class="form-control" value="123 MG Road, Koramangala" required>
                        </div>
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                            <div class="form-group">
                                <label class="form-label">City</label>
                                <input type="text" class="form-control" value="Bengaluru" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label">PIN Code</label>
                                <input type="text" class="form-control" value="560034" required>
                            </div>
                        </div>
                    </div>

                    <!-- Mock Payment Section -->
                    <div class="card" style="padding: 2rem;">
                        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                            <i class="fa-solid fa-credit-card" style="color: var(--accent);"></i> Select Mock Payment Method
                        </h2>

                        <div style="display: flex; flex-direction: column; gap: 1rem; margin-bottom: 1.5rem;">
                            <label class="card" style="display: flex; align-items: center; gap: 1rem; cursor: pointer; padding: 1rem;">
                                <input type="radio" name="paymentMethod" value="Mock Card" checked>
                                <div>
                                    <strong style="display: block; font-size: 0.95rem;">Mock Credit / Debit Card</strong>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);">Instant confirmation simulation (Visa, Mastercard, RuPay)</span>
                                </div>
                            </label>

                            <label class="card" style="display: flex; align-items: center; gap: 1rem; cursor: pointer; padding: 1rem;">
                                <input type="radio" name="paymentMethod" value="Mock UPI">
                                <div>
                                    <strong style="display: block; font-size: 0.95rem;">Mock UPI Payment</strong>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);">Simulated Google Pay / PhonePe / Paytm checkout</span>
                                </div>
                            </label>

                            <label class="card" style="display: flex; align-items: center; gap: 1rem; cursor: pointer; padding: 1rem;">
                                <input type="radio" name="paymentMethod" value="Cash on Delivery">
                                <div>
                                    <strong style="display: block; font-size: 0.95rem;">Cash on Delivery (COD)</strong>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);">Pay cash upon physical delivery</span>
                                </div>
                            </label>
                        </div>
                    </div>
                </div>

                <!-- Right Column: Order Items Summary & Confirm Button -->
                <div>
                    <div class="card" style="align-self: start; padding: 1.5rem; position: sticky; top: 90px;">
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                            Order Breakdown (${cartItems.size()} items)
                        </h3>

                        <div style="display: flex; flex-direction: column; gap: 1rem; margin-bottom: 1.5rem; max-height: 260px; overflow-y: auto; padding-right: 0.5rem;">
                            <c:forEach var="item" items="${cartItems}">
                                <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.88rem;">
                                    <div style="max-width: 200px;">
                                        <div style="font-weight: 600; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">${item.productName}</div>
                                        <div style="font-size: 0.78rem; color: var(--text-muted);">Qty: ${item.quantity} &times; &#8377;<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></div>
                                    </div>
                                    <div style="font-weight: 700;">&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></div>
                                </div>
                            </c:forEach>
                        </div>

                        <div style="border-top: 1px solid var(--border); padding-top: 1rem; margin-bottom: 1.5rem;">
                            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.95rem;">
                                <span style="color: var(--text-muted);">Subtotal</span>
                                <span style="font-weight: 600;">&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                            </div>
                            <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.95rem;">
                                <span style="color: var(--text-muted);">Shipping</span>
                                <span style="color: var(--success); font-weight: 600;">FREE</span>
                            </div>
                            <div style="display: flex; justify-content: space-between; font-size: 1.25rem; font-weight: 800; color: var(--primary);">
                                <span>Grand Total</span>
                                <span>&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-lg btn-primary" style="width: 100%;">
                            <i class="fa-solid fa-lock"></i> Pay & Confirm Order
                        </button>
                    </div>
                </div>

            </div>
        </form>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-bottom">
            <span>&copy; 2026 AmeerRasik Mart. All rights reserved.</span>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>
