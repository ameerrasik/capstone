<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-user-plus"></i> Register</a>
        </div>
    </nav>

    <div class="container" style="display: flex; align-items: center; justify-content: center; min-height: 80vh;">
        <div class="card" style="width: 100%; max-width: 440px; padding: 2.5rem;">
            <div style="text-align: center; margin-bottom: 2rem;">
                <div style="width: 56px; height: 56px; background: var(--secondary); color: #FFF; border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem; font-size: 1.5rem;">
                    <i class="fa-solid fa-right-to-bracket"></i>
                </div>
                <h2 style="font-size: 1.5rem; font-weight: 700;">Welcome Back</h2>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Sign in to your AmeerRasik Mart account</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    <i class="fa-solid fa-circle-check"></i> ${successMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm">
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="name@example.com" value="${email}" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Enter password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-lg" style="width: 100%; margin-top: 0.5rem;">
                    <i class="fa-solid fa-lock"></i> Sign In
                </button>
            </form>

            <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid var(--border); text-align: center;">
                <p style="font-size: 0.85rem; font-weight: 600; color: var(--text-muted); margin-bottom: 0.75rem;">QUICK DEMO ACCOUNTS</p>
                <div style="display: flex; gap: 0.5rem; justify-content: center; flex-wrap: wrap;">
                    <button type="button" class="btn btn-sm btn-outline" onclick="fillDemo('buyer@ameerrasikmart.com', 'Buyer@123')">
                        Buyer
                    </button>
                    <button type="button" class="btn btn-sm btn-outline" onclick="fillDemo('techstore@ameerrasikmart.com', 'Seller@123')">
                        Seller
                    </button>
                    <button type="button" class="btn btn-sm btn-outline" onclick="fillDemo('admin@ameerrasikmart.com', 'Admin@123')">
                        Admin
                    </button>
                </div>
            </div>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem;">
                Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" style="font-weight: 600;">Create One</a>
            </div>
        </div>
    </div>

    <script>
        function fillDemo(email, pass) {
            document.getElementById('email').value = email;
            document.getElementById('password').value = pass;
        }
    </script>
</body>
</html>
