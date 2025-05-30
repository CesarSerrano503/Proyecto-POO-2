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
    private SubtareaDAO subDao;
    private AsignacionDAO asigDao;

    @Override
    public void init() throws ServletException {
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        subDao  = new SubtareaDAO(conn);
        asigDao = new AsignacionDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action     = req.getParameter("action");
        String rawIdAsig  = req.getParameter("idAsignacion");
        if (rawIdAsig == null || rawIdAsig.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            return;
        }

        int idAsig;
        try {
            idAsig = Integer.parseInt(rawIdAsig.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            return;
        }

        try {
            switch ((action == null) ? "list" : action.toLowerCase()) {
                case "new": {
                    Asignacion padre = asigDao.findById(idAsig);
                    req.setAttribute("idAsignacion", idAsig);
                    req.setAttribute("idCotizacion", padre != null ? padre.getIdCotizacion() : 0);
                    req.getRequestDispatcher("/WEB-INF/jsp/add-subtarea.jsp")
                            .forward(req, resp);
                    break;
                }
                case "edit": {
                    String rawIdSubE = req.getParameter("idSubtarea");
                    int idSubE = Integer.parseInt(rawIdSubE.trim());
                    Subtarea sEdit = subDao.findByAsignacion(idAsig).stream()
                            .filter(s -> s.getIdSubtarea() == idSubE)
                            .findFirst().orElse(null);
                    Asignacion padre = asigDao.findById(idAsig);
                    req.setAttribute("subtarea", sEdit);
                    req.setAttribute("idAsignacion", idAsig);
                    req.setAttribute("idCotizacion", padre != null ? padre.getIdCotizacion() : 0);
                    req.getRequestDispatcher("/WEB-INF/jsp/edit-subtarea.jsp")
                            .forward(req, resp);
                    break;
                }
                case "remove": {
                    String rawIdSubR = req.getParameter("idSubtarea");
                    int idSubR = Integer.parseInt(rawIdSubR.trim());
                    boolean deleted = subDao.remove(idSubR);
                    req.getSession().setAttribute("mensaje",
                            deleted ? "Subtarea eliminada correctamente."
                                    : "No se encontró la subtarea.");
                    resp.sendRedirect(req.getContextPath()
                            + "/subtareas?action=list&idAsignacion=" + idAsig);
                    break;
                }
                case "list":
                default: {
                    List<Subtarea> lista = subDao.findByAsignacion(idAsig);
                    Asignacion padre = asigDao.findById(idAsig);
                    req.setAttribute("listaSub",      lista);
                    req.setAttribute("idAsignacion",  idAsig);
                    req.setAttribute("idCotizacion",  padre != null ? padre.getIdCotizacion() : 0);
                    req.getRequestDispatcher("/WEB-INF/jsp/listaSubtareas.jsp")
                            .forward(req, resp);
                    break;
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doGet de Subtareas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String rawIdAsig = req.getParameter("idAsignacion");
        int idAsig;
        try {
            idAsig = Integer.parseInt(rawIdAsig.trim());
        } catch (Exception e) {
            throw new ServletException("ID de asignación inválido", e);
        }

        String rawIdSub   = req.getParameter("idSubtarea");
        String titulo     = req.getParameter("tituloSubtarea");
        String descripcion= req.getParameter("descripcionSubtarea");

        List<String> errors = new java.util.ArrayList<>();
        if (titulo == null || titulo.isBlank())     errors.add("El título es obligatorio.");
        if (descripcion == null || descripcion.isBlank()) errors.add("La descripción es obligatoria.");

        if (!errors.isEmpty()) {
            int idCot = 0;
            try {
                Asignacion padre = asigDao.findById(idAsig);
                idCot = (padre != null) ? padre.getIdCotizacion() : 0;
            } catch (SQLException ex) {
                throw new ServletException("Error cargando cotización padre", ex);
            }
            req.setAttribute("errors", errors);
            req.setAttribute("idAsignacion", idAsig);
            req.setAttribute("idCotizacion", idCot);

            if (rawIdSub != null && !rawIdSub.isBlank()) {
                Subtarea s = new Subtarea();
                s.setIdSubtarea(Integer.parseInt(rawIdSub.trim()));
                s.setIdAsignacion(idAsig);
                s.setTituloSubtarea(titulo);
                s.setDescripcionSubtarea(descripcion);
                req.setAttribute("subtarea", s);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-subtarea.jsp")
                        .forward(req, resp);
            } else {
                req.getRequestDispatcher("/WEB-INF/jsp/add-subtarea.jsp")
                        .forward(req, resp);
            }
            return;
        }

        try {
            Subtarea s = new Subtarea();
            if (rawIdSub != null && !rawIdSub.isBlank()) {
                s.setIdSubtarea(Integer.parseInt(rawIdSub.trim()));
            }
            s.setIdAsignacion(idAsig);
            s.setTituloSubtarea(titulo);
            s.setDescripcionSubtarea(descripcion);

            boolean ok = (rawIdSub == null || rawIdSub.isBlank())
                    ? subDao.insert(s)
                    : subDao.update(s);

            req.getSession().setAttribute("mensaje",
                    ok ? (rawIdSub == null
                            ? "Subtarea creada correctamente."
                            : "Subtarea actualizada correctamente.")
                            : "Error al guardar la subtarea.");

            resp.sendRedirect(req.getContextPath()
                    + "/subtareas?action=list&idAsignacion=" + idAsig);

        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doPost de Subtareas", e);
        }
    }
}
