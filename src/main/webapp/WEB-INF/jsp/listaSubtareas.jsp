<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title>Subtareas de Asignación ${idAsignacion}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
</head>
<body>
<div class="container">
  <h2>Subtareas de Asignación ${idAsignacion}</h2>
  <a href="${pageContext.request.contextPath}/asignaciones?action=list&idCotizacion=${idCotizacion}" class="button cancel">Volver a Asignaciones</a>
  <c:if test="${not empty sessionScope.mensaje}">
    <div class="alert">${sessionScope.mensaje}</div>
    <c:remove var="mensaje" scope="session"/>
  </c:if>
  <a href="${pageContext.request.contextPath}/subtareas?action=new&idAsignacion=${idAsignacion}" class="button">Nueva Subtarea</a>
  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Título</th>
      <th>Descripción</th>
      <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="s" items="${listaSub}">
      <tr>
        <td>${s.idSubtarea}</td>
        <td>${s.tituloSubtarea}</td>
        <td>${s.descripcionSubtarea}</td>
        <td class="actions">
          <a href="${pageContext.request.contextPath}/subtareas?action=edit&amp;idAsignacion=${idAsignacion}&amp;idSubtarea=${s.idSubtarea}">Editar</a> |
          <a href="${pageContext.request.contextPath}/subtareas?action=remove&amp;idAsignacion=${idAsignacion}&amp;idSubtarea=${s.idSubtarea}" onclick="return confirm('¿Eliminar esta subtarea?');">Eliminar</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
