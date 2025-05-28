<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Listado de Empleados</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css" />
</head>
<body>
<div class="container">
  <h2>Lista de Empleados</h2>

  <!-- Mensaje de éxito / error -->
  <c:if test="${not empty sessionScope.mensaje}">
    <div class="alert">
        ${sessionScope.mensaje}
    </div>
    <c:remove var="mensaje" scope="session"/>
  </c:if>

  <a class="button" href="${pageContext.request.contextPath}/empleados?action=new">Nuevo Empleado</a>
  <table>
    <thead>
    <tr>
      <th>ID</th><th>Nombre</th><th>Documento</th><th>Tipo Persona</th>
      <th>Tipo Contratación</th><th>Teléfono</th><th>Correo</th>
      <th>Dirección</th><th>Estado</th><th>Creado Por</th>
      <th>Fecha Creación</th><th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="emp" items="${listaEmpleados}">
      <tr>
        <td>${emp.idEmpleado}</td>
        <td>${emp.nombre}</td>
        <td>${emp.documento}</td>
        <td>${emp.tipoPersona}</td>
        <td>${emp.tipoContratacion}</td>
        <td>${emp.telefono}</td>
        <td>${emp.correo}</td>
        <td>${emp.direccion}</td>
        <td>${emp.estado}</td>
        <td>${emp.creadoPor}</td>
        <td><fmt:formatDate value="${emp.fechaCreacion}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td class="actions">
          <c:choose>
            <c:when test="${emp.estado == 'Activo'}">
              <a href="${pageContext.request.contextPath}/empleados?action=delete&idEmpleado=${emp.idEmpleado}"
                 onclick="return confirm('¿Inactivar este empleado?');">Inactivar</a>
            </c:when>
            <c:otherwise>
              <a href="${pageContext.request.contextPath}/empleados?action=activate&idEmpleado=${emp.idEmpleado}"
                 onclick="return confirm('¿Activar este empleado?');">Activar</a>
            </c:otherwise>
          </c:choose>
          |
          <a href="${pageContext.request.contextPath}/empleados?action=edit&idEmpleado=${emp.idEmpleado}">Editar</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
