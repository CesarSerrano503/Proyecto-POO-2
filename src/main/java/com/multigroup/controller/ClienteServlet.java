/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * Servlet para gestionar operaciones CRUD sobre Clientes.
 * - doGet: muestra listado, formulario de creación, edición, inactivación, activación o eliminación definitiva.
 * - doPost: valida datos del formulario y persiste (crea o actualiza) el cliente.
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

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {
    // DAO para operaciones sobre la tabla de Clientes
    private ClienteDAO clienteDAO;

    /**
     * Inicializa el DAO obteniendo la conexión de la aplicación.
     */
    @Override
    public void init() throws ServletException {
        // Recupera la conexión a BD guardada en el contexto de la aplicación
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        // Instancia el DAO con la conexión
        clienteDAO = new ClienteDAO(conn);
    }

    /**
     * Maneja peticiones GET para listar, mostrar formularios de nuevo/edición,
     * inactivar, activar o eliminar permanentemente clientes.
     *
     * Parámetros esperados:
     * - action (opcional): "list" (por defecto), "new", "edit", "delete", "activate", "remove".
     * - idCliente (solo para "edit", "delete", "activate" y "remove").
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            // Si action es null, vacío o "list", muestra el listado de clientes
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                // Recupera todos los clientes (incluye activos e inactivos según DAO)
                List<Cliente> lista = clienteDAO.findAll();
                // Se asigna la lista al request para la JSP
                req.setAttribute("listaClientes", lista);
                // Forward a la vista de listado
                req.getRequestDispatcher("/WEB-INF/jsp/ListaClientes.jsp").forward(req, resp);

            }
            // Si action es "new", muestra formulario para agregar un nuevo cliente
            else if ("new".equalsIgnoreCase(action)) {
                req.getRequestDispatcher("/WEB-INF/jsp/add-cliente.jsp").forward(req, resp);

            }
            // Si action es "edit", muestra formulario con datos del cliente a editar
            else if ("edit".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                // Obtiene el cliente por ID
                Cliente c = clienteDAO.findById(id);
                // Asigna el cliente al request para rellenar campos del formulario
                req.setAttribute("cliente", c);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-cliente.jsp").forward(req, resp);

            }
            // Si action es "delete", inactiva el cliente (cambia estado, sin eliminar registro)
            else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                clienteDAO.delete(id);
                // Mensaje de inactivación en sesión
                req.getSession().setAttribute("mensaje", "Cliente inactivado.");
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");

            }
            // Si action es "activate", reactiva un cliente previamente inactivado
            else if ("activate".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                clienteDAO.activate(id);
                req.getSession().setAttribute("mensaje", "Cliente reactivado.");
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");

            }
            // Si action es "remove", elimina definitivamente el cliente de la BD
            else if ("remove".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idCliente"));
                boolean ok = clienteDAO.remove(id);
                // Mensaje de éxito o error al eliminar permanentemente
                req.getSession().setAttribute("mensaje",
                        ok ? "Cliente eliminado permanentemente."
                                : "Error al eliminar el cliente.");
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");

            }
            // Para cualquier otro action inválido, redirige al listado
            else {
                resp.sendRedirect(req.getContextPath() + "/clientes?action=list");
            }
        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException
            throw new ServletException("Error accediendo a la BD en doGet", e);
        }
    }

    /**
     * Maneja peticiones POST para crear o actualizar un cliente.
     *
     * Parámetros esperados:
     * - idCliente (opcional; si viene vacío => creación, si viene => edición).
     * - nombre, documento, tipoPersona, telefono, correo, direccion, creadoPor, estado (solo en edición).
     *
     * Valida cada campo, comprueba duplicados y persiste la entidad en BD.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Asegurar codificación UTF-8 para leer caracteres especiales
            req.setCharacterEncoding("UTF-8");
            // Lista para acumular mensajes de error de validación
            List<String> errors = new ArrayList<>();

            // 1) Leer parámetros del formulario
            String idParam     = req.getParameter("idCliente");
            String nombre      = req.getParameter("nombre");
            String documento   = req.getParameter("documento");
            String tipoPersona = req.getParameter("tipoPersona");
            String telefono    = req.getParameter("telefono");
            String correo      = req.getParameter("correo");
            String direccion   = req.getParameter("direccion");
            String creadoPor   = req.getParameter("creadoPor");
            String estado      = req.getParameter("estado");

            // 2) Validaciones obligatorias: campos no vacíos
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

            // 3) Validaciones de longitud y formato
            if (nombre != null && nombre.length() > 255) {
                errors.add("El nombre no puede exceder 255 caracteres.");
            }
            if (documento != null && documento.length() > 50) {
                errors.add("El documento no puede exceder 50 caracteres.");
            }
            // Expresión regular para permitir solo letras (incluye acentos y ñ) y espacios
            String soloLetras = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$";
            if (nombre != null && !nombre.matches(soloLetras)) {
                errors.add("El nombre sólo puede contener letras y espacios.");
            }
            if (creadoPor != null && !creadoPor.matches(soloLetras)) {
                errors.add("El campo 'Creado Por' sólo puede contener letras y espacios.");
            }
            // Expresión regular básica para validar formato de correo
            String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
            if (correo != null && !correo.matches(emailRegex)) {
                errors.add("El correo electrónico no tiene un formato válido.");
            }

            // 4) Comprobar duplicado de documento al crear un nuevo cliente
            if ((idParam == null || idParam.isEmpty())
                    && clienteDAO.existsByDocumento(documento)) {
                errors.add("Ya existe un cliente con ese documento.");
            }

            // 5) Si hay errores, reenviar al formulario correspondiente
            if (!errors.isEmpty()) {
                // Asigna lista de errores para mostrar en JSP
                req.setAttribute("errors", errors);

                // Reconstruir objeto Cliente para rellenar campos del formulario
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
                // Determina JSP de destino: creación o edición
                String jsp = (idParam == null || idParam.isEmpty())
                        ? "/WEB-INF/jsp/add-cliente.jsp"
                        : "/WEB-INF/jsp/edit-cliente.jsp";
                req.getRequestDispatcher(jsp).forward(req, resp);
                return;
            }

            // 6) Si pasa validación, persistir en BD
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
                // Campo estado por defecto al crear nuevo cliente
                c.setEstado("Activo");
                ok = clienteDAO.insert(c);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cliente agregado correctamente."
                                : "Error al agregar el cliente.");
            } else {
                // Edición de cliente existente: se conserva o modifica estado
                c.setIdCliente(Integer.parseInt(idParam));
                c.setEstado(estado);
                ok = clienteDAO.update(c);
                req.getSession().setAttribute("mensaje",
                        ok ? "Cliente actualizado correctamente."
                                : "Error al actualizar el cliente.");
            }

            // Redirige al listado de clientes
            resp.sendRedirect(req.getContextPath() + "/clientes?action=list");
        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException
            throw new ServletException("Error accediendo a la BD en doPost", e);
        }
    }
}
