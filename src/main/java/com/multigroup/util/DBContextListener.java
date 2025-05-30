/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 */
package com.multigroup.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * Listener que abre la conexión JDBC al iniciar la webapp
 * usando usuario root y sin contraseña, y la cierra al parar.
 */
@WebListener
public class DBContextListener implements ServletContextListener {
    private Connection conn;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String url  = "jdbc:mysql://localhost:3306/multigroup?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String pass = "";  // sin contraseña

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);

            sce.getServletContext().setAttribute("DBConnection", conn);
            System.out.println(">>> Conexión a BD ‘multigroup’ establecida con root sin contraseña");
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando conexión a BD", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1) Cerramos la conexión
        if (conn != null) {
            try {
                conn.close();
                System.out.println(">>> Conexión a BD cerrada");
            } catch (SQLException ignore) { }
        }

        // 2) Limpiamos el hilo de MySQL
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            // ignorar cualquier error durante la limpieza
        }

        // 3) Deregistramos los drivers para evitar fugas de memoria en Tomcat
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ignore) { }
        }
    }
}
