<%--
  Created by IntelliJ IDEA.
  User: Cesar
  Date: 27/5/2025
  Time: 22:23
  To change this template use File | Settings | File Templates.
--%>
<!-- src/main/webapp/WEB-INF/jsp/add-empleado.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Nuevo Empleado</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empleados-form.css" />

</head>
<body>
<div class="container">
  <h2>Nuevo Empleado</h2>
  <form action="${pageContext.request.contextPath}/empleados" method="post" class="form">
    <label>Nombre:
      <input type="text" name="nombre" required maxlength="255"/>
    </label>
    <label>Documento:
      <input type="text" name="documento" required maxlength="50"/>
    </label>
    <label>Tipo Persona:
      <select name="tipoPersona" required>
        <option value="Natural">Natural</option>
        <option value="Jurídica">Jurídica</option>
      </select>
    </label>
    <label>Tipo Contratación:
      <select name="tipoContratacion" required>
        <option value="Permanente">Permanente</option>
        <option value="Por Horas">Por Horas</option>
      </select>
    </label>
    <label>Teléfono:
      <input type="text" name="telefono" maxlength="20"/>
    </label>
    <label>Correo:
      <input type="email" name="correo" maxlength="150"/>
    </label>
    <label>Dirección:
      <input type="text" name="direccion" maxlength="255"/>
    </label>
    <label>Creado Por:
      <input type="text" name="creadoPor" maxlength="100" required/>
    </label>

    <div class="form-actions">
      <button type="submit" class="button">Guardar</button>
      <!-- botón ‘Cancelar’ -->
      <button type="button"
              class="button cancel"
              onclick="window.location='${pageContext.request.contextPath}/empleados';">
        Cancelar
      </button>
    </div>
  </form>
</div>
</body>
</html>


