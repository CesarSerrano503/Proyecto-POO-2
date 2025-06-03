/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * SubtareaServlet: gestiona operaciones CRUD sobre Subtareas vinculadas a Asignaciones.
 * - init: inicializa los DAOs usando la conexión compartida.
 * - doGet: muestra listado, formulario de creación, edición o eliminación de subtareas.
 * - doPost: valida datos del formulario (título y descripción) y persiste (crea o actualiza) la subtarea.
 */
package com.multigroup.controller;

import com.multigroup.dao.AsignacionDAO;
import com.multigroup.dao.SubtareaDAO;
import com.multigroup.model.Asignacion;
import com.multigroup.model.Subtarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/subtareas")
public class SubtareaServlet extends HttpServlet {
    // DAO para operaciones sobre la tabla de Subtareas
    private SubtareaDAO subDao;
    // DAO para obtener información de la Asignación padre (para conocer idCotización)
    private AsignacionDAO asigDao;

    /**
     * Inicializa los DAOs obteniendo la conexión almacenada en el contexto.
     */
    @Override
    public void init() throws ServletException {
        // Recupera la conexión a BD desde el contexto de la aplicación
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        // Inicializa DAOs con la conexión
        subDao  = new SubtareaDAO(conn);
        asigDao = new AsignacionDAO(conn);
    }

