<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>500 Server Error - AmeerRasik Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">
            <i class="fa-solid fa-bag-shopping" style="color: var(--accent);"></i>
            AmeerRasik<span style="color: var(--accent);">Mart</span>
        </a>
    </nav>

    <div class="container" style="display: flex; align-items: center; justify-content: center; min-height: 75vh;">
        <div class="card" style="text-align: center; padding: 4rem 2rem; max-width: 520px; width: 100%;">
            <div style="font-size: 4rem; color: var(--danger); margin-bottom: 1rem;">
                <i class="fa-solid fa-triangle-exclamation"></i>
            </div>
            <h1 style="font-size: 1.6rem; font-weight: 800; margin-bottom: 0.5rem;">Internal Server Error</h1>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">
                Something went wrong while processing your request. Our system administrators have been notified.
            </p>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary btn-lg">
                <i class="fa-solid fa-house"></i> Go to Homepage
            </a>
        </div>
    </div>
</body>
</html>
