/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 1/6/2025
 */
package com.multigroup.controller;

import com.multigroup.dao.UsuarioDAO;
import com.multigroup.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servlet para gestionar operaciones CRUD sobre usuarios.
 * Acepta acciones vía parámetro "action":
 * - list: mostrar listado de usuarios
 * - form: mostrar formulario de creación/edición
 * - save: procesar creación o edición
 * - delete: eliminar un usuario
 */
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    // DAO para acceso a datos de la tabla “usuarios”
    private UsuarioDAO dao = new UsuarioDAO();

    /**
     * Maneja peticiones GET para listar, mostrar formulario o eliminar.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtener el parámetro “action”; por defecto “list”
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            // Rutear según el action
            switch (action) {
                case "form":
                    mostrarFormulario(req, resp);
                    break;
                case "delete":
                    eliminarUsuario(req, resp);
                    break;
                case "list":
                default:
                    listarUsuarios(req, resp);
                    break;
            }
        } catch (Exception e) {
            // Cualquier excepción se envuelve en ServletException
            throw new ServletException("Error en UsuarioServlet GET", e);
        }
    }

    /**
     * Maneja peticiones POST para guardar (crear/editar) usuarios.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if ("save".equals(action)) {
            try {
                // Procesamos creación o edición
                guardarUsuario(req, resp);
            } catch (Exception e) {
                // Si falla al guardar, regresamos al formulario con mensaje de error
                req.setAttribute("error", "No se pudo guardar el usuario: " + e.getMessage());
                try {
                    mostrarFormulario(req, resp);
                } catch (Exception ex) {
                    throw new ServletException("Error mostrando formulario tras fallo en save", ex);
                }
            }
        } else {
            // Si llega otro POST inesperado, redirigimos al listado
            resp.sendRedirect(req.getContextPath() + "/usuarios?action=list");
        }
    }

    /**
     * Lista todos los usuarios y los envía al JSP de listado.
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @throws Exception si ocurre un error en DAO o forwarding
     */
    private void listarUsuarios(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // Obtenemos la lista de todos los usuarios desde el DAO
        List<Usuario> lista = dao.findAll();
        req.setAttribute("usuarios", lista);

        // Si vienen parámetros de éxito o error en la URL, los pasamos como atributos
        String paramSuccess = req.getParameter("success");
        String paramError   = req.getParameter("error");
        if (paramSuccess != null && !paramSuccess.isEmpty()) {
            req.setAttribute("success", paramSuccess);
        }
        if (paramError != null && !paramError.isEmpty()) {
            req.setAttribute("error", paramError);
        }

        // Hacemos forward a usuario-listar.jsp para renderizar la vista
        req.getRequestDispatcher("/WEB-INF/jsp/usuario-listar.jsp")
                .forward(req, resp);
    }

    /**
     * Muestra el formulario para crear un nuevo usuario o editar uno existente.
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @throws Exception si ocurre error en DAO o forwarding
     */
    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // Si se recibe “id” significa que es edición
        String idParam = req.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Usuario u = dao.findById(id);
            if (u != null) {
                // Pasamos el objeto Usuario al JSP para rellenar el formulario
                req.setAttribute("usuarioObj", u);
            } else {
                // Si el ID no existe, redirigimos al listado con mensaje de error
                String mensaje = "Usuario con ID " + id + " no encontrado.";
                resp.sendRedirect(req.getContextPath() + "/usuarios?action=list&error=" +
                        URLEncoder.encode(mensaje, "UTF-8"));
                return;
            }
        }
        // Forward al JSP que contiene el formulario
        req.getRequestDispatcher("/WEB-INF/jsp/usuario-form.jsp")
                .forward(req, resp);
    }

    /**
     * Elimina un usuario cuyo ID se pase como parámetro.
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @throws Exception si ocurre error en DAO o redirección
     */
    private void eliminarUsuario(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            dao.delete(id);
            // Redirigimos al listado con mensaje de éxito
            String mensaje = "Usuario eliminado correctamente.";
            resp.sendRedirect(req.getContextPath() + "/usuarios?action=list&success=" +
                    URLEncoder.encode(mensaje, "UTF-8"));
        } else {
            // Si no se indicó ID, redirigimos con mensaje de error
            String mensaje = "No se indicó qué usuario eliminar.";
            resp.sendRedirect(req.getContextPath() + "/usuarios?action=list&error=" +
                    URLEncoder.encode(mensaje, "UTF-8"));
        }
    }

    /**
     * Procesa la creación o edición de un usuario según si se proporciona “id”.
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @throws Exception si ocurre error en validación, DAO o redirección
     */
    private void guardarUsuario(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        // Recuperamos campos del formulario
        String idParam   = req.getParameter("id");
        String username  = req.getParameter("username").trim();
        String password  = req.getParameter("password"); // Puede venir vacío en edición
        String rol       = req.getParameter("rol");
        boolean estado   = "on".equalsIgnoreCase(req.getParameter("estado"));

        // Validaciones básicas en servidor
        String error = null;
        // Username obligatorio y mínimo 4 caracteres
        if (username.isEmpty() || username.length() < 4) {
            error = "El nombre de usuario debe tener al menos 4 caracteres.";
        }
        // En creación (sin id), la contraseña es obligatoria y mínimo 6 caracteres
        if (idParam == null && (password == null || password.length() < 6)) {
            error = "La contraseña (mínimo 6 caracteres) es obligatoria para nuevo usuario.";
        }
        if (error != null) {
            // Si hay error de validación, reabrimos el formulario mostrando el mensaje
            req.setAttribute("error", error);
            mostrarFormulario(req, resp);
            return;
        }

        // Si idParam es nulo, creamos nuevo usuario
        if (idParam == null) {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(password);  // DAO hará hash con BCrypt
            u.setRol(rol);
            u.setEstado(estado);
            // Asignamos quién creó el registro (puede ser “sistema” si no hay sesión)
            Usuario sesionUser = (Usuario) req.getSession().getAttribute("usuario");
            u.setCreadoPor(sesionUser != null ? sesionUser.getUsername() : "sistema");
            u.setFechaCreacion(LocalDateTime.now());
            dao.create(u);

            // Redirigimos al listado con mensaje de éxito
            String mensaje = "Usuario creado correctamente.";
            resp.sendRedirect(req.getContextPath() + "/usuarios?action=list&success=" +
                    URLEncoder.encode(mensaje, "UTF-8"));
        } else {
            // En caso de que idParam no sea nulo, estamos editando
            int id = Integer.parseInt(idParam);
            Usuario u = dao.findById(id);
            if (u == null) {
                // Si no existe ese ID, redirigimos con error
                String mensaje = "Usuario con ID " + id + " no existe.";
                resp.sendRedirect(req.getContextPath() + "/usuarios?action=list&error=" +
                        URLEncoder.encode(mensaje, "UTF-8"));
                return;
            }
            // Actualizamos campos básicos
            u.setUsername(username);
            u.setRol(rol);
            u.setEstado(estado);
            // Si no se escribió nueva contraseña (cadena vacía), DAO.update omitirá cambiarla
            u.setPassword(password != null ? password : "");

            // Llamamos a update, que internamente hará hash si password no está vacío
            dao.update(u);

            // Redirigimos al listado con mensaje de éxito
            String mensaje = "Usuario actualizado correctamente.";
            resp.sendRedirect(req.getContextPath() + "/usuarios?action=list&success=" +
                    URLEncoder.encode(mensaje, "UTF-8"));
        }
    }
}
