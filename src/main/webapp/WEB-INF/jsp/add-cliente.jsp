<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nuevo Cliente</title>
</head>
<body>
<h2>Registrar Nuevo Cliente</h2>
<form action="${pageContext.request.contextPath}/clientes/nuevo" method="post">
    <label>Nombre:<br/>
        <input type="text" name="nombre" required maxlength="255"/>
    </label><br/><br/>
    <label>Documento:<br/>
        <input type="text" name="documento" required maxlength="50"/>
    </label><br/><br/>
    <label>Tipo de Persona:<br/>
        <select name="tipoPersona">
            <option value="Natural">Natural</option>
            <option value="Jurídica">Jurídica</option>
        </select>
    </label><br/><br/>
    <label>Teléfono:<br/>
        <input type="text" name="telefono" maxlength="20"/>
    </label><br/><br/>
    <label>Correo:<br/>
        <input type="email" name="correo" maxlength="255"/>
    </label><br/><br/>
    <label>Dirección:<br/>
        <textarea name="direccion" rows="3" cols="30"></textarea>
    </label><br/><br/>
    <button type="submit">Guardar Cliente</button>
</form>
<p><a href="${pageContext.request.contextPath}/clientes">← Volver al listado</a></p>
</body>
</html>
