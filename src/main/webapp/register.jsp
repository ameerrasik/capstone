<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - AmeerRasik Mart</title>
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
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-sm btn-outline" style="color: #FFF;"><i class="fa-solid fa-right-to-bracket"></i> Login</a>
        </div>
    </nav>

    <div class="container" style="display: flex; align-items: center; justify-content: center; min-height: 85vh; padding: 2rem 1rem;">
        <div class="card" style="width: 100%; max-width: 480px; padding: 2.5rem;">
            <div style="text-align: center; margin-bottom: 2rem;">
                <div style="width: 56px; height: 56px; background: var(--accent); color: #FFF; border-radius: var(--radius-full); display: flex; align-items: center; justify-content: center; margin: 0 auto 1rem; font-size: 1.5rem;">
                    <i class="fa-solid fa-user-plus"></i>
                </div>
                <h2 style="font-size: 1.5rem; font-weight: 700;">Create Account</h2>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Join AmeerRasik Mart as a Buyer or Seller</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST" onsubmit="return Validation.validateRegistrationForm(this);">
                <div class="form-group">
                    <label class="form-label" for="name">Full Name</label>
                    <input type="text" id="name" name="name" class="form-control" placeholder="e.g. Ramesh Kumar" value="${name}" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="name@example.com" value="${email}" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="role">Account Type</label>
                    <select id="role" name="role" class="form-control" required>
                        <option value="BUYER" ${role == 'BUYER' ? 'selected' : ''}>Buyer (Purchase products)</option>
                        <option value="SELLER" ${role == 'SELLER' ? 'selected' : ''}>Seller (List & sell products)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Min. 6 characters" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Re-enter password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-lg" style="width: 100%; margin-top: 0.5rem;">
                    <i class="fa-solid fa-check-circle"></i> Create Account
                </button>
            </form>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem;">
                Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" style="font-weight: 600;">Sign In</a>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
