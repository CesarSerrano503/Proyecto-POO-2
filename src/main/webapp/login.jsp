<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 29/5/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>Login - MultiGroup</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 2rem; }
        .error { color: red; margin-bottom: 1rem; }
        .success { color: green; margin-bottom: 1rem; }
        form { max-width: 300px; margin: auto; }
        label { display: block; margin-top: 1rem; }
        input { width: 100%; padding: 0.5rem; margin-top: 0.25rem; }
        button { margin-top: 1.5rem; padding: 0.5rem 1rem; }
    </style>
</head>
<body>
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
</body>
</html>
