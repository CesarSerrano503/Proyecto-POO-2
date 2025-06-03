<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 27/5/2025
  Archivo: index.jsp
  Función: Página principal que muestra la guía de módulos con tarjetas interactivas para Clientes, Empleados, Cotizaciones, Asignaciones y Subtareas.
  Modificado: Se agregó botón para cerrar sesión.
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>MultiGroup - Módulos</title>
    <%-- Vincula la hoja de estilos CSS para esta página --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <style>
        <%-- Contenedor para agrupar las tarjetas en un diseño tipo grid flexible --%>
        .grid-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: center;
        }
        <%-- Estilos para cada tarjeta individual --%>
        .card {
            position: relative;
            width: 200px;
            padding: 1.5rem;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-align: center;
            cursor: pointer;
            /* Asegura que los elementos hijos se dispongan en columna y centrados */
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .card h2 {
            margin: 0;
            color: #28a745;
            font-size: 1.2rem;
        }
        <%-- Enlace para mostrar/ocultar información adicional dentro de la tarjeta --%>
        .toggle-info {
            margin-top: 0.75rem;
            color: #0066cc;
            text-decoration: underline;
            font-size: 0.9rem;
            cursor: pointer;
        }
        <%-- Contenedor oculto que mostrará instrucciones cuando se haga clic en "toggle-info" --%>
        .info {
            display: none;
            width: 100%;
            box-sizing: border-box;
            margin-top: 0.5rem;
            padding: 0.5rem;
            background: #f9f9f9;
            border-radius: 4px;
            text-align: left;
            font-size: 0.9rem;
        }
        .info ol {
            padding-left: 1.2rem;
            margin: 0;
        }
        .info li {
            margin-bottom: 0.3rem;
        }
        <%-- Estilo para el botón de cerrar sesión --%>
        .logout-button {
            position: absolute;
            top: 1rem;
            right: 1rem;
            padding: 0.5rem 1rem;
            background-color: #dc3545; /* Rojo */
            color: white;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .logout-button:hover {
            background-color: #c82333;
        }
        .header-container {
            position: relative;
            margin-bottom: 2rem;
        }
        .header-container h1 {
            margin: 0;
            display: inline-block;
        }
    </style>
</head>
<body>
<main>
    <div class="container">
        <%-- Contenedor para el título y el botón de cerrar sesión --%>
        <div class="header-container">
            <h1>Guía de Módulos</h1>
            <%-- Botón para cerrar sesión, visible siempre que el usuario tenga sesión activa --%>
            <c:if test="${not empty sessionScope.usuario}">
                <a href="${pageContext.request.contextPath}/logout" class="logout-button">Cerrar Sesión</a>
            </c:if>
        </div>

        <div class="grid-cards">
            <%-- Tarjeta para acceder al Módulo Clientes --%>
            <a class="card" href="${pageContext.request.contextPath}/clientes">
                <h2>Módulo Clientes</h2>
            </a>

            <%-- Tarjeta para acceder al Módulo Empleados --%>
            <a class="card" href="${pageContext.request.contextPath}/empleados">
                <h2>Módulo Empleados</h2>
            </a>

            <%-- Tarjeta para acceder al Módulo Cotizaciones --%>
            <a class="card" href="${pageContext.request.contextPath}/cotizaciones?action=list">
                <h2>Módulo Cotizaciones</h2>
            </a>

            <%-- Tarjeta para el Módulo Asignaciones. Al hacer clic, redirige al listado de cotizaciones --%>
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Asignaciones</h2>
                <%-- Enlace para alternar la visibilidad de las instrucciones de uso --%>
                <span class="toggle-info" data-target="infoAsign">¿Cómo agrego una asignación?</span>
                <%-- Contenedor de instrucciones, inicialmente oculto --%>
                <div id="infoAsign" class="info">
                    <ol>
                        <li>Ve al <strong>Módulo Cotizaciones</strong> y selecciona una cotización.</li>
                        <li>Haz clic en <em>Nueva Asignación</em> y completa los campos.</li>
                        <li>Guarda; la nueva asignación aparecerá en la lista.</li>
                    </ol>
                </div>
            </div>

            <%-- Tarjeta para el Módulo Subtareas. Redirige al listado de cotizaciones --%>
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Subtareas</h2>
                <%-- Enlace para alternar la visibilidad de las instrucciones de uso --%>
                <span class="toggle-info" data-target="infoSubt">¿Cómo agrego una subtarea?</span>
                <%-- Contenedor de instrucciones, inicialmente oculto --%>
                <div id="infoSubt" class="info">
                    <ol>
                        <li>En el <strong>Módulo Asignaciones</strong>, haz clic en “Ver Subtareas”.</li>
                        <li>Selecciona <em>Nueva Subtarea</em> y completa título y descripción.</li>
                        <li>Guarda; la nueva subtarea aparecerá en la lista.</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</main>

<%-- Script JavaScript para manejar el toggle de visibilidad de los bloques de información --%>
<script>
    document.querySelectorAll('.toggle-info').forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.stopPropagation();  // Evita que el clic propague al contenedor .card y dispare navigation
            e.preventDefault();   // Previene comportamiento predeterminado del enlace
            var info = document.getElementById(this.getAttribute('data-target'));
            var isVisible = info.style.display === 'block';
            // Alterna entre mostrar/ocultar la sección de información
            info.style.display = isVisible ? 'none' : 'block';
            // Cambia el texto del enlace entre “Más información” y “Menos información”
            this.textContent = isVisible
                ? '¿Cómo ' + (this.getAttribute('data-target') === 'infoAsign'
                ? 'agrego una asignación?'
                : 'agrego una subtarea?')
                : 'Menos información';
        });
    });
</script>
</body>
</html>
