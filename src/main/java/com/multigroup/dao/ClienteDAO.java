package com.multigroup.dao;

import com.multigroup.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Clientes, con CRUD completo, baja lógica, eliminación física y chequeo de duplicados.
 */
public class ClienteDAO {
    private final Connection conn;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT * FROM clientes";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Cliente findById(int idCliente) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean insert(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes(" +
                "nombre, documento, tipo_persona, telefono, correo, direccion, estado, creado_por, fecha_creacion" +
                ") VALUES(?,?,?,?,?,?,?,? ,CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDocumento());
            stmt.setString(3, c.getTipoPersona());
            stmt.setString(4, c.getTelefono());
            stmt.setString(5, c.getCorreo());
            stmt.setString(6, c.getDireccion());
            stmt.setString(7, c.getEstado());
            stmt.setString(8, c.getCreadoPor());
            int afect = stmt.executeUpdate();
            if (afect == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        c.setIdCliente(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public boolean update(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET " +
                "nombre=?, documento=?, tipo_persona=?, telefono=?, correo=?, direccion=?, estado=?, fecha_actualizacion=CURRENT_TIMESTAMP " +
                "WHERE id_cliente=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDocumento());
            stmt.setString(3, c.getTipoPersona());
            stmt.setString(4, c.getTelefono());
            stmt.setString(5, c.getCorreo());
            stmt.setString(6, c.getDireccion());
            stmt.setString(7, c.getEstado());
            stmt.setInt(8, c.getIdCliente());
            return stmt.executeUpdate() == 1;
        }
    }

    /** Baja lógica: marca Inactivo y fecha_inactivacion */
    public boolean delete(int idCliente) throws SQLException {
        String sql = "UPDATE clientes SET estado='Inactivo', fecha_inactivacion=CURRENT_TIMESTAMP WHERE id_cliente=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() == 1;
        }
    }

    /** Reactivación */
    public boolean activate(int idCliente) throws SQLException {
        String sql = "UPDATE clientes SET estado='Activo', fecha_inactivacion=NULL, fecha_actualizacion=CURRENT_TIMESTAMP WHERE id_cliente=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() == 1;
        }
    }

    /** Eliminación física */
    public boolean remove(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() == 1;
        }
    }

    /** Comprueba si ya existe un cliente con ese documento */
    public boolean existsByDocumento(String documento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE documento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setNombre(rs.getString("nombre"));
        c.setDocumento(rs.getString("documento"));
        c.setTipoPersona(rs.getString("tipo_persona"));
        c.setTelefono(rs.getString("telefono"));
        c.setCorreo(rs.getString("correo"));
        c.setDireccion(rs.getString("direccion"));
        c.setEstado(rs.getString("estado"));
        c.setCreadoPor(rs.getString("creado_por"));
        c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        c.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        c.setFechaInactivacion(rs.getTimestamp("fecha_inactivacion"));
        return c;
    }
}
