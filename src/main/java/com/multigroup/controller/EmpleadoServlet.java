/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * Servlet para gestionar operaciones CRUD sobre Empleados.
 * - doGet: muestra listado, formulario de creación, edición, inactivación, activación o eliminación definitiva.
 * - doPost: valida datos del formulario (incluyendo validación de duplicado de documento durante edición)
 *           y persiste (crea o actualiza) el empleado.
 */
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

@WebServlet("/empleados")
public class EmpleadoServlet extends HttpServlet {
    // DAO para operaciones sobre la tabla de Empleados
    private EmpleadoDAO empleadoDAO;

    /**
     * Inicializa el DAO obteniendo la conexión de la aplicación.
     */
    @Override
    public void init() throws ServletException {
        // Recupera la conexión a BD almacenada en el contexto de la aplicación
        Connection conn = (Connection) getServletContext().getAttribute("DBConnection");
        // Instancia el DAO con la conexión
        empleadoDAO = new EmpleadoDAO(conn);
    }

    /**
     * Maneja peticiones GET para listar, mostrar formularios de nuevo/edición,
     * inactivar, activar o eliminar permanentemente empleados.
     *
     * Parámetros esperados:
     * - action (opcional): "list" (por defecto), "new", "edit", "delete", "activate", "remove".
     * - idEmpleado (solo para "edit", "delete", "activate" y "remove").
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            // Si action es null, vacío o "list", muestra el listado de empleados
            if (action == null || action.isEmpty() || "list".equalsIgnoreCase(action)) {
                // Recupera todos los empleados (incluye estado según DAO)
                List<Empleado> lista = empleadoDAO.findAll();
                // Asigna la lista al request para la JSP
                req.setAttribute("listaEmpleados", lista);
                // Forward a la vista de listado
                req.getRequestDispatcher("/WEB-INF/jsp/listaEmpleados.jsp").forward(req, resp);

            }
            // Si action es "new", muestra formulario para agregar un nuevo empleado
            else if ("new".equalsIgnoreCase(action)) {
                req.getRequestDispatcher("/WEB-INF/jsp/add-empleado.jsp").forward(req, resp);

            }
            // Si action es "edit", muestra formulario con datos del empleado a editar
            else if ("edit".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                // Obtiene el empleado por ID
                Empleado emp = empleadoDAO.findById(id);
                // Asigna el empleado al request para rellenar campos del formulario
                req.setAttribute("empleado", emp);
                req.getRequestDispatcher("/WEB-INF/jsp/edit-empleado.jsp").forward(req, resp);

            }
            // Si action es "delete", inactiva el empleado (baja lógica)
            else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                empleadoDAO.delete(id);
                // Mensaje de inactivación en sesión
                req.getSession().setAttribute("mensaje", "Empleado inactivado.");
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");

            }
            // Si action es "activate", reactiva un empleado previamente inactivado
            else if ("activate".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                empleadoDAO.activate(id);
                // Mensaje de reactivación en sesión
                req.getSession().setAttribute("mensaje", "Empleado reactivado.");
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");

            }
            // Si action es "remove", elimina definitivamente el empleado de la BD
            else if ("remove".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("idEmpleado"));
                boolean ok = empleadoDAO.remove(id);
                // Mensaje de éxito o error al eliminar permanentemente
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado eliminado permanentemente."
                                : "Error al eliminar el empleado.");
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");

            }
            // Para cualquier otro action inválido, redirige al listado
            else {
                resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
            }
        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException
            throw new ServletException("Error accediendo a la BD en doGet", e);
        }
    }

    /**
     * Maneja peticiones POST para crear o actualizar un empleado.
     * Valida cada campo, comprueba duplicados (teniendo en cuenta edición vs. creación)
     * y persiste la entidad en BD.
     *
     * Parámetros esperados:
     * - idEmpleado (opcional; si viene vacío ⇒ creación, si viene ⇒ edición).
     * - nombre, documento, tipoPersona, tipoContratacion, telefono, correo, direccion, creadoPor, estado (solo en edición).
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

            // 4) Comprobar duplicado de documento:
            //    - Si idParam está vacío ⇒ creación: no debe existir ningún empleado con el mismo documento.
            //    - Si idParam NO está vacío ⇒ edición: si existe un empleado con ese documento,
            //      solo es válido si el ID coincide con el que estamos editando.
            if (documento != null && !documento.isBlank()) {
                Empleado existente = empleadoDAO.findByDocumento(documento);
                if (idParam == null || idParam.isEmpty()) {
                    // CREACIÓN: si existe cualquier empleado con ese documento → error
                    if (existente != null) {
                        errors.add("Ya existe un empleado con ese documento.");
                    }
                } else {
                    // EDICIÓN: si existe un empleado con ese documento y es distinto del que editamos → error
                    int idEdit = Integer.parseInt(idParam);
                    if (existente != null && existente.getIdEmpleado() != idEdit) {
                        errors.add("Ya existe otro empleado con ese documento.");
                    }
                }
            }

            // 5) Si hay errores, reenviar al formulario correspondiente (add o edit)
            if (!errors.isEmpty()) {
                // Asigna lista de errores para mostrar en JSP
                req.setAttribute("errors", errors);

                // Reconstruir objeto Empleado para rellenar campos del formulario
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

                // Determina JSP de destino: creación o edición
                String jsp = (idParam == null || idParam.isEmpty())
                        ? "/WEB-INF/jsp/add-empleado.jsp"
                        : "/WEB-INF/jsp/edit-empleado.jsp";
                req.getRequestDispatcher(jsp).forward(req, resp);
                return;
            }

            // 6) Si pasa validación, persistir en BD
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
                // CREACIÓN de nuevo empleado
                emp.setEstado("Activo");
                ok = empleadoDAO.insert(emp);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado agregado correctamente."
                                : "Error al agregar el empleado.");
            } else {
                // EDICIÓN de empleado existente
                emp.setIdEmpleado(Integer.parseInt(idParam));
                emp.setEstado(estado);
                ok = empleadoDAO.update(emp);
                req.getSession().setAttribute("mensaje",
                        ok ? "Empleado actualizado correctamente."
                                : "Error al actualizar el empleado.");
            }

            // Redirige al listado de empleados
            resp.sendRedirect(req.getContextPath() + "/empleados?action=list");
        } catch (SQLException e) {
            // En caso de error en BD, lanza ServletException
            throw new ServletException("Error accediendo a la BD en doPost", e);
        }
    }
}
