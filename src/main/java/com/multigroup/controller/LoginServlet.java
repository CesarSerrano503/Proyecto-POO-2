/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 */
package com.multigroup.controller;

import com.multigroup.dao.UsuarioDAO;
import com.multigroup.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Si alguien accede por GET, mostramos el formulario de login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // Al enviar el formulario (POST), procesamos las credenciales
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username != null) username = username.trim();
        if (password != null) password = password.trim();

        // Validación de campos vacíos
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty()) {
            request.setAttribute("error", "Debe completar todos los campos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            // 1) Carga el driver y abre la conexión
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/multigroup", "root", "");

            // 2) Valida credenciales en la base
            UsuarioDAO dao = new UsuarioDAO(conn);
            Usuario usuario = dao.validarLogin(username, password);

            if (usuario != null) {
                // 3) Login exitoso: guardamos en sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                // DEBUG: imprimimos el rol
                System.out.println("[LoginServlet] Rol detectado: " + usuario.getRol());

                // 4) Redirigimos según rol
                String rol = usuario.getRol().trim().toLowerCase();
                if ("admin".equals(rol)) {
                    response.sendRedirect(request.getContextPath() + "/admin-index.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }

                return;
            }

            // 5) Credenciales inválidas
            request.setAttribute("error", "Credenciales incorrectas o usuario inactivo.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor. Por favor, intente más tarde.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
