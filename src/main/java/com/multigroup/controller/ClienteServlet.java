/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 */
package com.multigroup.controller;

import com.multigroup.dao.ClienteDAO;
import com.multigroup.model.Cliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        clienteDAO = new ClienteDAO(conn);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                List<Cliente> lista = clienteDAO.findAll();
                req.setAttribute("listaClientes", lista);
                req.getRequestDispatcher("/WEB-INF/jsp/ListaClientes.jsp").forward(req, resp);

            } else if ("new".equalsIgnoreCase(action)) {
                req.getRequestDispatcher("/WEB-INF/jsp/add-cliente.jsp").forward(req, resp);

            } else if ("edit".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                Cliente c = clienteDAO.findById(id);
                req.setAttribute("cliente", c);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-cliente.jsp").forward(req, resp);

            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                clienteDAO.delete(id);
                req.getSession().setAttribute("mensaje", "Cliente inactivado.");
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");

            } else if ("activate".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                clienteDAO.activate(id);
                req.getSession().setAttribute("mensaje", "Cliente reactivado.");
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");

            } else if ("remove".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                boolean ok = clienteDAO.remove(id);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cliente eliminado permanentemente."
                                : "Error al eliminar el cliente.");
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");

            } else {
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");
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

            // 1) Leer parámetros
            String idParam     = req.getParameter("idCliente");
            String nombre      = req.getParameter("nombre");
            String documento   = req.getParameter("documento");
            String tipoPersona = req.getParameter("tipoPersona");
            String telefono    = req.getParameter("telefono");
            String correo      = req.getParameter("correo");
            String direccion   = req.getParameter("direccion");
            String creadoPor   = req.getParameter("creadoPor");
            String estado      = req.getParameter("estado");

            // 2) Validaciones obligatorias
            if (nombre == null || nombre.isBlank()) {
                errors.add("El nombre es obligatorio.");
            }
            if (documento == null || documento.isBlank()) {
                errors.add("El documento es obligatorio.");
            }
            if (tipoPersona == null || tipoPersona.isBlank()) {
                errors.add("Debe seleccionar Tipo de Persona.");
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

            // 3) Longitudes y formatos
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

            // 4) Duplicado de documento en alta
            if ((idParam==null || idParam.isEmpty())
                    && clienteDAO.existsByDocumento(documento)) {
                errors.add("Ya existe un cliente con ese documento.");
            }

            // 5) Si hay errores, reenviar al formulario
            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);

                Cliente c = new Cliente();
                if (idParam != null && !idParam.isEmpty()) {
                    c.setIdCliente(Integer.parseInt(idParam));
                    c.setEstado(estado);
                }
                c.setNombre(nombre);
                c.setDocumento(documento);
                c.setTipoPersona(tipoPersona);
                c.setTelefono(telefono);
                c.setCorreo(correo);
                c.setDireccion(direccion);
                c.setCreadoPor(creadoPor);

                req.setAttribute("cliente", c);
                String jsp = (idParam==null || idParam.isEmpty())
                        ? "/WEB-INF/jsp/add-cliente.jsp"
                        : "/WEB-INF/jsp/edit-cliente.jsp";
                req.getRequestDispatcher(jsp).forward(req, resp);
                return;
            }

            // 6) Persistir si pasa validación
            boolean ok;
            Cliente c = new Cliente();
            c.setNombre(nombre);
            c.setDocumento(documento);
            c.setTipoPersona(tipoPersona);
            c.setTelefono(telefono);
            c.setCorreo(correo);
            c.setDireccion(direccion);
            c.setCreadoPor(creadoPor);

            if (idParam == null || idParam.isEmpty()) {
                c.setEstado("Activo");
                ok = clienteDAO.insert(c);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cliente agregado correctamente."
                                : "Error al agregar el cliente.");
            } else {
                c.setIdCliente(Integer.parseInt(idParam));
                c.setEstado(estado);
                ok = clienteDAO.update(c);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cliente actualizado correctamente."
                                : "Error al actualizar el cliente.");
            }

            resp.sendRedirect(req.getContextPath() + "/clientes?action=list");
        } catch (SQLException e) {
            throw new ServletException("Error accediendo a la BD en doPost", e);
        }
    }
}
