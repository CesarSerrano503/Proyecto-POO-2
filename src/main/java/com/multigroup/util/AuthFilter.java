package com.multigroup.util;

import com.multigroup.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String loginURI  = request.getContextPath() + "/login";
        String loginPage = request.getContextPath() + "/login.jsp";

        boolean loggedIn     = session != null && session.getAttribute("usuario") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI)
                || request.getRequestURI().equals(loginPage);
        boolean resourceReq  = request.getRequestURI().startsWith(request.getContextPath() + "/css/")
                || request.getRequestURI().endsWith(".css");

        if (loggedIn || loginRequest || resourceReq) {
            // Si es módulo de usuarios, solo admins
            if (request.getRequestURI().contains("/usuarios")) {
                Usuario u = (Usuario) session.getAttribute("usuario");
                if (u == null || !"admin".equalsIgnoreCase(u.getRol())) {
                    // No es admin → redirigir a inicio colaborador
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    return;
                }
            }
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(loginPage);
        }
    }
}
