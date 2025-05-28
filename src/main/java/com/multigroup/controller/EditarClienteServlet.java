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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

@WebServlet(name="EditarCliente", urlPatterns="/clientes/editar")
public class EditarClienteServlet extends HttpServlet {
    private final ClienteDAO dao = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            Cliente c = dao.findById(id);
            req.setAttribute("cliente", c);
            req.getRequestDispatcher("/WEB-INF/jsp/edit-cliente.jsp")
                    .forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error al cargar cliente para edición", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Cliente c = new Cliente();
            c.setId(Integer.parseInt(req.getParameter("id")));
            c.setNombre(req.getParameter("nombre"));
            c.setDocumento(req.getParameter("documento"));
            c.setTipoPersona(req.getParameter("tipoPersona"));
            c.setTelefono(req.getParameter("telefono"));
            c.setCorreo(req.getParameter("correo"));
            c.setDireccion(req.getParameter("direccion"));
            c.setEstado(req.getParameter("estado"));

            dao.update(c);
            resp.sendRedirect(req.getContextPath() + "/clientes");
        } catch (Exception e) {
            throw new ServletException("Error al actualizar cliente", e);
        }
    }
}
