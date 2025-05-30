/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 */
package com.multigroup.controller;

import com.multigroup.dao.UsuarioDAO;
import com.multigroup.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="UsuarioServlet", urlPatterns={"/usuarios"})
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO dao;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/multigroup", "root", "");
            dao = new UsuarioDAO(conn);
        } catch (Exception e) {
            throw new ServletException("Error iniciando UsuarioDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "nuevo":
                    req.getRequestDispatcher("/WEB-INF/jsp/usuario-form.jsp")
                            .forward(req, resp);
                    break;
                case "insert":
                    insertar(req, resp);
                    break;
                case "edit":
                    editar(req, resp);
                    break;
                case "update":
                    actualizar(req, resp);
                    break;
                case "delete":
                    eliminar(req, resp);
                    break;
                default:
                    listar(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // Redirige POST a GET para soportar formularios con method="post"
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        List<Usuario> lista = dao.listar();
        req.setAttribute("usuarios", lista);
        req.getRequestDispatcher("/WEB-INF/jsp/usuario-listar.jsp")
                .forward(req, resp);
    }

    private void insertar(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        Usuario u = new Usuario();
        u.setUsername(req.getParameter("username"));
        u.setPassword(req.getParameter("password"));
        u.setRol(req.getParameter("rol"));
        u.setEstado(req.getParameter("estado"));
        u.setCreadoPor(((Usuario)req.getSession().getAttribute("usuario")).getUsername());
        dao.insertar(u);
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int id = Integer.parseInt(req.getParameter("id"));
        Usuario u = dao.buscarPorId(id);
        req.setAttribute("usuario", u);
        req.getRequestDispatcher("/WEB-INF/jsp/usuario-form.jsp")
                .forward(req, resp);
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        Usuario u = new Usuario();
        u.setIdUsuario(Integer.parseInt(req.getParameter("id")));
        u.setUsername(req.getParameter("username"));
        u.setPassword(req.getParameter("password"));
        u.setRol(req.getParameter("rol"));
        u.setEstado(req.getParameter("estado"));
        dao.actualizar(u);
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int id = Integer.parseInt(req.getParameter("id"));
        dao.eliminar(id);
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }
}
