/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * Listener que abre la conexión JDBC al iniciar la webapp usando usuario root y sin contraseña,
 * y la cierra al detener la aplicación para evitar fugas de recursos.
 */
package com.multigroup.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import java.util.Enumeration;

@WebListener
public class DBContextListener implements ServletContextListener {
    /** Conexión compartida para toda la aplicación web. */
    private Connection conn;

    /**
     * Método llamado cuando se inicia el contexto de la aplicación.
     * Se encarga de:
     * 1) Cargar el driver JDBC de MySQL.
     * 2) Abrir la conexión a la base de datos 'multigroup' con usuario root y sin contraseña.
     * 3) Almacenar la conexión en el ServletContext para que los DAOs puedan recuperarla.
     *
     * @param sce Evento que provee acceso al ServletContext.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // URL de conexión JDBC: base de datos 'multigroup' en localhost, puerto 3306.
            // useSSL=false para evitar advertencias, serverTimezone=UTC para consistencia de fechas.
            String url  = "jdbc:mysql://localhost:3306/multigroup?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String pass = "";  // Sin contraseña para root

            // Carga del driver JDBC de MySQL en tiempo de ejecución
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establece la conexión utilizando URL, usuario y contraseña
            conn = DriverManager.getConnection(url, user, pass);

            // Almacena la conexión abierta en el contexto de la aplicación para que otros componentes la usen
            sce.getServletContext().setAttribute("DBConnection", conn);
            System.out.println(">>> Conexión a BD 'multigroup' establecida con root sin contraseña");
        } catch (Exception e) {
            // Si ocurre cualquier error (clase no encontrada, SQL exception, etc.), lanza RuntimeException
            throw new RuntimeException("Error inicializando conexión a BD", e);
        }
    }

    /**
     * Método llamado cuando se destruye el contexto de la aplicación (server shutdown o redeploy).
     * Se encarga de:
     * 1) Cerrar la conexión JDBC si está abierta.
     * 2) Limpiar hilos que MySQL pueda haber dejado activos.
     * 3) Deregistrar drivers JDBC para evitar fugas de memoria en servidores como Tomcat.
     *
     * @param sce Evento que provee acceso al ServletContext.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1) Cerramos la conexión compartida si no es null
        if (conn != null) {
            try {
                conn.close();
                System.out.println(">>> Conexión a BD cerrada");
            } catch (SQLException ignore) {
                // Ignorar excepciones al cerrar para no interrumpir el shutdown
            }
        }

        // 2) Limpieza de hilos internos de MySQL para evitar fugas de memoria
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            // Ignorar cualquier error durante la limpieza de hilos
        }

        // 3) Deregistrar todos los drivers JDBC registrados en DriverManager
        //    Evita fugas de memoria en contenedores (por ejemplo, Tomcat) que recargan clases.
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ignore) {
                // Ignorar errores al deregistrar cada driver
            }
        }
    }
}
