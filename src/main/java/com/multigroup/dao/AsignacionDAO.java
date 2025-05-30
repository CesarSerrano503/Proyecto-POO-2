package com.multigroup.dao;

import com.multigroup.model.Asignacion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {
    private final Connection conn;

    public AsignacionDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Asignacion> findByCotizacion(int idCot) throws SQLException {
        String sql = "SELECT * FROM asignaciones WHERE id_cotizacion = ?";
        List<Asignacion> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCot);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    public Asignacion findById(int idAsig) throws SQLException {
        String sql = "SELECT * FROM asignaciones WHERE id_asignacion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsig);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public boolean insert(Asignacion a) throws SQLException {
        String sql = "INSERT INTO asignaciones(" +
                "id_cotizacion, id_empleado, area, costo_hora, fecha_inicio, fecha_fin, horas_estimadas, " +
                "titulo_actividad, tareas, costo_base, incremento_pct, total" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, a.getIdCotizacion());
            stmt.setInt(2, a.getIdEmpleado());
            stmt.setString(3, a.getArea());
            stmt.setDouble(4, a.getCostoHora());
            stmt.setTimestamp(5, Timestamp.valueOf(a.getFechaInicio()));
            stmt.setTimestamp(6, Timestamp.valueOf(a.getFechaFin()));
            stmt.setInt(7, a.getHorasEstimadas());
            stmt.setString(8, a.getTituloActividad());
            stmt.setString(9, a.getTareas());
            stmt.setDouble(10, a.getCostoBase());
            stmt.setDouble(11, a.getIncrementoPct());
            stmt.setDouble(12, a.getTotal());
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        a.setIdAsignacion(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public boolean update(Asignacion a) throws SQLException {
        String sql = "UPDATE asignaciones SET " +
                "id_empleado=?, area=?, costo_hora=?, fecha_inicio=?, fecha_fin=?, " +
                "horas_estimadas=?, titulo_actividad=?, tareas=?, costo_base=?, incremento_pct=?, total=? " +
                "WHERE id_asignacion=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getIdEmpleado());
            stmt.setString(2, a.getArea());
            stmt.setDouble(3, a.getCostoHora());
            stmt.setTimestamp(4, Timestamp.valueOf(a.getFechaInicio()));
            stmt.setTimestamp(5, Timestamp.valueOf(a.getFechaFin()));
            stmt.setInt(6, a.getHorasEstimadas());
            stmt.setString(7, a.getTituloActividad());
            stmt.setString(8, a.getTareas());
            stmt.setDouble(9, a.getCostoBase());
            stmt.setDouble(10, a.getIncrementoPct());
            stmt.setDouble(11, a.getTotal());
            stmt.setInt(12, a.getIdAsignacion());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean remove(int idAsig) throws SQLException {
        String sql = "DELETE FROM asignaciones WHERE id_asignacion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsig);
            return stmt.executeUpdate() == 1;
        }
    }

    private Asignacion mapRow(ResultSet rs) throws SQLException {
        Asignacion a = new Asignacion();
        a.setIdAsignacion(rs.getInt("id_asignacion"));
        a.setIdCotizacion(rs.getInt("id_cotizacion"));
        a.setIdEmpleado(rs.getInt("id_empleado"));
        a.setArea(rs.getString("area"));
        a.setCostoHora(rs.getDouble("costo_hora"));
        a.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
        a.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
        a.setHorasEstimadas(rs.getInt("horas_estimadas"));
        a.setTituloActividad(rs.getString("titulo_actividad"));
        a.setTareas(rs.getString("tareas"));
        a.setCostoBase(rs.getDouble("costo_base"));
        a.setIncrementoPct(rs.getDouble("incremento_pct"));
        a.setTotal(rs.getDouble("total"));
        return a;
    }
}
