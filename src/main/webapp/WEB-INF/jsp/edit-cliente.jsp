<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-form.css"/>
</head>
<body>
<div class="container">
    <h2>Editar Cliente</h2>

    <!-- Errores de validación -->
    <c:if test="${not empty errors}">
        <div class="alert">
            <ul>
                <c:forEach var="err" items="${errors}">
                    <li>${err}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post" class="form">
        <input type="hidden" name="idCliente" value="${cliente.idCliente}" />

        <!-- Nombre -->
        <label>Nombre:
            <input type="text" name="nombre"
                   value="${cliente.nombre}"
                   required maxlength="255"/>
        </label>

        <!-- Documento -->
        <label>Documento:
            <input type="text" name="documento"
                   value="${cliente.documento}"
                   required maxlength="50"/>
        </label>

        <!-- Tipo Persona -->
        <label>Tipo Persona:
            <select name="tipoPersona" required>
                <option value="">--Selecciona--</option>
                <option value="Natural" ${cliente.tipoPersona=='Natural'?'selected':''}>Natural</option>
                <option value="Jurídica" ${cliente.tipoPersona=='Jurídica'?'selected':''}>Jurídica</option>
            </select>
        </label>

        <!-- Teléfono -->
        <label>Teléfono:
            <input type="text" name="telefono"
                   value="${cliente.telefono}"
                   required maxlength="20"/>
        </label>

        <!-- Correo -->
        <label>Correo:
            <input type="email" name="correo"
                   value="${cliente.correo}"
                   required maxlength="150"/>
        </label>

        <!-- Dirección -->
        <label>Dirección:
            <input type="text" name="direccion"
                   value="${cliente.direccion}"
                   required maxlength="255"/>
        </label>

        <!-- Estado -->
        <label>Estado:
            <select name="estado" required>
                <option value="Activo"  ${cliente.estado=='Activo'?'selected':''}>Activo</option>
                <option value="Inactivo"${cliente.estado=='Inactivo'?'selected':''}>Inactivo</option>
            </select>
        </label>

        <!-- Creado Por -->
        <label>Creado Por:
            <input type="text" name="creadoPor"
                   value="${cliente.creadoPor}"
                   required maxlength="100"/>
        </label>

        <div class="form-actions">
            <button type="submit" class="button">Actualizar</button>
            <button type="button" class="button cancel"
                    onclick="location.href='${pageContext.request.contextPath}/clientes?action=list';">
                Cancelar
            </button>
        </div>
    </form>
</div>
</body>
</html>
