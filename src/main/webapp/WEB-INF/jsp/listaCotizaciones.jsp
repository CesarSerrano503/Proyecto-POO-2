<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Lista de Cotizaciones</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css" />
</head>
<body>
<div class="container">
    <h2>Lista de Cotizaciones</h2>
    <a href="${pageContext.request.contextPath}/" class="button cancel">Volver al Inicio</a>

    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert">${sessionScope.mensaje}</div>
        <c:remove var="mensaje" scope="session" />
    </c:if>

    <a href="${pageContext.request.contextPath}/cotizaciones?action=new" class="button">Nueva Cotización</a>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Cliente</th>
            <th>Estado</th>
            <th>Total Horas</th>
            <th>Inicio</th>
            <th>Fin</th>
            <th>Costo Asig.</th>
            <th>Costos Adic.</th>
            <th>Total</th>
            <th>Creado Por</th>
            <th>Creación</th>
            <th>Actualización</th>
            <th>Finalización</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${listaCot}">
            <tr>
                <td>${c.idCotizacion}</td>
                <td>${c.clienteNombre}</td>
                <td>${c.estado}</td>
                <td>
                    <fmt:formatNumber value="${c.totalHoras}" type="number" minFractionDigits="2" />
                </td>
                <td>
                    <fmt:formatDate value="${c.fechaInicio}" pattern="yyyy-MM-dd HH:mm" />
                </td>
                <td>
                    <fmt:formatDate value="${c.fechaFin}" pattern="yyyy-MM-dd HH:mm" />
                </td>
                <td>
                    <fmt:formatNumber value="${c.costoAsignaciones}" type="currency" />
                </td>
                <td>
                    <fmt:formatNumber value="${c.costosAdicionales}" type="currency" />
                </td>
                <td>
                    <fmt:formatNumber value="${c.total}" type="currency" />
                </td>
                <td>${c.creadoPor}</td>
                <td>
                    <fmt:formatDate value="${c.fechaCreacion}" pattern="yyyy-MM-dd HH:mm:ss" />
                </td>
                <td>
                    <fmt:formatDate value="${c.fechaActualizacion}" pattern="yyyy-MM-dd HH:mm:ss" />
                </td>
                <td>
                    <fmt:formatDate value="${c.fechaFinalizacion}" pattern="yyyy-MM-dd HH:mm:ss" />
                </td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/cotizaciones?action=finalize&amp;idCotizacion=${c.idCotizacion}"
                       onclick="return confirm('¿Finalizar cotización?');">Finalizar</a> |
                    <a href="${pageContext.request.contextPath}/cotizaciones?action=edit&amp;idCotizacion=${c.idCotizacion}">Editar</a> |
                    <a href="${pageContext.request.contextPath}/cotizaciones?action=remove&amp;idCotizacion=${c.idCotizacion}"
                       onclick="return confirm('¿Eliminar permanentemente?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
