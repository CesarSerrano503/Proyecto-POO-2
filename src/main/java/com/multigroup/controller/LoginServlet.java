/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * LoginServlet: Gestiona el inicio de sesión de usuarios.
 * - doGet: muestra el formulario de login.
 * - doPost: valida credenciales, verifica hash de contraseña con BCrypt y redirige según rol.
 */
package com.multigroup.controller;

import com.multigroup.dao.UsuarioDAO;
import com.multigroup.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // DAO para operaciones sobre la tabla de usuarios
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Método GET: muestra el formulario de login.
     *
     * @param req  HttpServletRequest con información de la petición.
     * @param resp HttpServletResponse para enviar la respuesta/forward.
     * @throws ServletException si ocurre error en el servlet.
     * @throws IOException      si ocurre error de E/S al forward.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Forward a la página de login (login.jsp)
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    /**
     * Método POST: procesa las credenciales enviadas desde el formulario.
     * 1) Valida que usuario y contraseña no estén vacíos.
     * 2) Busca el usuario por username en BD.
     * 3) Verifica el hash de la contraseña (BCrypt o texto plano antiguo).
     * 4) Si coincide, crea sesión y redirige según rol; si no, muestra error.
     *
     * @param req  HttpServletRequest con parámetros "username" y "password".
     * @param resp HttpServletResponse para respuestas/redirecciones.
     * @throws ServletException si ocurre error en el servlet.
     * @throws IOException      si ocurre error de E/S al forward o redirect.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Leer parámetros del formulario de login
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 2) Validación mínima: campos no vacíos
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            // Si faltan datos, agrega mensaje de error y retorna al formulario
            req.setAttribute("error", "Usuario y contraseña son obligatorios.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            // 3) Buscar el usuario por username en la base de datos
            Usuario usuario = usuarioDAO.findByUsername(username.trim());
            // Si no existe el usuario o está inactivo (estado == false), error
            if (usuario == null || !usuario.isEstado()) {
                req.setAttribute("error", "Usuario o contraseña incorrectos.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // 4) Obtener el valor almacenado en BD (puede ser hash BCrypt o texto plano)
            String stored = usuario.getPassword();
            boolean match;

            // 5) Comprobar si la contraseña almacenada empieza con prefijo BCrypt
            if (stored != null && (stored.startsWith("$2a$")
                    || stored.startsWith("$2b$")
                    || stored.startsWith("$2y$"))) {
                // Si está en formato BCrypt, usar checkpw para comparar hash
                match = BCrypt.checkpw(password, stored);
            } else {
                // Caso: contraseña almacenada en texto plano (legacy)
                if (password.equals(stored)) {
                    // Si coincide texto plano, se marca como match
                    match = true;
                    // Re-hasheamos la contraseña y actualizamos el registro a BCrypt
                    usuario.setPassword(password);
                    // El DAO hará el hash y el UPDATE solo del campo password
                    usuarioDAO.update(usuario);
                } else {
                    match = false;
                }
            }

            // 6) Si no coincide, error y regresar al formulario
            if (!match) {
                req.setAttribute("error", "Usuario o contraseña incorrectos.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // 7) Credenciales correctas: crear o recuperar sesión y almacenar objeto Usuario
            HttpSession session = req.getSession(true);
            session.setAttribute("usuario", usuario);

            // 8) Redirigir según el rol del usuario: "admin" o cualquier otro
            if ("admin".equalsIgnoreCase(usuario.getRol())) {
                resp.sendRedirect(req.getContextPath() + "/admin-index.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }

        } catch (Exception e) {
            // En caso de excepción, lanza ServletException con mensaje
            throw new ServletException("Error en LoginServlet", e);
        }
    }
}
