package com.multigroup.controller;

import com.multigroup.dao.EmpleadoDAO;
import com.multigroup.model.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                List<Empleado> lista = empleadoDAO.findAll();
                req.setAttribute("listaEmpleados", lista);
                req.getRequestDispatcher("/WEB-INF/jsp/listaEmpleados.jsp").forward(req, resp);

            } else if ("new".equalsIgnoreCase(action)) {
                req.getRequestDispatcher("/WEB-INF/jsp/add-empleado.jsp").forward(req, resp);

            } else if ("edit".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                Empleado emp = empleadoDAO.findById(id);
                req.setAttribute("empleado", emp);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-empleado.jsp").forward(req, resp);

            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                empleadoDAO.delete(id);
                req.getSession().setAttribute("mensaje", "Empleado inactivado.");
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");

            } else if ("activate".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                empleadoDAO.activate(id);
                req.getSession().setAttribute("mensaje", "Empleado reactivado.");
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");

            } else if ("remove".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                boolean ok = empleadoDAO.remove(id);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado eliminado permanentemente."
                                : "Error al eliminar el empleado.");
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");

            } else {
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
            }
        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doGet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            List<String> errors = new ArrayList<>();

            // Leer parámetros
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

            // Validaciones obligatorias
            if (nombre == null || nombre.isBlank()) {
                errors.add("El nombre es obligatorio.");
            }
            if (documento == null || documento.isBlank()) {
                errors.add("El documento es obligatorio.");
            }
            if (tipoPersona == null || tipoPersona.isBlank()) {
                errors.add("Debe seleccionar Tipo de Persona.");
            }
            if (tipoContrat == null || tipoContrat.isBlank()) {
                errors.add("Debe seleccionar Tipo de Contratación.");
            }
            if (telefono == null || telefono.isBlank()) {
                errors.add("El teléfono es obligatorio.");
            }
            if (correo == null || correo.isBlank()) {
                errors.add("El correo es obligatorio.");
            }
            if (direccion == null || direccion.isBlank()) {
                errors.add("La dirección es obligatoria.");
            }
            if (creadoPor == null || creadoPor.isBlank()) {
                errors.add("El campo 'Creado Por' es obligatorio.");
            }

            // Longitudes y formatos
            if (nombre != null && nombre.length()>255) {
                errors.add("El nombre no puede exceder 255 caracteres.");
            }
            if (documento != null && documento.length()>50) {
                errors.add("El documento no puede exceder 50 caracteres.");
            }
            String soloLetras = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$";
            if (nombre != null && !nombre.matches(soloLetras)) {
                errors.add("El nombre sólo puede contener letras y espacios.");
            }
            if (creadoPor != null && !creadoPor.matches(soloLetras)) {
                errors.add("El campo 'Creado Por' sólo puede contener letras y espacios.");
            }
            String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
            if (correo != null && !correo.matches(emailRegex)) {
                errors.add("El correo electrónico no tiene un formato válido.");
            }

            // Duplicado de documento en alta
            if ((idParam==null || idParam.isEmpty())
                    && empleadoDAO.existsByDocumento(documento)) {
                errors.add("Ya existe un empleado con ese documento.");
            }

            // Si hay errores, reenviar al formulario
            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                Empleado emp = new Empleado();
                if (idParam != null && !idParam.isEmpty()) {
                    emp.setIdEmpleado(Integer.parseInt(idParam));
                    emp.setEstado(estado);
                }
                emp.setNombre(nombre);
                emp.setDocumento(documento);
                emp.setTipoPersona(tipoPersona);
                emp.setTipoContratacion(tipoContrat);
                emp.setTelefono(telefono);
                emp.setCorreo(correo);
                emp.setDireccion(direccion);
                emp.setCreadoPor(creadoPor);
                req.setAttribute("empleado", emp);

                String jsp = (idParam==null || idParam.isEmpty())
                        ? "/WEB-INF/jsp/add-empleado.jsp"
                        : "/WEB-INF/jsp/edit-empleado.jsp";
                req.getRequestDispatcher(jsp).forward(req, resp);
                return;
            }

            // Persistir
            boolean ok;
            Empleado emp = new Empleado();
            emp.setNombre(nombre);
            emp.setDocumento(documento);
            emp.setTipoPersona(tipoPersona);
            emp.setTipoContratacion(tipoContrat);
            emp.setTelefono(telefono);
            emp.setCorreo(correo);
            emp.setDireccion(direccion);
            emp.setCreadoPor(creadoPor);

            if (idParam == null || idParam.isEmpty()) {
                emp.setEstado("Activo");
                ok = empleadoDAO.insert(emp);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado agregado correctamente."
                                : "Error al agregar el empleado.");
            } else {
                emp.setIdEmpleado(Integer.parseInt(idParam));
                emp.setEstado(estado);
                ok = empleadoDAO.update(emp);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado actualizado correctamente."
                                : "Error al actualizar el empleado.");
            }

            resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doPost", e);
        }
    }
}
