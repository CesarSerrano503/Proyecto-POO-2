/*
Created by IntelliJ IDEA.
User: Cesar
Date: 27/5/2025
Time: 22:12
To change this template use File | Settings | File Templates.
*/
package com.multigroup.controller;

import com.multigroup.dao.ClienteDAO;
import com.multigroup.model.Cliente;

// AHORA (jakarta.servlet)
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

@WebServlet(name = "InsertarCliente", urlPatterns = "/clientes/nuevo")
public class InsertarClienteServlet extends HttpServlet {

    private final ClienteDAO dao = new ClienteDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Leer parámetros del formulario
        String nombre      = req.getParameter("nombre");
        String documento   = req.getParameter("documento");
        String tipoPersona = req.getParameter("tipoPersona");
        String telefono    = req.getParameter("telefono");
        String correo      = req.getParameter("correo");
        String direccion   = req.getParameter("direccion");

        try {
            // Crear objeto Cliente
            Cliente c = new Cliente();
            c.setNombre(nombre);
            c.setDocumento(documento);
            c.setTipoPersona(tipoPersona);
            c.setTelefono(telefono);
            c.setCorreo(correo);
            c.setDireccion(direccion);

            // Insertar en BD
            dao.insert(c);

            // Redirigir al listado
            resp.sendRedirect(req.getContextPath() + "/clientes");
        } catch (Exception e) {
            throw new ServletException("Error al insertar cliente", e);
        }
    }

    // Opcional: permitir GET para mostrar el formulario
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/add-cliente.jsp")
                .forward(req, resp);
    }
}
