<%-- login.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>Login - MultiGroup</title>
    <!-- Enlaza tu nuevo CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
</head>
<body>
<div class="login-container">
    <h2>Acceso al Sistema</h2>

    <!-- Mostrar mensaje de error si existe -->
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <!-- Mostrar mensaje de logout si viene como parámetro -->
    <c:if test="${param.logout}">
        <div class="success">Has cerrado sesión exitosamente.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Usuario:</label>
        <input type="text" id="username" name="username" required autofocus/>

        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required/>

        <button type="submit">Ingresar</button>
    </form>
</div>
</body>
</html>
