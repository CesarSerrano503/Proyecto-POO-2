package com.multigroup.controller;

import com.multigroup.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/test-conn")
public class TestConnectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            // Línea de depuración para asegurarnos de que el servlet se ejecuta
            out.println("🔎 probando servlet<br/>");

            // Intentar obtener la conexión
            try (Connection conn = DBUtil.getConnection()) {
                out.println("<h3>¡Conexión exitosa a la BD!</h3>");
            } catch (SQLException e) {
                out.println("<h3>Error al conectar a la BD: " + e.getMessage() + "</h3>");
                e.printStackTrace(out);
            }

        }
    }
}
