package com.multigroup.reports;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/reportes/cotizaciones")
public class ReporteCotizacionesServlet extends HttpServlet {

    // Cambia a tu BD real:
    private static final String JDBC_URL      = "jdbc:mysql://localhost:3306/multiworks";
    private static final String JDBC_USER     = "appuser";       // o "root"
    private static final String JDBC_PASSWORD = "TuPassSegura";   // o "" si no hay

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1) Leer parámetros de fecha (yyyy-MM-dd)
        String fiParam = request.getParameter("fi");
        String ffParam = request.getParameter("ff");

        if (fiParam == null || ffParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar 'fi' (fechaInicio) y 'ff' (fechaFin) en formato yyyy-MM-dd");
            return;
        }

        Connection conn = null;
        try {
            // 2) Obtener la ruta real del directorio WEB-INF/reports en el servidor
            String reportsDir = getServletContext().getRealPath("/WEB-INF/reports");

            // 3) Ubicaciones de .jrxml y .jasper
            String jrxmlPath  = reportsDir + File.separator + "CotizacionesPorFecha.jrxml";
            String jasperPath = reportsDir + File.separator + "CotizacionesPorFecha.jasper";

            // 4) Si NO existe el .jasper, compílalo a partir del .jrxml
            File jasperFile = new File(jasperPath);
            if (!jasperFile.exists()) {
                // Compilar .jrxml → .jasper
                JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            }

            // 5) Cargar driver y conectar a la BD
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // 6) Preparar parámetros para el reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("FechaInicio", java.sql.Date.valueOf(fiParam));
            parametros.put("FechaFin",    java.sql.Date.valueOf(ffParam));

            // 7) Llenar el reporte (.jasper ya existe en disco)
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parametros, conn);

            // 8) Enviar PDF como respuesta
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "inline; filename=\"ReporteCotizaciones.pdf\"");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error generando reporte: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}
