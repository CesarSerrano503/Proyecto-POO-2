<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>MultiGroup - Módulos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
</head>
<body>
<main>
    <div class="container">
        <h1>Guía de Módulos</h1>
        <div class="grid-cards">
            <a class="card" href="${pageContext.request.contextPath}/clientes">
                <h2>Módulo Clientes</h2>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/empleados">
                <h2>Módulo Empleados</h2>
            </a>
            <a class="card" href="${pageContext.request.contextPath}/cotizaciones">
                <h2>Módulo Cotizaciones</h2>
            </a>
        </div>
    </div>
</main>
</body>
</html>
