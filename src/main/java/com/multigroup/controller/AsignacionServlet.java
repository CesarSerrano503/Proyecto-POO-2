package com.multigroup.controller;

import com.multigroup.dao.AsignacionDAO;
import com.multigroup.dao.EmpleadoDAO;
import com.multigroup.model.Asignacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/asignaciones")
public class AsignacionServlet extends HttpServlet {
    private AsignacionDAO asigDao;
    private EmpleadoDAO empDao;

    @Override
    public void init() throws ServletException {
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        asigDao = new AsignacionDAO(conn);
        empDao   = new EmpleadoDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action     = req.getParameter("action");
        String idCotParam = req.getParameter("idCotizacion");
        if (idCotParam == null) {
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            return;
        }
        int idCot = Integer.parseInt(idCotParam);

        try {
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                // Listar asignaciones
                req.setAttribute("listaAsig", asigDao.findByCotizacion(idCot));
                req.setAttribute("idCotizacion", idCot);
                req.getRequestDispatcher("/WEB-INF/jsp/listaAsignaciones.jsp")
                        .forward(req, resp);

            } else if ("new".equalsIgnoreCase(action)) {
                // Formulario alta
                req.setAttribute("idCotizacion", idCot);
                req.setAttribute("empleados", empDao.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/add-asignacion.jsp")
                        .forward(req, resp);

            } else if ("edit".equalsIgnoreCase(action)) {
                // Formulario edición
                int idAsig = Integer.parseInt(req.getParameter("idAsignacion"));
                Asignacion a = asigDao.findByCotizacion(idCot).stream()
                        .filter(x -> x.getIdAsignacion() == idAsig)
                        .findFirst().orElse(null);
                req.setAttribute("asignacion", a);
                req.setAttribute("empleados", empDao.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/edit-asignacion.jsp")
                        .forward(req, resp);

            } else if ("remove".equalsIgnoreCase(action)) {
                // Eliminación física
                int idAsig = Integer.parseInt(req.getParameter("idAsignacion"));
                asigDao.remove(idAsig);
                req.getSession().setAttribute("mensaje", "Asignación eliminada.");
                resp.sendRedirect(req.getContextPath()
                        + "/asignaciones?action=list&idCotizacion=" + idCot);

            } else {
                resp.sendRedirect(req.getContextPath()
                        + "/asignaciones?action=list&idCotizacion=" + idCot);
            }

        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doGet de Asignaciones", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idCotParam = req.getParameter("idCotizacion");
        int idCot = Integer.parseInt(idCotParam);

        req.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();

        // Leer parámetros
        String idAsigParam    = req.getParameter("idAsignacion");
        String empParam       = req.getParameter("idEmpleado");
        String area           = req.getParameter("area");
        String costoHoraParam = req.getParameter("costoHora");
        String inicioParam    = req.getParameter("fechaInicio");
        String finParam       = req.getParameter("fechaFin");
        String horasEstParam  = req.getParameter("horasEstimadas");
        String titulo         = req.getParameter("tituloActividad");
        String tareas         = req.getParameter("tareas");

        // Validaciones
        if (empParam == null || empParam.isBlank()) {
            errors.add("Debe seleccionar un empleado.");
        }
        if (area == null || area.isBlank()) {
            errors.add("El área es obligatoria.");
        }

        double costoHora = 0;
        try {
            costoHora = Double.parseDouble(costoHoraParam);
            if (costoHora <= 0) errors.add("El costo por hora debe ser mayor que cero.");
        } catch (NumberFormatException e) {
            errors.add("Costo por hora inválido.");
        }

        LocalDateTime fechaInicio = null, fechaFin = null;
        try {
            fechaInicio = LocalDateTime.parse(inicioParam);
            fechaFin    = LocalDateTime.parse(finParam);
            if (fechaFin.isBefore(fechaInicio)) {
                errors.add("La fecha de fin debe ser posterior a la de inicio.");
            }
        } catch (DateTimeParseException e) {
            errors.add("Formato de fecha inválido (debe ser YYYY-MM-DDTHH:MM).");
        }

        int horasEst = 0;
        try {
            horasEst = Integer.parseInt(horasEstParam);
            if (horasEst <= 0) errors.add("Horas estimadas debe ser un entero positivo.");
        } catch (NumberFormatException e) {
            errors.add("Horas estimadas inválidas.");
        }

        if (titulo == null || titulo.isBlank()) {
            errors.add("El título de la actividad es obligatorio.");
        }

        // Costos calculados
        double costoBase    = costoHora * horasEst;
        double incrementoPct = 0; // si añades input, cambia aquí
        double total         = costoBase * (1 + incrementoPct / 100);

        // Si hay errores, reenviar al formulario
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("idCotizacion", idCot);
            try {
                req.setAttribute("empleados", empDao.findAll());
            } catch (SQLException e) {
                throw new ServletException("Error cargando empleados en doPost", e);
            }

            Asignacion a = new Asignacion();
            if (idAsigParam != null && !idAsigParam.isEmpty()) {
                a.setIdAsignacion(Integer.parseInt(idAsigParam));
            }
            a.setIdCotizacion(idCot);
            try { a.setIdEmpleado(Integer.parseInt(empParam)); } catch(Exception ignored){}
            a.setArea(area);
            a.setCostoHora(costoHora);
            a.setFechaInicio(fechaInicio);
            a.setFechaFin(fechaFin);
            a.setHorasEstimadas(horasEst);
            a.setTituloActividad(titulo);
            a.setTareas(tareas);
            a.setCostoBase(costoBase);
            a.setIncrementoPct(incrementoPct);
            a.setTotal(total);
            req.setAttribute("asignacion", a);

            String jsp = (idAsigParam == null || idAsigParam.isEmpty())
                    ? "/WEB-INF/jsp/add-asignacion.jsp"
                    : "/WEB-INF/jsp/edit-asignacion.jsp";
            req.getRequestDispatcher(jsp).forward(req, resp);
            return;
        }

        // Persistir sin errores
        try {
            boolean ok;
            Asignacion a = new Asignacion();
            a.setIdCotizacion(idCot);
            a.setIdEmpleado(Integer.parseInt(empParam));
            a.setArea(area);
            a.setCostoHora(costoHora);
            a.setFechaInicio(fechaInicio);
            a.setFechaFin(fechaFin);
            a.setHorasEstimadas(horasEst);
            a.setTituloActividad(titulo);
            a.setTareas(tareas);
            a.setCostoBase(costoBase);
            a.setIncrementoPct(incrementoPct);
            a.setTotal(total);

            if (idAsigParam == null || idAsigParam.isEmpty()) {
                ok = asigDao.insert(a);
                req.getSession().setAttribute("mensaje",
                        ok ? "Asignación creada correctamente."
                                : "Error al crear la asignación.");
            } else {
                a.setIdAsignacion(Integer.parseInt(idAsigParam));
                ok = asigDao.update(a);
                req.getSession().setAttribute("mensaje",
                        ok ? "Asignación actualizada correctamente."
                                : "Error al actualizar la asignación.");
            }
            resp.sendRedirect(req.getContextPath()
                    + "/asignaciones?action=list&idCotizacion=" + idCot);

        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doPost de Asignaciones", e);
        }
    }
}
