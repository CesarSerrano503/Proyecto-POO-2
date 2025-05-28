// src/main/java/com/multigroup/controller/TestConnectionServlet.java
package com.multigroup.controller;

// AHORA (jakarta.servlet)
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * Servlet para verificar que la conexión a la base de datos está disponible.
 * Se accede en GET desde /test-conn
 */
@WebServlet("/test-conn")
public class TestConnectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Obtenemos la conexión que pusimos en el contexto de la aplicación
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");

        resp.setContentType("text/plain");
        try {
            if (conn != null && !conn.isClosed()) {
                resp.getWriter().write("Conexión OK");
            } else {
                resp.getWriter().write("Conexión NO disponible");
            }
        } catch (Exception e) {
            resp.getWriter().write("Error al comprobar conexión: " + e.getMessage());
        }
    }
}
