package com.multigroup.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Agregamos allowPublicKeyRetrieval=true junto a useSSL=false
    private static final String URL =
            "jdbc:mysql://localhost:3306/multigroup"
                    + "?allowPublicKeyRetrieval=true"
                    + "&useSSL=false"
                    + "&serverTimezone=UTC";
    private static final String USER = "mguser";
    private static final String PASS = "mgpass";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver no encontrado", e);
        }
    }

    /**
     * Devuelve una nueva conexión a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
