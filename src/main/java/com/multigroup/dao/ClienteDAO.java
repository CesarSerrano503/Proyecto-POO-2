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
            "SELECT id_cliente, nombre, documento, tipo_persona, telefono, correo, direccion, estado " +
                    "FROM clientes";

    private static final String SQL_FIND_BY_ID =
            "SELECT id_cliente, nombre, documento, tipo_persona, telefono, correo, direccion, estado " +
                    "FROM clientes WHERE id_cliente = ?";

    private static final String SQL_INSERT =
            "INSERT INTO clientes " +
                    "(nombre, documento, tipo_persona, telefono, correo, direccion, estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'Activo')";

    private static final String SQL_UPDATE =
            "UPDATE clientes SET " +
                    "nombre = ?, documento = ?, tipo_persona = ?, telefono = ?, correo = ?, direccion = ?, estado = ? " +
                    "WHERE id_cliente = ?";

    private static final String SQL_DELETE =
            "DELETE FROM clientes WHERE id_cliente = ?";

    /** Recupera todos los clientes. */
    public List<Cliente> findAll() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRowToCliente(rs));
            }
        }
        return lista;
    }

    /** Recupera un cliente por su ID. */
    public Cliente findById(int id) throws Exception {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCliente(rs);
                }
            }
        }
        return null;
    }

    /** Inserta un nuevo cliente. */
    public void insert(Cliente c) throws Exception {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDocumento());
            ps.setString(3, c.getTipoPersona());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getDireccion());
            ps.executeUpdate();
        }
    }

    /** Actualiza un cliente existente. */
    public void update(Cliente c) throws Exception {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDocumento());
            ps.setString(3, c.getTipoPersona());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getEstado());
            ps.setInt(8, c.getId());
            ps.executeUpdate();
        }
    }

    /** Elimina un cliente por su ID. */
    public void delete(int id) throws Exception {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Mapea una fila del ResultSet a un objeto Cliente. */
    private Cliente mapRowToCliente(ResultSet rs) throws Exception {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("nombre"),
                rs.getString("documento"),
                rs.getString("tipo_persona"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("direccion"),
                rs.getString("estado")
        );
    }
}
