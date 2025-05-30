<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title>Asignaciones</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
</head>
<body>
<div class="container">
  <h2>Asignaciones de Cotización ${param.idCotizacion}</h2>
  <a href="${pageContext.request.contextPath}/cotizaciones?action=list" class="button cancel">Volver a Cotizaciones</a>
  <c:if test="${not empty sessionScope.mensaje}">
    <div class="alert">${sessionScope.mensaje}</div>
    <c:remove var="mensaje" scope="session"/>
  </c:if>
  <a href="${pageContext.request.contextPath}/asignaciones?action=new&idCotizacion=${param.idCotizacion}" class="button">Nueva Asignación</a>
  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Empleado</th>
      <th>Área</th>
      <th>Horas</th>
      <th>Costo Hora</th>
      <th>Inicio</th>
      <th>Fin</th>
      <th>Total</th>
      <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="a" items="${listaAsig}">
      <tr>
        <td>${a.idAsignacion}</td>
        <td>
          <c:forEach var="emp" items="${empleados}">
            <c:if test="${emp.idEmpleado == a.idEmpleado}">
              ${emp.nombre}
            </c:if>
          </c:forEach>
        </td>
        <td>${a.area}</td>
        <td>${a.horasEstimadas}</td>
        <td><fmt:formatNumber value="${a.costoHora}" type="currency"/></td>
        <td>${fn:replace(a.fechaInicio.toString(), 'T', ' ')}</td>
        <td>${fn:replace(a.fechaFin.toString(), 'T', ' ')}</td>
        <td><fmt:formatNumber value="${a.total}" type="currency"/></td>
        <td class="actions">
          <a href="${pageContext.request.contextPath}/asignaciones?action=edit&idCotizacion=${param.idCotizacion}&idAsignacion=${a.idAsignacion}">Editar</a> |
          <a href="${pageContext.request.contextPath}/asignaciones?action=remove&idCotizacion=${param.idCotizacion}&idAsignacion=${a.idAsignacion}" onclick="return confirm('¿Eliminar esta asignación?');">Eliminar</a> |
          <a href="${pageContext.request.contextPath}/subtareas?action=list&idAsignacion=${a.idAsignacion}">Ver Subtareas</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
