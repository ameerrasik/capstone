<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Portal Dashboard - AmeerRasik Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <i class="fa-solid fa-bag-shopping" style="color: var(--accent);"></i>
            AmeerRasik<span style="color: var(--accent);">Mart</span>
            <span class="brand-badge" style="background: var(--secondary);">Seller</span>
        </a>
        <div class="nav-menu">
            <span style="color: #94A3B8; font-size: 0.85rem;">Seller: <strong>${sessionScope.user.name}</strong></span>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-store"></i> View Storefront</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>
        </div>
    </nav>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <div>
                <h1 style="font-size: 1.8rem; font-weight: 800;">Seller Dashboard</h1>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Manage your inventory, product listings, and incoming customer orders</p>
            </div>
            <button class="btn btn-primary" onclick="toggleAddProductForm()">
                <i class="fa-solid fa-plus-circle"></i> Add New Product
            </button>
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

        <!-- Metric Stat Cards -->
        <div class="grid grid-cols-3" style="margin-bottom: 2.5rem;">
            <div class="card" style="display: flex; align-items: center; gap: 1.25rem;">
                <div style="width: 54px; height: 54px; background: #EEF2FF; color: var(--secondary); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.5rem;">
                    <i class="fa-solid fa-indian-rupee-sign"></i>
                </div>
                <div>
                    <span style="font-size: 0.82rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Total Sales Revenue</span>
                    <h3 style="font-size: 1.5rem; font-weight: 800; color: var(--primary);">&#8377;<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/></h3>
                </div>
            </div>

            <div class="card" style="display: flex; align-items: center; gap: 1.25rem;">
                <div style="width: 54px; height: 54px; background: #ECFEFF; color: var(--accent); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.5rem;">
                    <i class="fa-solid fa-box"></i>
                </div>
                <div>
                    <span style="font-size: 0.82rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Active Listed Products</span>
                    <h3 style="font-size: 1.5rem; font-weight: 800; color: var(--primary);">${totalProductsCount} Items</h3>
                </div>
            </div>

            <div class="card" style="display: flex; align-items: center; gap: 1.25rem;">
                <div style="width: 54px; height: 54px; background: #F0FDF4; color: var(--success); border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; font-size: 1.5rem;">
                    <i class="fa-solid fa-truck-ramp-box"></i>
                </div>
                <div>
                    <span style="font-size: 0.82rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase;">Incoming Orders</span>
                    <h3 style="font-size: 1.5rem; font-weight: 800; color: var(--primary);">${totalOrdersCount} Orders</h3>
                </div>
            </div>
        </div>

        <!-- Add New Product Collapsible Card -->
        <div class="card" id="addProductCard" style="display: none; margin-bottom: 2.5rem; padding: 2rem; background: #F8FAFC; border: 2px dashed var(--secondary);">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h3 style="font-size: 1.25rem; font-weight: 700;"><i class="fa-solid fa-box-archive"></i> Add New Product Listing</h3>
                <button type="button" class="btn btn-sm btn-outline" onclick="toggleAddProductForm()"><i class="fa-solid fa-xmark"></i> Cancel</button>
            </div>

            <form action="${pageContext.request.contextPath}/seller/product/create" method="POST">
                <div class="grid grid-cols-2">
                    <div class="form-group">
                        <label class="form-label">Product Name</label>
                        <input type="text" name="name" class="form-control" placeholder="e.g. Ergonomic Wireless Mouse" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Category</label>
                        <input type="text" name="category" class="form-control" placeholder="e.g. Electronics" required list="categoryList">
                        <datalist id="categoryList">
                            <option value="Electronics"></option>
                            <option value="Fashion"></option>
                            <option value="Accessories"></option>
                            <option value="Home"></option>
                            <option value="Books"></option>
                        </datalist>
                    </div>
                </div>

                <div class="grid grid-cols-3">
                    <div class="form-group">
                        <label class="form-label">Price (&#8377; INR)</label>
                        <input type="number" step="0.01" name="price" class="form-control" placeholder="1299.00" required min="0.01">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Available Stock Qty</label>
                        <input type="number" name="stockQty" class="form-control" placeholder="50" required min="0">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Image URL</label>
                        <input type="url" name="imageUrl" class="form-control" placeholder="https://images.unsplash.com/...">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Product Description</label>
                    <textarea name="description" class="form-control" rows="3" placeholder="Provide detailed product specifications and features..."></textarea>
                </div>

                <button type="submit" class="btn btn-primary btn-lg"><i class="fa-solid fa-cloud-arrow-up"></i> Publish Product</button>
            </form>
        </div>

        <!-- Listed Products Table -->
        <div class="card" style="margin-bottom: 2.5rem; padding: 1.5rem;">
            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                My Listed Products (${products.size()})
            </h3>

            <c:choose>
                <c:when test="${empty products}">
                    <p style="color: var(--text-muted); text-align: center; padding: 2rem;">No products listed yet. Click 'Add New Product' to create your first listing.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Image</th>
                                    <th>Name</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Stock Qty</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${products}">
                                    <tr>
                                        <td>
                                            <img src="${p.imageUrl}" alt="${p.name}" style="width: 48px; height: 48px; object-fit: cover; border-radius: var(--radius-sm);" onerror="this.src='https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80'">
                                        </td>
                                        <td>
                                            <strong>${p.name}</strong>
                                        </td>
                                        <td><span class="badge badge-info">${p.category}</span></td>
                                        <td style="font-weight: 700; color: var(--primary);">&#8377;<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${p.stockQty > 5}">
                                                    <span class="badge badge-success">${p.stockQty} in stock</span>
                                                </c:when>
                                                <c:when test="${p.stockQty > 0}">
                                                    <span class="badge badge-warning">Low stock (${p.stockQty})</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">Out of Stock</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/seller/product/delete" method="POST" onsubmit="return confirm('Are you sure you want to delete this product?');" style="display: inline-block;">
                                                <input type="hidden" name="id" value="${p.id}">
                                                <button type="submit" class="btn btn-sm btn-danger"><i class="fa-solid fa-trash"></i> Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Incoming Orders Table -->
        <div class="card" style="padding: 1.5rem;">
            <h3 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                Incoming Customer Orders (${orders.size()})
            </h3>

            <c:choose>
                <c:when test="${empty orders}">
                    <p style="color: var(--text-muted); text-align: center; padding: 2rem;">No incoming customer orders yet.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Buyer Name</th>
                                    <th>Products Ordered</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                    <th>Update Fulfillment Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${orders}">
                                    <tr>
                                        <td><strong>#ARM-${o.id}</strong></td>
                                        <td>${o.buyerName}</td>
                                        <td>
                                            <c:forEach var="item" items="${o.items}">
                                                <div style="font-size: 0.85rem;">&bull; ${item.productName} (x${item.quantity})</div>
                                            </c:forEach>
                                        </td>
                                        <td style="font-size: 0.85rem; color: var(--text-muted);">${o.createdAt}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${o.status == 'CONFIRMED'}"><span class="badge badge-success">Confirmed</span></c:when>
                                                <c:when test="${o.status == 'SHIPPED'}"><span class="badge badge-info">Shipped</span></c:when>
                                                <c:when test="${o.status == 'DELIVERED'}"><span class="badge badge-success">Delivered</span></c:when>
                                                <c:otherwise><span class="badge badge-warning">${o.status}</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/seller/order/update-status" method="POST" style="display: flex; gap: 0.4rem;">
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
                </c:otherwise>
            </c:choose>
        </div>

    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-bottom">
            <span>&copy; 2026 AmeerRasik Mart. All rights reserved.</span>
        </div>
    </footer>

    <script>
        function toggleAddProductForm() {
            const form = document.getElementById('addProductCard');
            if (form.style.display === 'none' || form.style.display === '') {
                form.style.display = 'block';
                form.scrollIntoView({ behavior: 'smooth' });
            } else {
                form.style.display = 'none';
            }
        }
    </script>
</body>
</html>
