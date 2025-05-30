<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 27/5/2025
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Editar Empleado</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-form.css" />
</head>
<body>
<div class="container">
  <h2>Editar Empleado</h2>

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

  <form action="${pageContext.request.contextPath}/empleados" method="post" class="form">
    <!-- ID oculto -->
    <input type="hidden" name="idEmpleado" value="${empleado.idEmpleado}" />

    <!-- Nombre -->
    <label>Nombre:
      <input type="text" name="nombre"
             value="${empleado.nombre}"
             required maxlength="255"/>
    </label>

    <!-- Documento -->
    <label>Documento:
      <input type="text" name="documento"
             value="${empleado.documento}"
             required maxlength="50"/>
    </label>

    <!-- Tipo Persona -->
    <label>Tipo Persona:
      <select name="tipoPersona" required>
        <option value="">--Selecciona--</option>
        <option value="Natural"
        ${empleado.tipoPersona=='Natural' ? 'selected':''}>Natural</option>
        <option value="Jurídica"
        ${empleado.tipoPersona=='Jurídica' ? 'selected':''}>Jurídica</option>
      </select>
    </label>

    <!-- Tipo Contratación -->
    <label>Tipo Contratación:
      <select name="tipoContratacion" required>
        <option value="">--Selecciona--</option>
        <option value="Permanente"
        ${empleado.tipoContratacion=='Permanente' ? 'selected':''}>Permanente</option>
        <option value="Por Horas"
        ${empleado.tipoContratacion=='Por Horas' ? 'selected':''}>Por Horas</option>
      </select>
    </label>

    <!-- Teléfono -->
    <label>Teléfono:
      <input type="text" name="telefono"
             value="${empleado.telefono}"
             maxlength="20"/>
    </label>

    <!-- Correo -->
    <label>Correo:
      <input type="email" name="correo"
             value="${empleado.correo}"
             maxlength="150"/>
    </label>

    <!-- Dirección -->
    <label>Dirección:
      <input type="text" name="direccion"
             value="${empleado.direccion}"
             maxlength="255"/>
    </label>

    <!-- Estado -->
    <label>Estado:
      <select name="estado" required>
        <option value="Activo"
        ${empleado.estado=='Activo' ? 'selected':''}>Activo</option>
        <option value="Inactivo"
        ${empleado.estado=='Inactivo' ? 'selected':''}>Inactivo</option>
      </select>
    </label>

    <!-- Creado Por -->
    <label>Creado Por:
      <input type="text" name="creadoPor"
             value="${empleado.creadoPor}"
             required maxlength="100"/>
    </label>

    <div class="form-actions">
      <button type="submit" class="button">Actualizar</button>
      <button type="button" class="button cancel"
              onclick="location.href='${pageContext.request.contextPath}/empleados?action=list';">
        Cancelar
      </button>
    </div>
  </form>
</div>
</body>
</html>
