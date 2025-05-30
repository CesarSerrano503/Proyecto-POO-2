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
    <title>Lista de Usuarios</title>
    <style>
        table { width: 80%; margin: auto; border-collapse: collapse; }
        th, td { border: 1px solid #ccc; padding: 0.5rem; text-align: left; }
        th { background: #f0f0f0; }
        a.button { padding: 0.3rem 0.6rem; background: #28a745; color: white; text-decoration: none; border-radius: 4px; }
        a.button.delete { background: #dc3545; }
        .center { text-align: center; margin: 1rem; }
    </style>
</head>
<body>
<h2 class="center">Gestión de Usuarios</h2>
<div class="center">
    <a href="${pageContext.request.contextPath}/usuarios?action=nuevo" class="button">Nuevo Usuario</a>
    &nbsp;&nbsp;
    <a href="${pageContext.request.contextPath}/admin-index.jsp">Volver al Panel</a>
</div>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Usuario</th>
        <th>Rol</th>
        <th>Estado</th>
        <th>Creado Por</th>
        <th>Fecha Creación</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="u" items="${usuarios}">
        <tr>
            <td>${u.idUsuario}</td>
            <td>${u.username}</td>
            <td>${u.rol}</td>
            <td>${u.estado}</td>
            <td>${u.creadoPor}</td>
            <td>${u.fechaCreacion}</td>
            <td>
                <a href="${pageContext.request.contextPath}/usuarios?action=edit&id=${u.idUsuario}" class="button">Editar</a>
                <a href="${pageContext.request.contextPath}/usuarios?action=delete&id=${u.idUsuario}"
                   class="button delete"
                   onclick="return confirm('¿Seguro que desea inactivar este usuario?');">
                    Inactivar
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
