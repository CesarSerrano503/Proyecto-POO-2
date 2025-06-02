<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title>
    <c:choose>
      <c:when test="${not empty usuarioObj}">Editar Usuario</c:when>
      <c:otherwise>Nuevo Usuario</c:otherwise>
    </c:choose>
  </title>

  <!-- CSS global (contenedor, alertas, botones generales) -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
  <!-- CSS específico para formulario de usuario (usuario-form.css) -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuario-form.css"/>
</head>
<body>
<div class="container">
  <h2>
    <c:choose>
      <c:when test="${not empty usuarioObj}">Editar Usuario</c:when>
      <c:otherwise>Nuevo Usuario</c:otherwise>
    </c:choose>
  </h2>

  <!-- Mostrar mensaje de validación si existe -->
  <c:if test="${not empty error}">
    <div class="alert">
      <ul>
        <li>${error}</li>
      </ul>
    </div>
  </c:if>

  <form class="form" action="${pageContext.request.contextPath}/usuarios?action=save" method="post">
    <!-- Campo oculto con ID solo en edición -->
    <c:if test="${not empty usuarioObj}">
      <input type="hidden" name="id" value="${usuarioObj.idUsuario}" />
    </c:if>

    <!-- Campo Usuario -->
    <label>
      <span>Usuario:</span>
      <input type="text"
             name="username"
             required
             minlength="4"
             value="${usuarioObj.username}" />
    </label>

    <!-- Campo Contraseña -->
    <label>
      <span>Contraseña:</span>
      <input type="password"
             name="password"
             <c:if test="${empty usuarioObj}">required minlength="6"</c:if> />
      <c:if test="${not empty usuarioObj}">
        <small>(Déjalo en blanco para conservar la actual)</small>
      </c:if>
    </label>

    <!-- Campo Rol -->
    <label>
      <span>Rol:</span>
      <select name="rol" required>
        <option value="usuario" <c:if test="${usuarioObj.rol == 'usuario'}">selected</c:if>>
          Colaborador
        </option>
        <option value="admin" <c:if test="${usuarioObj.rol == 'admin'}">selected</c:if>>
          Administrador
        </option>
      </select>
    </label>

    <!-- Campo Estado (checkbox) -->
    <div class="checkbox-group">
      <input type="checkbox"
             name="estado"
             id="estado"
             <c:if test="${usuarioObj.estado}">checked</c:if> />
      <label for="estado">Activo</label>
    </div>

    <!-- Botones Guardar y Cancelar -->
    <div class="form-actions">
      <button type="submit" class="save">Guardar</button>
      <button type="button" class="cancel"
              onclick="window.location='${pageContext.request.contextPath}/usuarios?action=list';">
        Cancelar
      </button>
    </div>
  </form>

  <!-- Enlace para volver al inicio -->
  <div class="back-link mt-20">
    <a href="${pageContext.request.contextPath}/home">Volver al inicio</a>
  </div>
</div>
</body>
</html>
