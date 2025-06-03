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

    private static final String JDBC_URL      = "jdbc:mysql://localhost:3306/multiworks";
    private static final String JDBC_USER     = "appuser";
    private static final String JDBC_PASSWORD = "TuPassSegura";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fiParam = request.getParameter("fi");
        String ffParam = request.getParameter("ff");
        if (fiParam == null || ffParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar 'fi' y 'ff' en formato yyyy-MM-dd");
            return;
        }

        Connection conn = null;
        try {
            String reportsDir = getServletContext().getRealPath("/WEB-INF/reports");
            // **Nombre exacto** del JRXML que acabamos de pegar arriba:
            String jrxmlPath  = reportsDir + File.separator + "CotizacionesPorFecha.jrxml";
            String jasperPath = reportsDir + File.separator + "CotizacionesPorFecha.jasper";

            File jasperFile = new File(jasperPath);
            if (!jasperFile.exists()) {
                JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("FechaInicio", java.sql.Date.valueOf(fiParam));
            parametros.put("FechaFin",    java.sql.Date.valueOf(ffParam));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parametros, conn);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"ReporteCotizaciones.pdf\"");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error generando reporte: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignored) {}
        }
    }
}
