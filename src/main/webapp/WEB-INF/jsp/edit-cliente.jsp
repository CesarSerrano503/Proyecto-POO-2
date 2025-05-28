<%--
  Created by IntelliJ IDEA.
  User: Cesar
  Date: 27/5/2025
  Time: 22:12
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cliente-form.css">
</head>
<body>
<h2>Editar Cliente</h2>
<form action="${pageContext.request.contextPath}/clientes/editar" method="post">
    <!-- ID (oculto) -->
    <input type="hidden" name="id" value="${cliente.id}"/>

    <label>Nombre:<br/>
        <input type="text" name="nombre" required maxlength="255"
               value="${cliente.nombre}"/>
    </label><br/><br/>

    <label>Documento:<br/>
        <input type="text" name="documento" required maxlength="50"
               value="${cliente.documento}"/>
    </label><br/><br/>

    <label>Tipo de Persona:<br/>
        <select name="tipoPersona">
            <option value="Natural" ${cliente.tipoPersona == 'Natural' ? 'selected' : ''}>
                Natural
            </option>
            <option value="Jurídica" ${cliente.tipoPersona == 'Jurídica' ? 'selected' : ''}>
                Jurídica
            </option>
        </select>
    </label><br/><br/>

    <label>Teléfono:<br/>
        <input type="text" name="telefono" maxlength="20"
               value="${cliente.telefono}"/>
    </label><br/><br/>

    <label>Correo:<br/>
        <input type="email" name="correo" maxlength="255"
               value="${cliente.correo}"/>
    </label><br/><br/>

    <label>Dirección:<br/>
        <textarea name="direccion" rows="3" cols="30">${cliente.direccion}</textarea>
    </label><br/><br/>

    <label>Estado:<br/>
        <select name="estado">
            <option value="Activo" ${cliente.estado == 'Activo' ? 'selected' : ''}>
                Activo
            </option>
            <option value="Inactivo" ${cliente.estado == 'Inactivo' ? 'selected' : ''}>
                Inactivo
            </option>
        </select>
    </label><br/><br/>

    <button type="submit">Actualizar Cliente</button>
</form>
<p>
    <a href="${pageContext.request.contextPath}/clientes" class="back-link">
        ← Volver al listado
    </a>
</p>
</body>
</html>
