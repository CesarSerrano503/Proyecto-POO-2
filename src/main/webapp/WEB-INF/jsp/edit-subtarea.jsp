<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 29/5/2025
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title>Editar Subtarea</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subtarea-form.css"/>
</head>
<body>
<div class="form-card">
  <h2>Editar Subtarea</h2>
  <a href="${pageContext.request.contextPath}/subtareas?action=list&idAsignacion=${param.idAsignacion}" class="button cancel">Volver</a>

  <c:if test="${not empty errors}">
    <div class="alert">
      <ul class="error-list">
        <c:forEach var="err" items="${errors}">
          <li>${err}</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/subtareas" method="post">
    <input type="hidden" name="idAsignacion" value="${param.idAsignacion}"/>
    <input type="hidden" name="idSubtarea"    value="${subtarea.idSubtarea}"/>

    <div class="form-group">
      <label for="tituloSubtarea">Título de la Subtarea</label>
      <input id="tituloSubtarea"
             name="tituloSubtarea"
             type="text"
             class="form-control"
             value="${fn:escapeXml(subtarea.tituloSubtarea)}"
             required/>
    </div>

    <div class="form-group">
      <label for="descripcionSubtarea">Descripción</label>
      <textarea id="descripcionSubtarea"
                name="descripcionSubtarea"
                class="form-control"
                rows="4"
                required>${fn:escapeXml(subtarea.descripcionSubtarea)}</textarea>
    </div>

    <div class="form-actions">
      <button type="submit" class="button submit">Actualizar</button>
      <a href="${pageContext.request.contextPath}/subtareas?action=list&idAsignacion=${param.idAsignacion}" class="button cancel">Cancelar</a>
    </div>
  </form>
</div>
</body>
</html>
