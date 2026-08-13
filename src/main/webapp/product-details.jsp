<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-arrow-left"></i> Back to Products</a>
        </div>
    </nav>

    <div class="container">
        <!-- Main Product Summary Card -->
        <div class="card" style="margin-bottom: 2rem; padding: 2.5rem;">
            <div style="display: grid; grid-template-columns: 420px 1fr; gap: 3rem;">
                
                <div style="width: 100%; height: 380px; background: var(--surface-alt); border-radius: var(--radius-md); overflow: hidden;">
                    <img src="${product.imageUrl}" alt="${product.name}" style="width: 100%; height: 100%; object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80'">
                </div>

                <div>
                    <span class="badge badge-info" style="margin-bottom: 0.75rem;">${product.category}</span>
                    <h1 style="font-size: 2rem; font-weight: 800; margin-bottom: 0.75rem; line-height: 1.2;">${product.name}</h1>
                    
                    <div style="display: flex; align-items: center; gap: 1.5rem; margin-bottom: 1.5rem; font-size: 0.9rem;">
                        <span style="color: var(--text-muted);">Seller: <strong style="color: var(--text);">${product.sellerName}</strong></span>
                        <span style="color: var(--warning); font-weight: 700;">
                            <i class="fa-solid fa-star"></i> <fmt:formatNumber value="${product.averageRating}" pattern="0.0"/> / 5.0
                        </span>
                    </div>

                    <div style="font-size: 2.2rem; font-weight: 800; color: var(--primary); margin-bottom: 1.5rem;">
                        &#8377;<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                    </div>

                    <p style="color: var(--text-muted); line-height: 1.7; margin-bottom: 2rem; white-space: pre-line;">
                        ${product.description}
                    </p>

                    <div style="margin-bottom: 2rem;">
                        <c:choose>
                            <c:when test="${product.isInStock()}">
                                <span class="badge badge-success" style="font-size: 0.85rem;"><i class="fa-solid fa-check"></i> In Stock (${product.stockQty} units remaining)</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger" style="font-size: 0.85rem;"><i class="fa-solid fa-xmark"></i> Out of Stock</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${product.isInStock()}">
                        <form action="${pageContext.request.contextPath}/cart" method="POST" style="display: flex; gap: 1rem; align-items: center; max-width: 380px;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.id}">
                            
                            <div style="width: 100px;">
                                <label class="form-label">Quantity</label>
                                <input type="number" name="quantity" value="1" min="1" max="${product.stockQty}" class="form-control" style="text-align: center;">
                            </div>

                            <button type="submit" class="btn btn-lg btn-primary" style="flex: 1; margin-top: 1.5rem;">
                                <i class="fa-solid fa-cart-plus"></i> Add to Cart
                            </button>
                        </form>
                    </c:if>
                </div>

            </div>
        </div>

        <!-- Customer Reviews Section -->
        <div class="card" style="padding: 2rem;">
            <h2 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 1.5rem;">Customer Reviews (${reviews.size()})</h2>

            <c:if test="${not empty sessionScope.user && sessionScope.user.isBuyer()}">
                <div style="background: var(--surface-alt); border-radius: var(--radius-sm); padding: 1.5rem; margin-bottom: 2rem;">
                    <h3 style="font-size: 1.05rem; font-weight: 600; margin-bottom: 1rem;">Write a Product Review</h3>
                    <form action="${pageContext.request.contextPath}/review" method="POST">
                        <input type="hidden" name="productId" value="${product.id}">
                        
                        <div class="form-group" style="max-width: 200px;">
                            <label class="form-label">Rating (1 to 5 Stars)</label>
                            <select name="rating" class="form-control" required>
                                <option value="5">5 Stars - Excellent</option>
                                <option value="4">4 Stars - Very Good</option>
                                <option value="3">3 Stars - Average</option>
                                <option value="2">2 Stars - Below Average</option>
                                <option value="1">1 Star - Poor</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Your Review Comment</label>
                            <textarea name="comment" class="form-control" rows="3" placeholder="Share details of your experience with this product..." required></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary btn-sm"><i class="fa-solid fa-paper-plane"></i> Submit Review</button>
                    </form>
                </div>
            </c:if>

            <c:if test="${empty reviews}">
                <p style="color: var(--text-muted); font-style: italic;">No reviews submitted for this product yet.</p>
            </c:if>

            <div style="display: flex; flex-direction: column; gap: 1.25rem;">
                <c:forEach var="rev" items="${reviews}">
                    <div style="border-bottom: 1px solid var(--border); padding-bottom: 1rem;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.4rem;">
                            <strong style="font-size: 0.95rem;">${rev.userName}</strong>
                            <span style="color: var(--warning); font-size: 0.85rem; font-weight: 700;">
                                <c:forEach begin="1" end="${rev.rating}"><i class="fa-solid fa-star"></i></c:forEach>
                            </span>
                        </div>
                        <p style="font-size: 0.9rem; color: var(--text);">${rev.comment}</p>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">${rev.createdAt}</span>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-bottom">
            <span>&copy; 2026 AmeerRasik Mart. All rights reserved.</span>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script src="${pageContext.request.contextPath}/js/api.js"></script>
    <script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
