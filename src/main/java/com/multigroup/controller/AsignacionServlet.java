/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 */
package com.multigroup.controller;

import com.multigroup.dao.AsignacionDAO;
import com.multigroup.dao.EmpleadoDAO;
import com.multigroup.model.Asignacion;
import com.multigroup.model.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        empDao  = new EmpleadoDAO(conn);
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
                List<Asignacion> lista = asigDao.findByCotizacion(idCot);
                List<Empleado> empleados = empDao.findAll();

                req.setAttribute("listaAsig", lista);
                req.setAttribute("empleados", empleados);
                req.setAttribute("idCotizacion", idCot);
                req.getRequestDispatcher("/WEB-INF/jsp/listaAsignaciones.jsp")
                        .forward(req, resp);

            } else if ("new".equalsIgnoreCase(action)) {
                List<Empleado> empleados = empDao.findAll();
                req.setAttribute("idCotizacion", idCot);
                req.setAttribute("empleados", empleados);
                req.getRequestDispatcher("/WEB-INF/jsp/add-asignacion.jsp")
                        .forward(req, resp);

            } else if ("edit".equalsIgnoreCase(action)) {
                int idAsig = Integer.parseInt(req.getParameter("idAsignacion"));
                Asignacion a = asigDao.findByCotizacion(idCot).stream()
                        .filter(x -> x.getIdAsignacion() == idAsig)
                        .findFirst().orElse(null);
                List<Empleado> empleados = empDao.findAll();

                req.setAttribute("asignacion", a);
                req.setAttribute("empleados", empleados);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-asignacion.jsp")
                        .forward(req, resp);

            } else if ("remove".equalsIgnoreCase(action)) {
                int idAsig = Integer.parseInt(req.getParameter("idAsignacion"));
                boolean deleted = asigDao.remove(idAsig);

                req.getSession().setAttribute("mensaje",
                        deleted
                                ? "Asignación eliminada correctamente."
                                : "No se encontró la asignación para eliminar.");
                resp.sendRedirect(req.getContextPath()
                        + "/asignaciones?action=list&idCotizacion=" + idCot);

            } else {
                resp.sendRedirect(req.getContextPath()
                        + "/asignaciones?action=list&idCotizacion=" + idCot);
            }
        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la base de datos en doGet de Asignaciones", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idCot = Integer.parseInt(req.getParameter("idCotizacion"));
        req.setCharacterEncoding("UTF-8");

        // Parámetros
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
        List<String> errors = new ArrayList<>();
        if (empParam   == null || empParam.isBlank()) errors.add("Debe seleccionar un empleado.");
        if (area       == null || area.isBlank())    errors.add("El área es obligatoria.");

        double costoHora = 0;
        try {
            costoHora = Double.parseDouble(costoHoraParam);
            if (costoHora <= 0) errors.add("El costo por hora debe ser mayor que cero.");
        } catch (Exception e) {
            errors.add("Costo por hora inválido.");
        }

        LocalDateTime fechaInicio = null, fechaFin = null;
        try {
            fechaInicio = LocalDateTime.parse(inicioParam);
            fechaFin    = LocalDateTime.parse(finParam);
            if (fechaFin.isBefore(fechaInicio))
                errors.add("La fecha de fin debe ser posterior a la de inicio.");
        } catch (DateTimeParseException e) {
            errors.add("Formato de fecha inválido (YYYY-MM-DDTHH:MM).");
        }

        int horasEst = 0;
        try {
            horasEst = Integer.parseInt(horasEstParam);
            if (horasEst <= 0) errors.add("Horas estimadas debe ser un entero positivo.");
        } catch (Exception e) {
            errors.add("Horas estimadas inválidas.");
        }

        if (titulo == null || titulo.isBlank())
            errors.add("El título de la actividad es obligatorio.");

        // Calcular costos
        double costoBase    = costoHora * horasEst;
        double incrementoPct = 0;
        double total         = costoBase * (1 + incrementoPct / 100);

        // Si hay errores, reenvío
        if (!errors.isEmpty()) {
            try {
                req.setAttribute("empleados", empDao.findAll());
            } catch (SQLException ex) {
                throw new ServletException("Error cargando empleados para formulario de error", ex);
            }
            req.setAttribute("errors", errors);
            req.setAttribute("idCotizacion", idCot);
            Asignacion a = new Asignacion();
            if (idAsigParam != null && !idAsigParam.isEmpty())
                a.setIdAsignacion(Integer.parseInt(idAsigParam));
            a.setIdCotizacion(idCot);
            if (empParam != null) a.setIdEmpleado(Integer.parseInt(empParam));
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

        // Persistir
        try {
            Asignacion a = new Asignacion();
            if (idAsigParam != null && !idAsigParam.isEmpty()) {
                a.setIdAsignacion(Integer.parseInt(idAsigParam));
            }
            a.setIdCotizacion(idCot);
            a.setIdEmpleado(Integer.parseInt(empParam));
            a.setArea(area);
            a.setCostoHora(costoHora);
            a.setFechaInicio(fechaInicio);
            a.setFechaFin(fechaFin);
            a.setHorasEstimadas(horasEst);
            a.setTituloActividad(titulo);
            a.setTareas(tareas);

            // **¡Aquí nos aseguramos de fijar el costoBase, incrementoPct y total antes de guardar!**
            a.setCostoBase(costoBase);
            a.setIncrementoPct(incrementoPct);
            a.setTotal(total);

            boolean ok;
            if (idAsigParam == null || idAsigParam.isEmpty()) {
                ok = asigDao.insert(a);
                req.getSession().setAttribute("mensaje",
                        ok ? "Asignación creada correctamente." : "Error al crear la asignación.");
            } else {
                ok = asigDao.update(a);
                req.getSession().setAttribute("mensaje",
                        ok ? "Asignación actualizada correctamente." : "Error al actualizar la asignación.");
            }

            resp.sendRedirect(req.getContextPath()
                    + "/asignaciones?action=list&idCotizacion=" + idCot);

        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la base de datos en doPost de Asignaciones", e);
        }
    }
}
