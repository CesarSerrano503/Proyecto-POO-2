<!-- src/main/webapp/WEB-INF/jsp/ListaClientes.jsp -->
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>Lista de Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-list.css"/>
    <!-- Asegúrate de tener en empleados-list.css:
         .actions { display: flex; gap: 10px; align-items: center; }
         .actions a { margin: 0; }
    -->
</head>
<body>
<div class="container">
    <h2>Lista de Clientes</h2>
    <a href="${pageContext.request.contextPath}/" class="button cancel">Volver al Inicio</a>

    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert">${sessionScope.mensaje}</div>
        <c:remove var="mensaje" scope="session"/>
    </c:if>

    <a href="${pageContext.request.contextPath}/clientes?action=new" class="button">Nuevo Cliente</a>

    <table>
        <thead>
        <tr>
            <th>ID</th><th>Nombre</th><th>Documento</th><th>Tipo Persona</th>
            <th>Teléfono</th><th>Correo</th><th>Dirección</th><th>Estado</th>
            <th>Creado Por</th><th>Fecha Creación</th><th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cli" items="${listaClientes}">
            <tr>
                <td>${cli.idCliente}</td>
                <td>${cli.nombre}</td>
                <td>${cli.documento}</td>
                <td>${cli.tipoPersona}</td>
                <td>${cli.telefono}</td>
                <td>${cli.correo}</td>
                <td>${cli.direccion}</td>
                <td>${cli.estado}</td>
                <td>${cli.creadoPor}</td>
                <td><fmt:formatDate value="${cli.fechaCreacion}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>
                    <div class="actions">
                        <c:choose>
                            <c:when test="${cli.estado == 'Activo'}">
                                <a href="${pageContext.request.contextPath}/clientes?action=delete&idCliente=${cli.idCliente}"
                                   onclick="return confirm('¿Inactivar este cliente?');">Inactivar</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/clientes?action=activate&idCliente=${cli.idCliente}"
                                   onclick="return confirm('¿Activar este cliente?');">Activar</a>
                            </c:otherwise>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/clientes?action=edit&idCliente=${cli.idCliente}">Editar</a>
                        <a href="${pageContext.request.contextPath}/clientes?action=remove&idCliente=${cli.idCliente}"
                           onclick="return confirm('¿Eliminar permanentemente este cliente? Esta acción no se puede deshacer.');">Eliminar</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
