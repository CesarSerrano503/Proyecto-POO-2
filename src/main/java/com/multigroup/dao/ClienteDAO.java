package com.multigroup.dao;

import com.multigroup.model.Cliente;
import com.multigroup.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String SQL_FIND_ALL =
            "SELECT id_cliente, nombre, documento, tipo_persona, telefono, correo, direccion, estado "
                    + "FROM clientes";

    public List<Cliente> findAll() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("tipo_persona"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getString("estado")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    // Aquí más métodos CRUD: insert, update, delete, findById...
}
