// Archivo: src/main/java/com/multigroup/controller/HomeServlet.java
package com.multigroup.controller;

import com.multigroup.model.Usuario;   // <— Import correcto
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * HomeServlet: redirige a la página de inicio
 * correcta según el rol del usuario en sesión.
 *
 * - Si no hay sesión o no existe "usuario" → /login
 * - Si rol == "admin"            → /admin-index.jsp
 * - En cualquier otro caso       → /index.jsp
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            // No hay sesión activa: forzar login
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // <— Asegúra de que el LoginServlet leyó “new Usuario(...)”
        //    y lo guardó en sesión con session.setAttribute("usuario", usuarioObj);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            // No hay usuario en sesión: forzar login
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Compara el campo “rol” tal y como se guarda en la BD (“admin” o “colaborador”)
        if ("admin".equalsIgnoreCase(usuario.getRol())) {
            // Página de inicio para administradores
            resp.sendRedirect(req.getContextPath() + "/admin-index.jsp");
        } else {
            // Página de inicio para colaboradores (o cualquier otro rol)
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }
}
