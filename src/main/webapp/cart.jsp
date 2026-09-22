<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-store"></i> Continue Shopping</a>
        </div>
    </nav>

    <div class="container">
        <h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 1.5rem;">Shopping Cart</h1>

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
            <c:when test="${empty cartItems}">
                <div class="card" style="text-align: center; padding: 4rem 2rem;">
                    <div style="font-size: 3.5rem; color: var(--text-muted); margin-bottom: 1rem;">
                        <i class="fa-solid fa-cart-arrow-down"></i>
                    </div>
                    <h2 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 0.5rem;">Your Cart is Empty</h2>
                    <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Explore our marketplace and add items to your cart.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">Browse Products</a>
                </div>
            </c:when>

            <c:otherwise>
                <div style="display: grid; grid-template-columns: 1fr 340px; gap: 2rem;">
                    
                    <!-- Cart Items Table -->
                    <div class="card" style="padding: 1.5rem;">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <div style="display: flex; align-items: center; gap: 1rem;">
                                                    <img src="${item.imageUrl}" alt="${item.productName}" style="width: 54px; height: 54px; object-fit: cover; border-radius: var(--radius-sm);" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80'">
                                                    <div>
                                                        <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" style="font-weight: 600; color: var(--text);">${item.productName}</a>
                                                        <div style="font-size: 0.78rem; color: var(--text-muted);">${item.category}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="font-weight: 600;">&#8377;<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="POST" style="display: flex; align-items: center; gap: 0.4rem;">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.availableStock}" class="form-control" style="width: 70px; text-align: center; padding: 0.35rem;">
                                                    <button type="submit" class="btn btn-sm btn-outline" title="Update"><i class="fa-solid fa-rotate"></i></button>
                                                </form>
                                            </td>
                                            <td style="font-weight: 700; color: var(--primary);">&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="POST">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-sm btn-danger" title="Remove"><i class="fa-solid fa-trash"></i></button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Cart Summary Sidebar -->
                    <div class="card" style="align-self: start; padding: 1.5rem;">
                        <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">Order Summary</h3>
                        
                        <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.95rem;">
                            <span style="color: var(--text-muted);">Subtotal</span>
                            <span style="font-weight: 600;">&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                        </div>
                        <div style="display: flex; justify-content: space-between; margin-bottom: 1.25rem; font-size: 0.95rem;">
                            <span style="color: var(--text-muted);">Shipping</span>
                            <span style="color: var(--success); font-weight: 600;">FREE</span>
                        </div>

                        <div style="display: flex; justify-content: space-between; margin-bottom: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border); font-size: 1.2rem; font-weight: 800; color: var(--primary);">
                            <span>Total</span>
                            <span>&#8377;<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-lg btn-primary" style="width: 100%;">
                            Proceed to Checkout <i class="fa-solid fa-arrow-right"></i>
                        </a>
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

    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>
