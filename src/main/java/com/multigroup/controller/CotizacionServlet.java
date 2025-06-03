/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 28/5/2025
 *
 * Servlet para gestionar operaciones CRUD sobre Cotizaciones.
 * - doGet: muestra listado, formulario de creación, edición, inactivación, finalización o eliminación física.
 * - doPost: valida datos del formulario y persiste (crea o actualiza) la cotización.
 */
package com.multigroup.controller;

import com.multigroup.dao.CotizacionDAO;
import com.multigroup.dao.ClienteDAO;
import com.multigroup.model.Cotizacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cotizaciones")
public class CotizacionServlet extends HttpServlet {
    // DAO para operaciones sobre la tabla de Cotizaciones
    private CotizacionDAO cotDao;
    // DAO para obtener lista de clientes (para select en formularios)
    private ClienteDAO   clienteDao;

    /**
     * Inicializa los DAOs obteniendo la conexión de la aplicación.
     */
    @Override
    public void init() throws ServletException {
        // Recupera la conexión a BD guardada en el contexto de la aplicación
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        // Instancia los DAOs con la conexión
        cotDao     = new CotizacionDAO(conn);
        clienteDao = new ClienteDAO(conn);
    }

    /**
     * Maneja peticiones GET para listar, mostrar formularios de nuevo/edición,
     * inactivar, finalizar o eliminar permanentemente cotizaciones.
     *
     * Parámetros esperados:
     * - action (opcional): "list" (por defecto), "new", "edit", "delete", "finalize", "remove".
     * - idCotizacion (solo para "edit", "delete", "finalize" y "remove").
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            // 1) LISTADO: action nulo, vacío o "list"
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                // Recupera todas las cotizaciones (incluye estados según DAO)
                List<Cotizacion> lista = cotDao.findAll();
                // Asigna lista al request para la JSP
                req.setAttribute("listaCot", lista);
                // Forward a la vista de listado
                req.getRequestDispatcher("/WEB-INF/jsp/listaCotizaciones.jsp")
                        .forward(req, resp);

            }
            // 2) FORMULARIO ALTA: action "new"
            else if ("new".equalsIgnoreCase(action)) {
                // Obtiene lista de clientes para el select en el formulario
                req.setAttribute("listaClientes", clienteDao.findAll());
                // Crea un objeto Cotizacion vacío para evitar NPE en JSP
                req.setAttribute("cotizacion", new Cotizacion());
                req.getRequestDispatcher("/WEB-INF/jsp/add-cotizacion.jsp")
                        .forward(req, resp);

            }
            // 3) FORMULARIO EDICIÓN: action "edit"
            else if ("edit".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCotizacion"));
                // Obtiene la cotización por ID
                Cotizacion c = cotDao.findById(id);
                req.setAttribute("cotizacion", c);
                // Lista de clientes para el select, para permitir cambiar cliente
                req.setAttribute("listaClientes", clienteDao.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/edit-cotizacion.jsp")
                        .forward(req, resp);

            }
            // 4) BAJA LÓGICA: action "delete"
            else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCotizacion"));
                cotDao.delete(id);
                // Mensaje de inactivación en sesión
                req.getSession().setAttribute("mensaje", "Cotización inactivada.");
                resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");

            }
            // 5) MARCAR FINALIZADA: action "finalize"
            else if ("finalize".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCotizacion"));
                cotDao.finalize(id);
                // Mensaje de finalización en sesión
                req.getSession().setAttribute("mensaje", "Cotización finalizada.");
                resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");

            }
            // 6) ELIMINACIÓN FÍSICA: action "remove"
            else if ("remove".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCotizacion"));
                cotDao.remove(id);
                // Mensaje de eliminación permanente en sesión
                req.getSession().setAttribute("mensaje", "Cotización eliminada permanentemente.");
                resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");

            }
            // Cualquier otro action inválido, redirige a listado
            else {
                resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
            }
        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException
            throw new ServletException("Error en operación GET de Cotizaciones", e);
        }
    }

    /**
     * Maneja peticiones POST para crear o actualizar una cotización.
     *
     * Parámetros esperados:
     * - idCotizacion (opcional; si viene vacío => creación, si viene => edición).
     * - idCliente, totalHoras, fechaInicio, fechaFin, costoAsignaciones, costosAdicionales, total, creadoPor.
     *
     * Valida cada campo, verifica coherencia de fechas, convierte tipos y persiste la entidad en BD.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Asegurar codificación UTF-8 para caracteres especiales
        req.setCharacterEncoding("UTF-8");
        // Lista para acumular mensajes de error de validación
        List<String> errors = new ArrayList<>();

        // 1) Leer parámetros crudos del formulario
        String idParam           = req.getParameter("idCotizacion");
        String cliParam          = req.getParameter("idCliente");
        String totalHorasParam   = req.getParameter("totalHoras");
        String fechaIniParam     = req.getParameter("fechaInicio");
        String fechaFinParam     = req.getParameter("fechaFin");
        String costoAsigParam    = req.getParameter("costoAsignaciones");
        String costosAdicParam   = req.getParameter("costosAdicionales");
        String totalParam        = req.getParameter("total");
        String creadoPorParam    = req.getParameter("creadoPor");

        // 2) Variables para conversión de tipos
        Integer       idCliente    = null;
        BigDecimal    totalHoras   = null;
        Timestamp     tsInicio     = null;
        Timestamp     tsFin        = null;
        BigDecimal    costoAsig    = null;
        BigDecimal    costosAdic   = null;
        BigDecimal    total        = null;

        // --- Validación y conversión de idCliente ---
        if (cliParam == null || cliParam.isBlank()) {
            errors.add("Debe seleccionar un cliente.");
        } else {
            try {
                idCliente = Integer.parseInt(cliParam);
            } catch (NumberFormatException e) {
                errors.add("Cliente inválido.");
            }
        }

        // --- Validación y conversión de totalHoras ---
        if (totalHorasParam == null || totalHorasParam.isBlank()) {
            errors.add("Total de horas es obligatorio.");
        } else {
            try {
                totalHoras = new BigDecimal(totalHorasParam);
                if (totalHoras.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("Total de horas debe ser mayor que cero.");
                }
            } catch (Exception e) {
                errors.add("Formato de total de horas inválido.");
            }
        }

        // --- Validación y conversión de fechaInicio a Timestamp ---
        if (fechaIniParam == null || fechaIniParam.isBlank()) {
            errors.add("Fecha de inicio es obligatoria.");
        } else {
            try {
                LocalDateTime lt = LocalDateTime.parse(fechaIniParam);
                tsInicio = Timestamp.valueOf(lt);
            } catch (DateTimeParseException e) {
                errors.add("Formato de fecha de inicio inválido.");
            }
        }

        // --- Validación y conversión de fechaFin a Timestamp ---
        if (fechaFinParam == null || fechaFinParam.isBlank()) {
            errors.add("Fecha de fin es obligatoria.");
        } else {
            try {
                LocalDateTime lt = LocalDateTime.parse(fechaFinParam);
                tsFin = Timestamp.valueOf(lt);
            } catch (DateTimeParseException e) {
                errors.add("Formato de fecha de fin inválido.");
            }
        }

        // --- Validación de coherencia entre fechas ---
        if (tsInicio != null && tsFin != null && tsFin.before(tsInicio)) {
            errors.add("La fecha de fin debe ser posterior a la de inicio.");
        }

        // --- Validación y conversión de costoAsignaciones ---
        if (costoAsigParam == null || costoAsigParam.isBlank()) {
            errors.add("Costo de asignaciones es obligatorio.");
        } else {
            try {
                costoAsig = new BigDecimal(costoAsigParam);
                if (costoAsig.compareTo(BigDecimal.ZERO) < 0) {
                    errors.add("Costo de asignaciones no puede ser negativo.");
                }
            } catch (Exception e) {
                errors.add("Formato de costo de asignaciones inválido.");
            }
        }

        // --- Validación y conversión de costosAdicionales ---
        if (costosAdicParam == null || costosAdicParam.isBlank()) {
            errors.add("Costos adicionales es obligatorio.");
        } else {
            try {
                costosAdic = new BigDecimal(costosAdicParam);
                if (costosAdic.compareTo(BigDecimal.ZERO) < 0) {
                    errors.add("Costos adicionales no puede ser negativo.");
                }
            } catch (Exception e) {
                errors.add("Formato de costos adicionales inválido.");
            }
        }

        // --- Validación y conversión de total ---
        if (totalParam == null || totalParam.isBlank()) {
            errors.add("Total es obligatorio.");
        } else {
            try {
                total = new BigDecimal(totalParam);
                if (total.compareTo(BigDecimal.ZERO) < 0) {
                    errors.add("Total no puede ser negativo.");
                }
            } catch (Exception e) {
                errors.add("Formato de total inválido.");
            }
        }

        // --- Validación de creadoPor ---
        if (creadoPorParam == null || creadoPorParam.isBlank()) {
            errors.add("El campo 'Creado Por' es obligatorio.");
        }

        // 3) Si hay errores, reenviar a JSP correspondiente (alta o edición)
        if (!errors.isEmpty()) {
            // Asigna lista de errores para mostrar en JSP
            req.setAttribute("errors", errors);
            try {
                // Repoblar lista de clientes para el select
                req.setAttribute("listaClientes", clienteDao.findAll());
            } catch (SQLException e) {
                throw new ServletException("Error cargando clientes", e);
            }

            // Reconstruir objeto Cotizacion para no perder los datos ingresados
            Cotizacion c = new Cotizacion();
            if (idParam != null && !idParam.isBlank()) {
                c.setIdCotizacion(Integer.parseInt(idParam));
                c.setEstado(req.getParameter("estado")); // Mantener estado en edición
            }
            c.setIdCliente(idCliente != null ? idCliente : 0);
            c.setTotalHoras(totalHoras);
            c.setFechaInicio(tsInicio);
            c.setFechaFin(tsFin);
            c.setCostoAsignaciones(costoAsig);
            c.setCostosAdicionales(costosAdic);
            c.setTotal(total);
            c.setCreadoPor(creadoPorParam);
            req.setAttribute("cotizacion", c);

            // Determina JSP de destino: formulario alta o edición
            String jsp = (idParam == null || idParam.isBlank())
                    ? "/WEB-INF/jsp/add-cotizacion.jsp"
                    : "/WEB-INF/jsp/edit-cotizacion.jsp";
            req.getRequestDispatcher(jsp).forward(req, resp);
            return;
        }

        // 4) Si pasa validación, persistir en BD
        try {
            Cotizacion c = new Cotizacion();
            c.setIdCliente(idCliente);
            c.setTotalHoras(totalHoras);
            c.setFechaInicio(tsInicio);
            c.setFechaFin(tsFin);
            c.setCostoAsignaciones(costoAsig);
            c.setCostosAdicionales(costosAdic);
            c.setTotal(total);
            c.setCreadoPor(creadoPorParam);

            boolean ok;
            // Si no hay idParam, es nueva cotización
            if (idParam == null || idParam.isBlank()) {
                c.setEstado("En proceso"); // Estado inicial por defecto
                ok = cotDao.insert(c);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cotización creada correctamente."
                                : "Error al crear la cotización.");
            }
            // Si existe idParam, es actualización de cotización existente
            else {
                c.setIdCotizacion(Integer.parseInt(idParam));
                c.setEstado(req.getParameter("estado")); // Estado puede cambiar en edición
                ok = cotDao.update(c);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cotización actualizada correctamente."
                                : "Error al actualizar la cotización.");
            }

            // Redirige al listado de cotizaciones
            resp.sendRedirect(req.getContextPath() + "/cotizaciones?action=list");
        } catch (SQLException e) {
            // En caso de error en BD durante persistencia, lanza ServletException
            throw new ServletException("Error guardando cotización", e);
        }
    }
}
