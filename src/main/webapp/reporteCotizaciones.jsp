<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Generar Reporte de Cotizaciones</title>
</head>
<body>
<h2>Reporte de Cotizaciones por Fecha</h2>
<form id="frmReporte" action="${pageContext.request.contextPath}/reportes/cotizaciones" method="get" target="pdfFrame">
  <label>Fecha Inicio:</label>
  <input type="date" name="fi" required /><br/><br/>
  <label>Fecha Fin:</label>
  <input type="date" name="ff" required /><br/><br/>
  <button type="submit">Generar PDF</button>
</form>
<hr/>
<iframe name="pdfFrame" width="100%" height="600px" style="border:1px solid #ccc;">
  <!-- Aquí se cargará el PDF -->
</iframe>
</body>
</html>
