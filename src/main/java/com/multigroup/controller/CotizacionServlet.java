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
    private CotizacionDAO cotDao;
    private ClienteDAO   clienteDao;

    @Override
    public void init() throws ServletException {
        Connection conn = (Connection) getServletContext()
                .getAttribute("DBConnection");
        cotDao     = new CotizacionDAO(conn);
        clienteDao = new ClienteDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if (action == null
                    || action.isEmpty()
                    || "list".equalsIgnoreCase(action)) {
                // 1) LISTADO
                List<Cotizacion> lista = cotDao.findAll();
                req.setAttribute("listaCot", lista);
                req.getRequestDispatcher(
                        "/WEB-INF/jsp/listaCotizaciones.jsp"
                ).forward(req, resp);

            } else if ("new".equalsIgnoreCase(action)) {
                // 2) FORMULARIO ALTA
                req.setAttribute("listaClientes",
                        clienteDao.findAll());
                // Objeto vacío para no tener NPE en JSP
                req.setAttribute("cotizacion", new Cotizacion());
                req.getRequestDispatcher(
                        "/WEB-INF/jsp/add-cotizacion.jsp"
                ).forward(req, resp);

            } else if ("edit".equalsIgnoreCase(action)) {
                // 3) FORMULARIO EDICIÓN
                int id = Integer.parseInt(
                        req.getParameter("idCotizacion"));
                Cotizacion c = cotDao.findById(id);
                req.setAttribute("cotizacion", c);
                req.setAttribute("listaClientes",
                        clienteDao.findAll());
                req.getRequestDispatcher(
                        "/WEB-INF/jsp/edit-cotizacion.jsp"
                ).forward(req, resp);

            } else if ("delete".equalsIgnoreCase(action)) {
                // 4) BAJA LÓGICA
                int id = Integer.parseInt(
                        req.getParameter("idCotizacion"));
                cotDao.delete(id);
                req.getSession().setAttribute(
                        "mensaje","Cotización inactivada."
                );
                resp.sendRedirect(
                        req.getContextPath()
                                + "/cotizaciones?action=list"
                );

            } else if ("finalize".equalsIgnoreCase(action)) {
                // 5) MARCAR FINALIZADA
                int id = Integer.parseInt(
                        req.getParameter("idCotizacion"));
                cotDao.finalize(id);
                req.getSession().setAttribute(
                        "mensaje","Cotización finalizada."
                );
                resp.sendRedirect(
                        req.getContextPath()
                                + "/cotizaciones?action=list"
                );

            } else if ("remove".equalsIgnoreCase(action)) {
                // 6) ELIMINACIÓN FÍSICA
                int id = Integer.parseInt(
                        req.getParameter("idCotizacion"));
                cotDao.remove(id);
                req.getSession().setAttribute(
                        "mensaje","Cotización eliminada permanentemente."
                );
                resp.sendRedirect(
                        req.getContextPath()
                                + "/cotizaciones?action=list"
                );

            } else {
                // cualquier otro caso, redirigir a lista
                resp.sendRedirect(
                        req.getContextPath()
                                + "/cotizaciones?action=list"
                );
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Error en operación GET de Cotizaciones", e
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();

        // 1) Leer parámetros crudos
        String idParam           = req.getParameter("idCotizacion");
        String cliParam          = req.getParameter("idCliente");
        String totalHorasParam   = req.getParameter("totalHoras");
        String fechaIniParam     = req.getParameter("fechaInicio");
        String fechaFinParam     = req.getParameter("fechaFin");
        String costoAsigParam    = req.getParameter("costoAsignaciones");
        String costosAdicParam   = req.getParameter("costosAdicionales");
        String totalParam        = req.getParameter("total");
        String creadoPorParam    = req.getParameter("creadoPor");

        // 2) Validar y convertir
        Integer       idCliente      = null;
        BigDecimal    totalHoras     = null;
        Timestamp     tsInicio       = null;
        Timestamp     tsFin          = null;
        BigDecimal    costoAsig      = null;
        BigDecimal    costosAdic     = null;
        BigDecimal    total          = null;

        // idCliente
        if (cliParam==null||cliParam.isBlank()) {
            errors.add("Debe seleccionar un cliente.");
        } else {
            try { idCliente = Integer.parseInt(cliParam); }
            catch(NumberFormatException e){
                errors.add("Cliente inválido.");
            }
        }

        // totalHoras
        if(totalHorasParam==null||totalHorasParam.isBlank()){
            errors.add("Total de horas es obligatorio.");
        } else {
            try {
                totalHoras = new BigDecimal(totalHorasParam);
                if(totalHoras.compareTo(BigDecimal.ZERO)<=0)
                    errors.add("Total de horas debe ser mayor que cero.");
            } catch(Exception e){
                errors.add("Formato de total de horas inválido.");
            }
        }

        // fechaInicio → Timestamp
        if(fechaIniParam==null||fechaIniParam.isBlank()){
            errors.add("Fecha de inicio es obligatoria.");
        } else {
            try {
                LocalDateTime lt = LocalDateTime.parse(fechaIniParam);
                tsInicio = Timestamp.valueOf(lt);
            } catch(DateTimeParseException e){
                errors.add("Formato de fecha de inicio inválido.");
            }
        }

        // fechaFin → Timestamp
        if(fechaFinParam==null||fechaFinParam.isBlank()){
            errors.add("Fecha de fin es obligatoria.");
        } else {
            try {
                LocalDateTime lt = LocalDateTime.parse(fechaFinParam);
                tsFin = Timestamp.valueOf(lt);
            } catch(DateTimeParseException e){
                errors.add("Formato de fecha de fin inválido.");
            }
        }

        // coherencia fechas
        if(tsInicio!=null && tsFin!=null && tsFin.before(tsInicio)){
            errors.add("La fecha de fin debe ser posterior a la de inicio.");
        }

        // costoAsignaciones
        if(costoAsigParam==null||costoAsigParam.isBlank()){
            errors.add("Costo de asignaciones es obligatorio.");
        } else {
            try {
                costoAsig = new BigDecimal(costoAsigParam);
                if(costoAsig.compareTo(BigDecimal.ZERO)<0)
                    errors.add("Costo de asignaciones no puede ser negativo.");
            } catch(Exception e){
                errors.add("Formato de costo de asignaciones inválido.");
            }
        }

        // costosAdicionales
        if(costosAdicParam==null||costosAdicParam.isBlank()){
            errors.add("Costos adicionales es obligatorio.");
        } else {
            try {
                costosAdic = new BigDecimal(costosAdicParam);
                if(costosAdic.compareTo(BigDecimal.ZERO)<0)
                    errors.add("Costos adicionales no puede ser negativo.");
            } catch(Exception e){
                errors.add("Formato de costos adicionales inválido.");
            }
        }

        // total
        if(totalParam==null||totalParam.isBlank()){
            errors.add("Total es obligatorio.");
        } else {
            try {
                total = new BigDecimal(totalParam);
                if(total.compareTo(BigDecimal.ZERO)<0)
                    errors.add("Total no puede ser negativo.");
            } catch(Exception e){
                errors.add("Formato de total inválido.");
            }
        }

        // creadoPor
        if(creadoPorParam==null||creadoPorParam.isBlank()){
            errors.add("El campo 'Creado Por' es obligatorio.");
        }

        // 3) Si hay errores, reenviar a JSP
        if(!errors.isEmpty()){
            req.setAttribute("errors", errors);
            try {
                req.setAttribute("listaClientes", clienteDao.findAll());
            } catch(SQLException e){
                throw new ServletException("Error cargando clientes", e);
            }
            // rellenar objeto para no perder datos
            Cotizacion c = new Cotizacion();
            if(idParam!=null && !idParam.isBlank()){
                c.setIdCotizacion(Integer.parseInt(idParam));
                c.setEstado(req.getParameter("estado"));
            }
            c.setIdCliente(idCliente!=null? idCliente:0);
            c.setTotalHoras(totalHoras);
            c.setFechaInicio(tsInicio);
            c.setFechaFin(tsFin);
            c.setCostoAsignaciones(costoAsig);
            c.setCostosAdicionales(costosAdic);
            c.setTotal(total);
            c.setCreadoPor(creadoPorParam);
            req.setAttribute("cotizacion", c);

            String jsp = (idParam==null||idParam.isBlank())
                    ? "/WEB-INF/jsp/add-cotizacion.jsp"
                    : "/WEB-INF/jsp/edit-cotizacion.jsp";
            req.getRequestDispatcher(jsp).forward(req, resp);
            return;
        }

        // 4) Persistir sin errores
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
            if(idParam==null||idParam.isBlank()){
                c.setEstado("En proceso");
                ok = cotDao.insert(c);
                req.getSession().setAttribute(
                        "mensaje",
                        ok
                                ? "Cotización creada correctamente."
                                : "Error al crear la cotización."
                );
            } else {
                c.setIdCotizacion(Integer.parseInt(idParam));
                c.setEstado(req.getParameter("estado"));
                ok = cotDao.update(c);
                req.getSession().setAttribute(
                        "mensaje",
                        ok
                                ? "Cotización actualizada correctamente."
                                : "Error al actualizar la cotización."
                );
            }

            resp.sendRedirect(
                    req.getContextPath()
                            + "/cotizaciones?action=list"
            );
        } catch(SQLException e){
            throw new ServletException(
                    "Error guardando cotización", e
            );
        }
    }
}
