package com.multigroup.dao;

import com.multigroup.model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Data Access Object (DAO) para Empleado.
 */
public class EmpleadoDAO {
    private final Connection conn;

    public EmpleadoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Empleado> findAll() throws SQLException {
        String sql = "SELECT * FROM empleados";
        List<Empleado> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Empleado findById(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean insert(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleados(nombre, documento, tipo_persona, tipo_contratacion, telefono, " +
                "correo, direccion, estado, creado_por, fecha_creacion) VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getDocumento());
            stmt.setString(3, e.getTipoPersona());
            stmt.setString(4, e.getTipoContratacion());
            stmt.setString(5, e.getTelefono());
            stmt.setString(6, e.getCorreo());
            stmt.setString(7, e.getDireccion());
            stmt.setString(8, e.getEstado());
            stmt.setString(9, e.getCreadoPor());
            int afect = stmt.executeUpdate();
            if (afect == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        e.setIdEmpleado(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Empleado e) throws SQLException {
        String sql = "UPDATE empleados SET nombre=?, documento=?, tipo_persona=?, tipo_contratacion=?, telefono=?, " +
                "correo=?, direccion=?, estado=?, fecha_actualizacion=CURRENT_TIMESTAMP WHERE id_empleado=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getDocumento());
            stmt.setString(3, e.getTipoPersona());
            stmt.setString(4, e.getTipoContratacion());
            stmt.setString(5, e.getTelefono());
            stmt.setString(6, e.getCorreo());
            stmt.setString(7, e.getDireccion());
            stmt.setString(8, e.getEstado());
            stmt.setInt(9, e.getIdEmpleado());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean delete(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET estado='Inactivo', fecha_inactivacion=CURRENT_TIMESTAMP WHERE id_empleado=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean activate(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET estado='Activo', fecha_inactivacion=NULL, fecha_actualizacion=CURRENT_TIMESTAMP WHERE id_empleado=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() == 1;
        }
    }

    private Empleado mapRow(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdEmpleado(rs.getInt("id_empleado"));
        e.setNombre(rs.getString("nombre"));
        e.setDocumento(rs.getString("documento"));
        e.setTipoPersona(rs.getString("tipo_persona"));
        e.setTipoContratacion(rs.getString("tipo_contratacion"));
        e.setTelefono(rs.getString("telefono"));
        e.setCorreo(rs.getString("correo"));
        e.setDireccion(rs.getString("direccion"));
        e.setEstado(rs.getString("estado"));
        e.setCreadoPor(rs.getString("creado_por"));
        e.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        e.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        e.setFechaInactivacion(rs.getTimestamp("fecha_inactivacion"));
        return e;
    }

    /**
     * Limpia drivers MySQL y AbandonedConnectionCleanupThread.
     * Llamar en contextDestroyed.
     */
    public static void cleanup() {
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception ignored) { }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException ignored) { }
        }
    }
}
