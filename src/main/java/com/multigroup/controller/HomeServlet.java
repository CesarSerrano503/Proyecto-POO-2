/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * HomeServlet: redirige a la página de inicio correcta según el rol del usuario en sesión.
 * - Si no hay sesión o no existe "usuario" → /login
 * - Si rol == "admin"            → /admin-index.jsp
 * - En cualquier otro caso       → /index.jsp
 */
package com.multigroup.controller;

import com.multigroup.model.Usuario;   // Import correcto de la clase Usuario
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    /**
     * Método GET: verifica si existe sesión y usuario en sesión,
     * luego redirige según el rol almacenado en el objeto Usuario.
     *
     * @param req  HttpServletRequest con información de la petición.
     * @param resp HttpServletResponse para enviar la respuesta/redirección.
     * @throws ServletException si ocurre error en el servlet.
     * @throws IOException      si ocurre error de E/S al redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Intenta recuperar la sesión existente; no crea una nueva (false)
        HttpSession session = req.getSession(false);
        if (session == null) {
            // No hay sesión activa: redirige al login
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Recupera el objeto "usuario" almacenado en sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            // Si no existe el atributo "usuario", redirige al login
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Compara el campo "rol" (se asume guardado en minúsculas o mayúsculas indistintas)
        if ("admin".equalsIgnoreCase(usuario.getRol())) {
            // Si el rol es "admin", redirige a la página de inicio de administradores
            resp.sendRedirect(req.getContextPath() + "/admin-index.jsp");
        } else {
            // En cualquier otro caso (colaborador, roles futuros, etc.), redirige a la página de inicio estándar
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }
}
