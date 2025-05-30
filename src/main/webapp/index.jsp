<%--
  Código hecho por: Cesar Antonio Serrano Gutiérrez
  Fecha de creación: 27/5/2025
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <title>MultiGroup - Módulos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <style>
        .grid-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: center;
        }
        .card {
            position: relative;
            width: 200px;
            padding: 1.5rem;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-align: center;
            cursor: pointer;
            /* Nuevo: asegurar que los hijos fluyan en columna */
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .card h2 {
            margin: 0;
            color: #28a745;
            font-size: 1.2rem;
        }
        .toggle-info {
            margin-top: 0.75rem;
            color: #0066cc;
            text-decoration: underline;
            font-size: 0.9rem;
            cursor: pointer;
        }
        .info {
            display: none;
            /* Ocupar ancho completo de la tarjeta */
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
    </style>
</head>
<body>
<main>
    <div class="container">
        <h1>Guía de Módulos</h1>
        <div class="grid-cards">

            <!-- Clientes -->
            <a class="card" href="${pageContext.request.contextPath}/clientes">
                <h2>Módulo Clientes</h2>
            </a>

            <!-- Empleados -->
            <a class="card" href="${pageContext.request.contextPath}/empleados">
                <h2>Módulo Empleados</h2>
            </a>

            <!-- Cotizaciones -->
            <a class="card" href="${pageContext.request.contextPath}/cotizaciones?action=list">
                <h2>Módulo Cotizaciones</h2>
            </a>

            <!-- Asignaciones -->
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Asignaciones</h2>
                <span class="toggle-info" data-target="infoAsign">¿Cómo agrego una asignación?</span>
                <div id="infoAsign" class="info">
                    <ol>
                        <li>Ve al <strong>Módulo Cotizaciones</strong> y selecciona una cotización.</li>
                        <li>Haz clic en <em>Nueva Asignación</em> y completa los campos.</li>
                        <li>Guarda; la nueva asignación aparecerá en la lista.</li>
                    </ol>
                </div>
            </div>

            <!-- Subtareas -->
            <div class="card" onclick="window.location='${pageContext.request.contextPath}/cotizaciones?action=list'">
                <h2>Módulo Subtareas</h2>
                <span class="toggle-info" data-target="infoSubt">¿Cómo agrego una subtarea?</span>
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

<script>
    document.querySelectorAll('.toggle-info').forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.stopPropagation();
            e.preventDefault();
            var info = document.getElementById(this.getAttribute('data-target'));
            var isVisible = info.style.display === 'block';
            info.style.display = isVisible ? 'none' : 'block';
            this.textContent = isVisible ? '¿Cómo agrego ' +
                (this.getAttribute('data-target') === 'infoAsign' ? 'una asignación?' : 'una subtarea?')
                : 'Menos información';
        });
    });
</script>
</body>
</html>
