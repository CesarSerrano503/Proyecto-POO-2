<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html><html lang="es"><head>
<meta charset="UTF-8"/><title>Asignaciones</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
</head><body>
<div class="container">
  <h2>Asignaciones de Cotización ${param.idCotizacion}</h2>
  <a href="${pageContext.request.contextPath}/cotizaciones?action=list" class="button cancel">Volver a Cotizaciones</a>
  <c:if test="${not empty sessionScope.mensaje}">
    <div class="alert">${sessionScope.mensaje}</div>
    <c:remove var="mensaje" scope="session"/>
  </c:if>
  <a href="${pageContext.request.contextPath}/asignaciones?action=new&idCotizacion=${param.idCotizacion}" class="button">Nueva Asignación</a>
  <table>
    <thead><tr>
      <th>ID</th><th>Empleado</th><th>&Aacute;rea</th><th>Horas</th><th>Costo Hora</th>
      <th>Inicio</th><th>Fin</th><th>Total</th><th>Acciones</th>
    </tr></thead>
    <tbody>
    <c:forEach var="a" items="${listaAsig}">
      <tr>
        <td>${a.idAsignacion}</td>
        <td>${a.idEmpleado}</td>
        <td>${a.area}</td>
        <td>${a.horasEstimadas}</td>
        <td><fmt:formatNumber value="${a.costoHora}" type="currency"/></td>
        <td><fmt:formatDate value="${a.fechaInicio}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td><fmt:formatDate value="${a.fechaFin}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td><fmt:formatNumber value="${a.total}" type="currency"/></td>
        <td class="actions">
          <a href="${pageContext.request.contextPath}/asignaciones?action=edit&idCotizacion=${param.idCotizacion}&idAsignacion=${a.idAsignacion}">Editar</a>
          <a href="${pageContext.request.contextPath}/asignaciones?action=remove&idCotizacion=${param.idCotizacion}&idAsignacion=${a.idAsignacion}"
             onclick="return confirm('Eliminar asignación?');">Eliminar</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body></html>
