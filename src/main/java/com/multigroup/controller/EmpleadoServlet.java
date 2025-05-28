// src/main/java/com/multigroup/controller/EmpleadoServlet.java
package com.multigroup.controller;

import com.multigroup.dao.EmpleadoDAO;
import com.multigroup.model.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Servlet para CRUD de Empleados, con acción por defecto "list".
 * Archivo modificado: EmpleadoServlet.java
 */
@WebServlet("/empleados")
public class EmpleadoServlet extends HttpServlet {
    private EmpleadoDAO empleadoDAO;

    @Override
    public void init() throws ServletException {
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        empleadoDAO = new EmpleadoDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            // Acción por defecto: listar
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                List<Empleado> lista = empleadoDAO.findAll();
                req.setAttribute("listaEmpleados", lista);
                req.getRequestDispatcher("/WEB-INF/jsp/listaEmpleados.jsp")
                        .forward(req, resp);
                return;
            }

            switch (action) {
                case "new":
                    req.getRequestDispatcher("/WEB-INF/jsp/add-empleado.jsp")
                            .forward(req, resp);
                    break;
                case "edit":
                    int idEdit = Integer.parseInt(req.getParameter("idEmpleado"));
                    Empleado empEdit = empleadoDAO.findById(idEdit);
                    req.setAttribute("empleado", empEdit);
                    req.getRequestDispatcher("/WEB-INF/jsp/edit-empleado.jsp")
                            .forward(req, resp);
                    break;
                case "delete":
                    int idDel = Integer.parseInt(req.getParameter("idEmpleado"));
                    empleadoDAO.delete(idDel);
                    resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
                    break;
                case "activate":
                    int idAct = Integer.parseInt(req.getParameter("idEmpleado"));
                    empleadoDAO.activate(idAct);
                    resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String idParam       = req.getParameter("idEmpleado");
            String nombre        = req.getParameter("nombre");
            String documento     = req.getParameter("documento");
            String tipoPersona   = req.getParameter("tipoPersona");
            String tipoContrat   = req.getParameter("tipoContratacion");
            String telefono      = req.getParameter("telefono");
            String correo        = req.getParameter("correo");
            String direccion     = req.getParameter("direccion");
            String creadoPor     = req.getParameter("creadoPor");
            String estado        = req.getParameter("estado");

            Empleado emp = new Empleado();
            emp.setNombre(nombre);
            emp.setDocumento(documento);
            emp.setTipoPersona(tipoPersona);
            emp.setTipoContratacion(tipoContrat);
            emp.setTelefono(telefono);
            emp.setCorreo(correo);
            emp.setDireccion(direccion);
            emp.setCreadoPor(creadoPor);

            boolean ok;
            if (idParam == null || idParam.isEmpty()) {
                emp.setEstado("Activo");
                ok = empleadoDAO.insert(emp);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado agregado correctamente." : "Error al agregar el empleado.");
            } else {
                emp.setIdEmpleado(Integer.parseInt(idParam));
                emp.setEstado(estado);
                ok = empleadoDAO.update(emp);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado actualizado correctamente." : "Error al actualizar el empleado.");
            }

            resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
        } catch (Exception e) {
            req.getSession().setAttribute("mensaje",
                    "Error interno: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
        }
    }
}
