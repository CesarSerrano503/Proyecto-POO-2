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

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Simplemente mostramos el formulario de login
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Validación mínima: campos no vacíos
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Usuario y contraseña son obligatorios.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            // 1) Buscamos el usuario por username
            Usuario usuario = usuarioDAO.findByUsername(username.trim());
            if (usuario == null || !usuario.isEstado()) {
                req.setAttribute("error", "Usuario o contraseña incorrectos.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // 2) Recuperamos el hash (o, si aún no está hasheado, la contraseña en texto plano)
            String stored = usuario.getPassword();
            boolean match;

            // 3) Comprobamos si el valor en BD empieza con prefijo BCrypt
            if (stored != null && (stored.startsWith("$2a$")
                    || stored.startsWith("$2b$")
                    || stored.startsWith("$2y$"))) {
                // Comparación normal con BCrypt
                match = BCrypt.checkpw(password, stored);

            } else {
                // Si no empieza con "$2a$", suponemos que es texto plano antiguo
                if (password.equals(stored)) {
                    match = true;
                    // Rehasheamos esa contraseña y actualizamos al vuelo
                    // Rellenamos el objeto Usuario con la nueva contraseña en texto plano:
                    usuario.setPassword(password);
                    // Pasamos el objeto al DAO, que hará hash y UPDATE solo de password
                    usuarioDAO.update(usuario);
                } else {
                    match = false;
                }
            }

            if (!match) {
                req.setAttribute("error", "Usuario o contraseña incorrectos.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // 4) Si llegamos aquí, las credenciales son correctas
            HttpSession session = req.getSession(true);
            session.setAttribute("usuario", usuario);

            // 5) Redirigimos según el rol
            if ("admin".equalsIgnoreCase(usuario.getRol())) {
                resp.sendRedirect(req.getContextPath() + "/admin-index.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }

        } catch (Exception e) {
            throw new ServletException("Error en LoginServlet", e);
        }
    }
}
