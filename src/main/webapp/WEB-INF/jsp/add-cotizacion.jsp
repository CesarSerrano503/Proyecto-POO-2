<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 28/5/2025
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>Nueva Cotización</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-form.css"/>
</head>
<body>
<div class="container">
    <h2>Nueva Cotización</h2>

    <c:if test="${not empty errors}">
        <div class="alert">
            <ul>
                <c:forEach var="err" items="${errors}">
                    <li>${err}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/cotizaciones" method="post" class="form">
        <label>Cliente:
            <select name="idCliente" required>
                <option value="">--Selecciona--</option>
                <c:forEach var="cli" items="${listaClientes}">
                    <option value="${cli.idCliente}"
                            <c:if test="${cli.idCliente == cotizacion.idCliente}">selected</c:if>>
                            ${cli.nombre}
                    </option>
                </c:forEach>
            </select>
        </label>

        <label>Total Horas:
            <input type="number" name="totalHoras"
                   value="${cotizacion.totalHoras}"
                   step="0.01" required/>
        </label>

        <label>Fecha Inicio:
            <input type="datetime-local" name="fechaInicio"
                   value="${cotizacion.fechaInicio}"
                   required/>
        </label>

        <label>Fecha Fin:
            <input type="datetime-local" name="fechaFin"
                   value="${cotizacion.fechaFin}"
                   required/>
        </label>

        <label>Costo Asignaciones:
            <input type="number" name="costoAsignaciones"
                   value="${cotizacion.costoAsignaciones}"
                   step="0.01" required/>
        </label>

        <label>Costos Adicionales:
            <input type="number" name="costosAdicionales"
                   value="${cotizacion.costosAdicionales}"
                   step="0.01" required/>
        </label>

        <label>Total:
            <input type="number" name="total"
                   value="${cotizacion.total}"
                   step="0.01" required/>
        </label>

        <label>Creado Por:
            <input type="text" name="creadoPor"
                   value="${cotizacion.creadoPor}"
                   maxlength="100" required/>
        </label>

        <div class="form-actions">
            <button type="submit" class="button">Guardar</button>
            <button type="button" class="button cancel"
                    onclick="location.href='${pageContext.request.contextPath}/cotizaciones?action=list';">
                Cancelar
            </button>
        </div>
    </form>
</div>
</body>
</html>
