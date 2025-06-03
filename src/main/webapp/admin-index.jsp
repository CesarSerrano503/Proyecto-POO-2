<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 27/5/2025
  Modificado para administrador: 29/5/2025
  Archivo: admin-index.jsp
  Función: Panel de control para usuarios con rol "admin"; muestra enlaces a módulos, incluido el módulo Usuarios exclusivo.
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>MultiGroup - Administrador</title>
    <%-- Vincula la misma hoja de estilos base utilizada en index.jsp --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <style>
        <%-- Contenedor para agrupar las tarjetas de módulos en un diseño tipo grid flexible --%>
        .grid-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: center;
        }
        <%-- Estilos para cada tarjeta individual --%>
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
        <%-- Título principal para el panel administrativo --%>
        <h1>Panel Administrativo - MultiGroup</h1>
        <%-- Mensaje de bienvenida, mostrando el nombre de usuario almacenado en sesión --%>
        <h3>Bienvenido, ${sessionScope.usuario.username}</h3>
        <div class="grid-cards">

            <%-- Módulo Usuarios: solo visible en panel admin --%>
            <a class="card" href="${pageContext.request.contextPath}/usuarios">
                <h2>Módulo Usuarios</h2>
            </a>

            <%-- Módulo Clientes --%>
            <a class="card" href="${pageContext.request.contextPath}/clientes">
                <h2>Módulo Clientes</h2>
            </a>

            <%-- Módulo Empleados --%>
            <a class="card" href="${pageContext.request.contextPath}/empleados">
                <h2>Módulo Empleados</h2>
            </a>

            <%-- Módulo Cotizaciones --%>
            <a class="card" href="${pageContext.request.contextPath}/cotizaciones?action=list">
                <h2>Módulo Cotizaciones</h2>
            </a>

            <%-- Módulo Asignaciones: redirige al listado de cotizaciones para elegir sobre cuál trabajar --%>
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Asignaciones</h2>
            </div>

            <%-- Módulo Subtareas: igual que Asignaciones, primero seleccionar cotización, luego asignación --%>
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Subtareas</h2>
            </div>

        </div>
        <%-- Enlace para cerrar la sesión y volver al login --%>
        <a href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
    </div>
</main>
</body>
</html>
