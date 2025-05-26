<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Clientes</title>
</head>
<body>
<h2>Clientes registrados</h2>
<table border="1" cellpadding="5">
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
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
