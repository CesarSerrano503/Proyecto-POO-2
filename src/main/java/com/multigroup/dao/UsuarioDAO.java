/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 */
package com.multigroup.dao;

import com.multigroup.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public Usuario validarLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND LOWER(estado) = 'activo'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        }
        return lista;
    }

    public void insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios "
                + "(username, password, rol, estado, creado_por, fecha_creacion, fecha_actualizacion) "
                + "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            ps.setString(5, u.getCreadoPor());
            ps.executeUpdate();
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    public void actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET username = ?, password = ?, rol = ?, estado = ?, fecha_actualizacion = NOW() WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            ps.setInt(5, u.getIdUsuario());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "UPDATE usuarios SET estado = 'inactivo', fecha_actualizacion = NOW() WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("rol"),
                rs.getString("estado"),
                rs.getString("creado_por"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getTimestamp("fecha_actualizacion").toLocalDateTime()
        );
    }
}
