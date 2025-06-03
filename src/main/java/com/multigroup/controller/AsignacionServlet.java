/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * Servlet para gestionar operaciones CRUD sobre Asignaciones vinculadas a Cotizaciones.
 * - doGet: muestra listado, formulario de creación, edición o elimina una asignación.
 * - doPost: valida datos del formulario y persiste (crea o actualiza) la asignación.
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

    // DAO para operaciones sobre la tabla de Asignaciones
    private AsignacionDAO asigDao;
    // DAO para operaciones sobre la tabla de Empleados (para poblar selectores)
    private EmpleadoDAO empDao;

    /**
     * Inicializa los DAOs obteniendo la conexión de la aplicación.
     */
    @Override
    public void init() throws ServletException {
        // Recupera la conexión a BD guardada en el contexto de la aplicación
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        // Instancia los DAOs con la conexión
        asigDao = new AsignacionDAO(conn);
        empDao  = new EmpleadoDAO(conn);
    }

    /**
     * Maneja peticiones GET para listar, mostrar formulario de nuevo, editar o eliminar asignaciones.
     *
     * Parámetros esperados:
     * - action (opcional): "list" (por defecto), "new", "edit" o "remove".
     * - idCotizacion: ID de la cotización a la que pertenecen las asignaciones.
     * - idAsignacion (solo para "edit" y "remove"): ID de la asignación a editar/eliminar.
     *
     * Redirige al listado de cotizaciones si falta idCotizacion.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Obtiene acción y parámetro idCotizacion desde la URL
        String action     = req.getParameter("action");
        String idCotParam = req.getParameter("idCotizacion");

        // Si no se provee idCotizacion, redirige al listado de cotizaciones
        if (idCotParam == null) {
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            return;
        }
        int idCot = Integer.parseInt(idCotParam);

        try {
            // Si action es null, vacío o "list", muestra el listado de asignaciones
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                // Recupera todas las asignaciones para la cotización
                List<Asignacion> lista = asigDao.findByCotizacion(idCot);
                // Recupera todos los empleados para el menú de selección
                List<Empleado> empleados = empDao.findAll();

                // Agrega atributos al request para la JSP
                req.setAttribute("listaAsig", lista);
                req.setAttribute("empleados", empleados);
                req.setAttribute("idCotizacion", idCot);
                // Redirige a la vista de listado
                req.getRequestDispatcher("/WEB-INF/jsp/listaAsignaciones.jsp")
                        .forward(req, resp);

            }
            // Si action es "new", muestra formulario para crear nueva asignación
            else if ("new".equalsIgnoreCase(action)) {
                List<Empleado> empleados = empDao.findAll();
                req.setAttribute("idCotizacion", idCot);
                req.setAttribute("empleados", empleados);
                req.getRequestDispatcher("/WEB-INF/jsp/add-asignacion.jsp")
                        .forward(req, resp);

            }
            // Si action es "edit", muestra formulario para editar asignación existente
            else if ("edit".equalsIgnoreCase(action)) {
                int idAsig = Integer.parseInt(req.getParameter("idAsignacion"));
                // Busca la asignación dentro del listado de la cotización
                Asignacion a = asigDao.findByCotizacion(idCot).stream()
                        .filter(x -> x.getIdAsignacion() == idAsig)
                        .findFirst().orElse(null);
                List<Empleado> empleados = empDao.findAll();

                req.setAttribute("asignacion", a);
                req.setAttribute("empleados", empleados);
                req.setAttribute("idCotizacion", idCot);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-asignacion.jsp")
                        .forward(req, resp);

            }
            // Si action es "remove", elimina asignación y redirige al listado
            else if ("remove".equalsIgnoreCase(action)) {
                int idAsig = Integer.parseInt(req.getParameter("idAsignacion"));
                boolean deleted = asigDao.remove(idAsig);

                // Mensaje de éxito o fracaso en sesión
                req.getSession().setAttribute("mensaje",
                        deleted
                                ? "Asignación eliminada correctamente."
                                : "No se encontró la asignación para eliminar.");
                resp.sendRedirect(req.getContextPath()
                        + "/asignaciones?action=list&idCotizacion=" + idCot);

            }
            // Para cualquier otro action inválido, redirige al listado
            else {
                resp.sendRedirect(req.getContextPath()
                        + "/asignaciones?action=list&idCotizacion=" + idCot);
            }
        } catch (SQLException e) {
            // Envía error de servlet si hay problemas en BD
            throw new ServletException("Error accediendo a la base de datos en doGet de Asignaciones", e);
        }
    }

    /**
     * Maneja peticiones POST para crear o actualizar una asignación.
     *
     * Parámetros esperados:
     * - idCotizacion (obligatorio).
     * - idAsignacion (opcional; si viene vacío => creación, si viene => edición).
     * - idEmpleado, area, costoHora, fechaInicio, fechaFin, horasEstimadas, tituloActividad, tareas.
     *
     * Valida cada campo, recalcula costos y persiste la entidad en BD.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Parseo inicial de idCotización y ajuste de codificación
        int idCot = Integer.parseInt(req.getParameter("idCotizacion"));
        req.setCharacterEncoding("UTF-8");

        // Captura de parámetros del formulario
        String idAsigParam    = req.getParameter("idAsignacion");
        String empParam       = req.getParameter("idEmpleado");
        String area           = req.getParameter("area");
        String costoHoraParam = req.getParameter("costoHora");
        String inicioParam    = req.getParameter("fechaInicio");
        String finParam       = req.getParameter("fechaFin");
        String horasEstParam  = req.getParameter("horasEstimadas");
        String titulo         = req.getParameter("tituloActividad");
        String tareas         = req.getParameter("tareas");

        // Lista de errores para validaciones
        List<String> errors = new ArrayList<>();

        // Validación de empleado
        if (empParam == null || empParam.isBlank()) {
            errors.add("Debe seleccionar un empleado.");
        }
        // Validación de área
        if (area == null || area.isBlank()) {
            errors.add("El área es obligatoria.");
        }

        // Validación y parseo de costo por hora
        double costoHora = 0;
        try {
            costoHora = Double.parseDouble(costoHoraParam);
            if (costoHora <= 0) {
                errors.add("El costo por hora debe ser mayor que cero.");
            }
        } catch (Exception e) {
            errors.add("Costo por hora inválido.");
        }

        // Validación y parseo de fechas
        LocalDateTime fechaInicio = null, fechaFin = null;
        try {
            fechaInicio = LocalDateTime.parse(inicioParam);
            fechaFin    = LocalDateTime.parse(finParam);
            if (fechaFin.isBefore(fechaInicio)) {
                errors.add("La fecha de fin debe ser posterior a la de inicio.");
            }
        } catch (DateTimeParseException e) {
            errors.add("Formato de fecha inválido (YYYY-MM-DDTHH:MM).");
        }

        // Validación y parseo de horas estimadas
        int horasEst = 0;
        try {
            horasEst = Integer.parseInt(horasEstParam);
            if (horasEst <= 0) {
                errors.add("Horas estimadas debe ser un entero positivo.");
            }
        } catch (Exception e) {
            errors.add("Horas estimadas inválidas.");
        }

        // Validación de título de actividad
        if (titulo == null || titulo.isBlank()) {
            errors.add("El título de la actividad es obligatorio.");
        }

        // Cálculo de costos base y total (sin incremento de momento)
        double costoBase     = costoHora * horasEst;
        double incrementoPct = 0; // Por ahora siempre 0; se puede modificar si hay reglas de negocio
        double total         = costoBase * (1 + incrementoPct / 100);

        // Si hay errores, vuelve a mostrar el formulario con mensajes
        if (!errors.isEmpty()) {
            try {
                // Para repoblar la lista de empleados en el formulario
                req.setAttribute("empleados", empDao.findAll());
            } catch (SQLException ex) {
                throw new ServletException("Error cargando empleados para formulario de error", ex);
            }
            req.setAttribute("errors", errors);
            req.setAttribute("idCotizacion", idCot);

            // Reconstrucción de objeto Asignación para rellenar campos del formulario
            Asignacion a = new Asignacion();
            if (idAsigParam != null && !idAsigParam.isEmpty()) {
                a.setIdAsignacion(Integer.parseInt(idAsigParam));
            }
            a.setIdCotizacion(idCot);
            if (empParam != null && !empParam.isEmpty()) {
                a.setIdEmpleado(Integer.parseInt(empParam));
            }
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

            // Decide a qué JSP reenviar: creación o edición
            String jsp = (idAsigParam == null || idAsigParam.isEmpty())
                    ? "/WEB-INF/jsp/add-asignacion.jsp"
                    : "/WEB-INF/jsp/edit-asignacion.jsp";
            req.getRequestDispatcher(jsp).forward(req, resp);
            return;
        }

        // Si no hay errores, se persisten los datos en BD
        try {
            Asignacion a = new Asignacion();
            // Si viene idAsignacion, es actualización; si no, es creación
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

            // Se fijan los campos calculados antes de llamar al DAO
            a.setCostoBase(costoBase);
            a.setIncrementoPct(incrementoPct);
            a.setTotal(total);

            boolean ok;
            if (idAsigParam == null || idAsigParam.isEmpty()) {
                // Inserta nueva asignación
                ok = asigDao.insert(a);
                req.getSession().setAttribute("mensaje",
                        ok ? "Asignación creada correctamente." : "Error al crear la asignación.");
            } else {
                // Actualiza asignación existente
                ok = asigDao.update(a);
                req.getSession().setAttribute("mensaje",
                        ok ? "Asignación actualizada correctamente." : "Error al actualizar la asignación.");
            }

            // Redirige al listado de asignaciones de la cotización
            resp.sendRedirect(req.getContextPath()
                    + "/asignaciones?action=list&idCotizacion=" + idCot);

        } catch (SQLException e) {
            // Si ocurre error en BD durante persistencia, lanza ServletException
            throw new ServletException("Error accediendo a la base de datos en doPost de Asignaciones", e);
        }
    }
}
