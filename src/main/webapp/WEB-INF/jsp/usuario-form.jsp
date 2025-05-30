<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title><c:choose>
    <c:when test="${not empty usuario}">Editar Usuario</c:when>
    <c:otherwise>Nuevo Usuario</c:otherwise>
  </c:choose></title>
  <style>
    form { width: 300px; margin: auto; }
    label { display: block; margin-top: 1rem; }
    input, select { width: 100%; padding: 0.4rem; margin-top: 0.2rem; }
    button { margin-top: 1rem; padding: 0.5rem 1rem; }
    .center { text-align: center; margin-top: 1rem; }
  </style>
</head>
<body>
<h2 class="center">
  <c:choose>
    <c:when test="${not empty usuario}">Editar Usuario</c:when>
    <c:otherwise>Nuevo Usuario</c:otherwise>
  </c:choose>
</h2>
<form action="${pageContext.request.contextPath}/usuarios" method="post">
  <c:if test="${not empty usuario}">
    <input type="hidden" name="action" value="update"/>
    <input type="hidden" name="id" value="${usuario.idUsuario}"/>
  </c:if>
  <c:if test="${empty usuario}">
    <input type="hidden" name="action" value="insert"/>
  </c:if>

  <label for="username">Usuario:</label>
  <input id="username" type="text" name="username"
         value="${usuario.username}" required/>

  <label for="password">Contraseña:</label>
  <input id="password" type="text" name="password"
         value="${usuario.password}" required/>

  <label for="rol">Rol:</label>
  <select id="rol" name="rol" required>
    <option value="">-- Seleccione --</option>
    <option value="admin"      ${usuario.rol=='admin' ? 'selected' : ''}>Administrador</option>
    <option value="colaborador"${usuario.rol=='colaborador' ? 'selected' : ''}>Colaborador</option>
  </select>

  <label for="estado">Estado:</label>
  <select id="estado" name="estado" required>
    <option value="">-- Seleccione --</option>
    <option value="activo"   ${usuario.estado=='activo' ? 'selected' : ''}>Activo</option>
    <option value="inactivo" ${usuario.estado=='inactivo' ? 'selected' : ''}>Inactivo</option>
  </select>

  <div class="center">
    <button type="submit">Guardar</button>
    &nbsp;
    <a href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
  </div>
</form>
</body>
</html>
