<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>Listado de Usuarios</title>
    <!-- Usamos el CSS de empleados para el estilo de tabla y botones -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css"/>
</head>
<body>
<div class="container">
    <!-- Título de la sección -->
    <h1>Gestión de Usuarios</h1>

    <!-- Mensaje de éxito: si el servlet puso atributo “success”, lo mostramos -->
    <c:if test="${not empty success}">
        <div class="alert">
                ${success}
        </div>
    </c:if>
    <!-- Mensaje de error: si el servlet puso atributo “error”, lo mostramos con estilo rojo -->
    <c:if test="${not empty error}">
        <div class="alert" style="background-color: #f2dede; color: #a94442; border-color: #ebcccc;">
                ${error}
        </div>
    </c:if>

    <!-- Botones de navegación: volver al inicio o crear nuevo usuario -->
    <div style="margin-bottom: 15px;">
        <!-- Enlace para regresar al home -->
        <a href="${pageContext.request.contextPath}/home" class="button">Volver al inicio</a>
        <!-- Enlace para abrir el formulario de creación -->
        <a href="${pageContext.request.contextPath}/usuarios?action=form" class="button">Nuevo Usuario</a>
    </div>

    <!-- Tabla de usuarios -->
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Usuario</th>
            <th>Rol</th>
            <th>Activo</th>
            <th>Creado Por</th>
            <th>Fecha Creación</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <!-- Recorremos la lista “usuarios” enviada desde el servlet -->
        <c:forEach var="u" items="${usuarios}">
            <tr>
                <!-- Mostrar cada campo del objeto Usuario -->
                <td>${u.idUsuario}</td>
                <td>${u.username}</td>
                <td>${u.rol}</td>
                <td>
                    <!-- Convertimos el booleano a “Sí” o “No” -->
                    <c:out value="${u.estado ? 'Sí' : 'No'}"/>
                </td>
                <td>${u.creadoPor}</td>
                <td>
                    <!-- Formateamos LocalDateTime a “yyyy-MM-dd HH:mm” -->
                    <c:out value="${u.fechaCreacion.toString().substring(0,16).replace('T',' ')}"/>
                </td>
                <td>
                    <!-- Contenedor de acciones: Editar y Eliminar -->
                    <div class="actions">
                        <!-- Enlace para editar, pasando el ID en la URL -->
                        <a href="${pageContext.request.contextPath}/usuarios?action=form&amp;id=${u.idUsuario}">
                            Editar
                        </a>
                        <!-- Enlace para eliminar: confirmación JavaScript antes de ejecutar -->
                        <a href="${pageContext.request.contextPath}/usuarios?action=delete&amp;id=${u.idUsuario}"
                           onclick="return confirm('¿Eliminar usuario ${u.username}?');">
                            Eliminar
                        </a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