    /**
     * Maneja peticiones GET para listar subtareas, mostrar formularios de creación/edición
     * o eliminar subtareas según el parámetro "action".
     *
     * Parámetros esperados:
     * - action (opcional): "list" (por defecto), "new", "edit" o "remove".
     * - idAsignacion (obligatorio): ID de la asignación a la que pertenecen las subtareas.
     * - idSubtarea (solo para "edit" o "remove"): ID de la subtarea específica.
     *
     * @param req  HttpServletRequest con parámetros de la petición.
     * @param resp HttpServletResponse para enviar la respuesta o redirección.
     * @throws ServletException si ocurre error en el servlet o en BD.
     * @throws IOException      si ocurre error de E/S al redirigir o forward.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action     = req.getParameter("action");
        String rawIdAsig  = req.getParameter("idAsignacion");

        // Si no se suministra idAsignacion, redirige al listado de cotizaciones
        if (rawIdAsig == null || rawIdAsig.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            return;
        }

        int idAsig;
        try {
            // Convierte idAsignacion a entero
            idAsig = Integer.parseInt(rawIdAsig.trim());
        } catch (NumberFormatException e) {
            // Si no es número válido, redirige al listado de cotizaciones
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            return;
        }

        try {
            // Determina acción (por defecto "list" si action es null)
            switch ((action == null) ? "list" : action.toLowerCase()) {
                case "new": {
                    // FORMULARIO DE CREACIÓN: obtén asignación padre para saber idCotización
                    Asignacion padre = asigDao.findById(idAsig);
                    req.setAttribute("idAsignacion", idAsig);
                    req.setAttribute("idCotizacion", padre != null ? padre.getIdCotizacion() : 0);
                    // Forward al JSP de agregar subtarea
                    req.getRequestDispatcher("/WEB-INF/jsp/add-subtarea.jsp")
                            .forward(req, resp);
                    break;
                }
                case "edit": {
                    // FORMULARIO DE EDICIÓN: lee idSubtarea y busca la entidad correspondiente
                    String rawIdSubE = req.getParameter("idSubtarea");
                    int idSubE = Integer.parseInt(rawIdSubE.trim());
                    // Busca la subtarea dentro de la lista de esta asignación
                    Subtarea sEdit = subDao.findByAsignacion(idAsig).stream()
                            .filter(s -> s.getIdSubtarea() == idSubE)
                            .findFirst().orElse(null);
                    // Obtiene asignación padre para idCotización
                    Asignacion padre = asigDao.findById(idAsig);
                    req.setAttribute("subtarea", sEdit);
                    req.setAttribute("idAsignacion", idAsig);
                    req.setAttribute("idCotizacion", padre != null ? padre.getIdCotizacion() : 0);
                    // Forward al JSP de edición de subtarea
                    req.getRequestDispatcher("/WEB-INF/jsp/edit-subtarea.jsp")
                            .forward(req, resp);
                    break;
                }
                case "remove": {
                    // ELIMINAR SUBTAREA: lee idSubtarea y llama a DAO.remove
                    String rawIdSubR = req.getParameter("idSubtarea");
                    int idSubR = Integer.parseInt(rawIdSubR.trim());
                    boolean deleted = subDao.remove(idSubR);
                    // Mensaje de éxito o fallo en sesión
                    req.getSession().setAttribute("mensaje",
                            deleted ? "Subtarea eliminada correctamente."
                                    : "No se encontró la subtarea.");
                    // Redirige al listado de subtareas de esta asignación
                    resp.sendRedirect(req.getContextPath()
                            + "/subtareas?action=list&idAsignacion=" + idAsig);
                    break;
                }
                case "list":
                default: {
                    // LISTADO DE SUBTAREAS: obtiene todas las subtareas para la asignación
                    List<Subtarea> lista = subDao.findByAsignacion(idAsig);
                    Asignacion padre = asigDao.findById(idAsig);
                    req.setAttribute("listaSub",      lista);
                    req.setAttribute("idAsignacion",  idAsig);
                    req.setAttribute("idCotizacion",  padre != null ? padre.getIdCotizacion() : 0);
                    // Forward al JSP de listado de subtareas
                    req.getRequestDispatcher("/WEB-INF/jsp/listaSubtareas.jsp")
                            .forward(req, resp);
                    break;
                }
            }
        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException
            throw new ServletException("Error accediendo a la BD en doGet de Subtareas", e);
        }
    }

    /**
     * Maneja peticiones POST para crear o actualizar una subtarea.
     * Valida que título y descripción no estén vacíos. Si hay errores, reenvía al JSP correspondiente.
     * Si pasa validación, persiste la subtarea (insert o update) y redirige al listado.
     *
     * Parámetros esperados:
     * - idAsignacion (obligatorio): ID de la asignación padre.
     * - idSubtarea (opcional; si viene vacío => creación, si viene => edición).
     * - tituloSubtarea, descripcionSubtarea (obligatorios).
     *
     * @param req  HttpServletRequest con parámetros del formulario.
     * @param resp HttpServletResponse para respuestas o redirecciones.
     * @throws ServletException si ocurre error en el servlet o en BD.
     * @throws IOException      si ocurre error de E/S al redirigir o forward.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Asegura codificación UTF-8 para caracteres especiales
        req.setCharacterEncoding("UTF-8");

        // 1) Leer idAsignacion y convertir a entero
        String rawIdAsig = req.getParameter("idAsignacion");
        int idAsig;
        try {
            idAsig = Integer.parseInt(rawIdAsig.trim());
        } catch (Exception e) {
            // Si idAsignacion no es válido, lanza excepción que resultará en error 500
            throw new ServletException("ID de asignación inválido", e);
        }

        // 2) Leer parámetros opcionales para edición o creación
        String rawIdSub    = req.getParameter("idSubtarea");
        String titulo      = req.getParameter("tituloSubtarea");
        String descripcion = req.getParameter("descripcionSubtarea");

        // 3) Validaciones: título y descripción no vacíos
        List<String> errors = new java.util.ArrayList<>();
        if (titulo == null || titulo.isBlank()) {
            errors.add("El título es obligatorio.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            errors.add("La descripción es obligatoria.");
        }

        // Si hay errores, volver al formulario (edición o creación según rawIdSub)
        if (!errors.isEmpty()) {
            int idCot = 0;
            try {
                // Obtener idCotizacion de la asignación padre para reenviar al JSP
                Asignacion padre = asigDao.findById(idAsig);
                idCot = (padre != null) ? padre.getIdCotizacion() : 0;
            } catch (SQLException ex) {
                throw new ServletException("Error cargando cotización padre", ex);
            }
            // Asigna lista de errores y datos de asignación/cotización al request
            req.setAttribute("errors", errors);
            req.setAttribute("idAsignacion", idAsig);
            req.setAttribute("idCotizacion", idCot);

            if (rawIdSub != null && !rawIdSub.isBlank()) {
                // Si viene idSubtarea, es edición: reconstruir objeto Subtarea para rellenar campos
                Subtarea s = new Subtarea();
                s.setIdSubtarea(Integer.parseInt(rawIdSub.trim()));
                s.setIdAsignacion(idAsig);
                s.setTituloSubtarea(titulo);
                s.setDescripcionSubtarea(descripcion);
                req.setAttribute("subtarea", s);
                // Forward al JSP de edición de subtarea
                req.getRequestDispatcher("/WEB-INF/jsp/edit-subtarea.jsp")
                        .forward(req, resp);
            } else {
                // Si no hay idSubtarea, es creación: forward al JSP de agregar subtarea
                req.getRequestDispatcher("/WEB-INF/jsp/add-subtarea.jsp")
                        .forward(req, resp);
            }
            return;
        }

        // 4) Si pasa validación, construir objeto Subtarea y persistir
        try {
            Subtarea s = new Subtarea();
            // Si rawIdSub existe, es edición; asignar ID para update
            if (rawIdSub != null && !rawIdSub.isBlank()) {
                s.setIdSubtarea(Integer.parseInt(rawIdSub.trim()));
            }
            // Campos comunes: asignación padre, título y descripción
            s.setIdAsignacion(idAsig);
            s.setTituloSubtarea(titulo);
            s.setDescripcionSubtarea(descripcion);

            // Determinar operación: insert o update
            boolean ok = (rawIdSub == null || rawIdSub.isBlank())
                    ? subDao.insert(s)
                    : subDao.update(s);

            // Mensaje de éxito o fallo en sesión según operación
            req.getSession().setAttribute("mensaje",
                    ok ? (rawIdSub == null
                            ? "Subtarea creada correctamente."
                            : "Subtarea actualizada correctamente.")
                            : "Error al guardar la subtarea.");

            // Redirige al listado de subtareas de esta asignación
            resp.sendRedirect(req.getContextPath()
                    + "/subtareas?action=list&idAsignacion=" + idAsig);

        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException con mensaje
            throw new ServletException("Error accediendo a la BD en doPost de Subtareas", e);
        }
    }
}
