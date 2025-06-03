/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * LogoutServlet: invalida la sesión del usuario y redirige al formulario de login con parámetro de cierre.
 * - doGet: destruye la sesión actual y redirige a /login.jsp?logout=true para mostrar mensaje de cierre de sesión.
 */
package com.multigroup.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Método GET: invalida la sesión del usuario y redirige a la página de login
     * con un parámetro "logout=true" para que JSP muestre mensaje de sesión cerrada.
     *
     * @param request  HttpServletRequest con datos de la petición.
     * @param response HttpServletResponse para enviar la redirección.
     * @throws ServletException si ocurre error en el servlet.
     * @throws IOException      si ocurre error de E/S al redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalida la sesión actual, eliminando todos los atributos almacenados
        request.getSession().invalidate();
        // Redirige al formulario de login con query string logout=true
        response.sendRedirect(request.getContextPath() + "/login.jsp?logout=true");
    }
}
