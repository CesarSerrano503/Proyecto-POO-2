<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clientes.css">
</head>
<body>
<h2>Clientes registrados</h2>

<a href="${pageContext.request.contextPath}/clientes/nuevo" class="button">
    + Nuevo Cliente
</a>

<table class="table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Documento</th>
        <th>Tipo</th>
        <th>Teléfono</th>
        <th>Correo</th>
        <th>Dirección</th>
        <th>Estado</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="cliente" items="${clientes}">
        <tr>
            <td>${cliente.id}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.documento}</td>
            <td>${cliente.tipoPersona}</td>
            <td>${cliente.telefono}</td>
            <td>${cliente.correo}</td>
            <td>${cliente.direccion}</td>
            <td>${cliente.estado}</td>
            <td>
                <a href="${pageContext.request.contextPath}/clientes/editar?id=${cliente.id}"
                   class="button">Editar</a>
                &nbsp;
                <a href="${pageContext.request.contextPath}/clientes/eliminar?id=${cliente.id}"
                   class="delete" onclick="return confirm('¿Eliminar este cliente?');">
                    Eliminar
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
