<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 27git add ./5/2025
  Modificado para administrador: 29/5/2025
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>MultiGroup - Administrador</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <style>
        .grid-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: center;
        }
        .card {
            position: relative;
            width: 200px;
            padding: 1.5rem;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-align: center;
            cursor: pointer;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .card h2 {
            margin: 0;
            color: #28a745;
            font-size: 1.2rem;
        }
    </style>
</head>
<body>
<main>
    <div class="container">
        <h1>Panel Administrativo - MultiGroup</h1>
        <h3>Bienvenido, ${sessionScope.usuario.username}</h3>
        <div class="grid-cards">

            <!-- Módulo Usuarios (exclusivo Admin) -->
            <a class="card" href="${pageContext.request.contextPath}/usuarios">
                <h2>Módulo Usuarios</h2>
            </a>

            <!-- Clientes -->
            <a class="card" href="${pageContext.request.contextPath}/clientes">
                <h2>Módulo Clientes</h2>
            </a>

            <!-- Empleados -->
            <a class="card" href="${pageContext.request.contextPath}/empleados">
                <h2>Módulo Empleados</h2>
            </a>

            <!-- Cotizaciones -->
            <a class="card" href="${pageContext.request.contextPath}/cotizaciones?action=list">
                <h2>Módulo Cotizaciones</h2>
            </a>

            <!-- Asignaciones -->
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Asignaciones</h2>
            </div>

            <!-- Subtareas -->
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Subtareas</h2>
            </div>


        </div>
        <a href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
    </div>
</main>
</body>
</html>
