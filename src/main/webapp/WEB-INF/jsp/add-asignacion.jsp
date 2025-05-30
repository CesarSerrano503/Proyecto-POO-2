<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 29/5/2025
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html><html lang="es"><head>
<meta charset="UTF-8"/><title>Nueva Asignación</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-form.css"/>
</head><body>
<div class="container">
    <h2>Nueva Asignación</h2>
    <c:if test="${not empty errors}">
        <div class="alert"><ul>
            <c:forEach var="e" items="${errors}"><li>${e}</li></c:forEach>
        </ul></div>
    </c:if>
    <form action="${pageContext.request.contextPath}/asignaciones" method="post" class="form">
        <input type="hidden" name="idCotizacion" value="${param.idCotizacion}"/>

        <label>Empleado:
            <select name="idEmpleado" required>
                <option value="">--Selecciona--</option>
                <c:forEach var="emp" items="${empleados}">
                    <option value="${emp.idEmpleado}"
                        ${asignacion.idEmpleado==emp.idEmpleado?'selected':''}>
                            ${emp.nombre}
                    </option>
                </c:forEach>
            </select>
        </label>

        <label>&Aacute;rea:
            <input type="text" name="area" value="${asignacion.area}" required/>
        </label>

        <label>Costo Hora:
            <input type="number" step="0.01" name="costoHora" value="${asignacion.costoHora}" required/>
        </label>

        <label>Fecha Inicio:
            <input type="datetime-local" name="fechaInicio"
                   value="${asignacion.fechaInicio}" required/>
        </label>

        <label>Fecha Fin:
            <input type="datetime-local" name="fechaFin"
                   value="${asignacion.fechaFin}" required/>
        </label>

        <label>Horas Estimadas:
            <input type="number" name="horasEstimadas"
                   value="${asignacion.horasEstimadas}" required/>
        </label>

        <label>Título Actividad:
            <input type="text" name="tituloActividad"
                   value="${asignacion.tituloActividad}" required/>
        </label>

        <label>Tareas:
            <textarea name="tareas">${asignacion.tareas}</textarea>
        </label>

        <div class="form-actions">
            <button type="submit" class="button">Guardar</button>
            <button type="button" class="button cancel"
                    onclick="location.href='${pageContext.request.contextPath}/asignaciones?action=list&idCotizacion=${param.idCotizacion}'">
                Cancelar
            </button>
        </div>
    </form>
</div>
</body></html>
