package com.multigroup.controller;

import com.multigroup.dao.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="EliminarCliente", urlPatterns="/clientes/eliminar")
public class EliminarClienteServlet extends HttpServlet {
    private final ClienteDAO dao = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                dao.delete(id);
            } catch (Exception e) {
                throw new ServletException("Error al eliminar cliente con ID=" + idParam, e);
            }
        }
        // Redirige siempre al listado, incluso si id era null o hubo excepción
        resp.sendRedirect(req.getContextPath() + "/clientes");
    }
}
