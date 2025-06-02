<%--
  Created by IntelliJ IDEA.
  User: Cesar
  Date: 1/6/2025
  Time: 19:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <title>Generar Reportes</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<div class="container">
  <h1>Reportes</h1>

  <!-- Formulario para elegir estado y generar PDF de clientes -->
  <form action="${pageContext.request.contextPath}/reportes" method="get">
    <!-- Indicamos que action=clientes para el ReporteServlet -->
    <input type="hidden" name="action" value="clientes"/>

    <label for="estado">Estado de Clientes:</label>
    <select name="estado" id="estado">
      <option value="Activo">Activo</option>
      <option value="Inactivo">Inactivo</option>
    </select>

    <button type="submit" class="button">Generar Reporte de Clientes</button>
  </form>

  <!-- Agrega, en el futuro, más formularios para otros reportes: empleados, cotizaciones, etc. -->

  <p style="margin-top: 20px;">
    <a href="${pageContext.request.contextPath}/home" class="button">Volver al inicio</a>
  </p>
</div>
</body>
</html>

