<%--
  Created by IntelliJ IDEA.
  User: Cesar
  Date: 27/5/2025
  Time: 22:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Editar Empleado</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-form.css" />

</head>
<body>
<div class="container">
  <h2>Editar Empleado</h2>
  <form action="${pageContext.request.contextPath}/empleados" method="post" class="form">
    <input type="hidden" name="idEmpleado" value="${empleado.idEmpleado}" />
    <label>Nombre:
      <input type="text" name="nombre" required maxlength="255" value="${empleado.nombre}" />
    </label>
    <label>Documento:
      <input type="text" name="documento" required maxlength="50" value="${empleado.documento}" />
    </label>
    <label>Tipo Persona:
      <select name="tipoPersona" required>
        <option value="Natural" ${empleado.tipoPersona=='Natural' ? 'selected':''}>Natural</option>
        <option value="Jurídica" ${empleado.tipoPersona=='Jurídica' ? 'selected':''}>Jurídica</option>
      </select>
    </label>
    <label>Tipo Contratación:
      <select name="tipoContratacion" required>
        <option value="Permanente" ${empleado.tipoContratacion=='Permanente' ? 'selected':''}>Permanente</option>
        <option value="Por Horas" ${empleado.tipoContratacion=='Por Horas' ? 'selected':''}>Por Horas</option>
      </select>
    </label>
    <label>Teléfono:
      <input type="text" name="telefono" maxlength="20" value="${empleado.telefono}" />
    </label>
    <label>Correo:
      <input type="email" name="correo" maxlength="150" value="${empleado.correo}" />
    </label>
    <label>Dirección:
      <input type="text" name="direccion" maxlength="255" value="${empleado.direccion}" />
    </label>
    <label>Estado:
      <select name="estado" required>
        <option value="Activo" ${empleado.estado=='Activo' ? 'selected':''}>Activo</option>
        <option value="Inactivo" ${empleado.estado=='Inactivo' ? 'selected':''}>Inactivo</option>
      </select>
    </label>
    <label>Creado Por:
      <input type="text" name="creadoPor" maxlength="100" value="${empleado.creadoPor}" required />
    </label>
    <div>
      <button type="submit" class="button">Actualizar</button>
      <a href="${pageContext.request.contextPath}/empleados" class="button cancel">Cancelar</a>
    </div>
  </form>
</div>
</body>
</html>