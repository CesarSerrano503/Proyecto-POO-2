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
import java.util.List;

@WebServlet("/clientes")
public class ListarClientesServlet extends HttpServlet {
    private final ClienteDAO dao = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Cliente> clientes = dao.findAll();
            req.setAttribute("clientes", clientes);
            req.getRequestDispatcher("/WEB-INF/jsp/list-clientes.jsp")
                    .forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error al listar clientes", e);
        }
    }
}
