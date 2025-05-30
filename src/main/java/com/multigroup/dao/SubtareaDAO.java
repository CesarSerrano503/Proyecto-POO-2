package com.multigroup.dao;

import com.multigroup.model.Subtarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubtareaDAO {
    private final Connection conn;

    public SubtareaDAO(Connection conn) {
        this.conn = conn;
    }

    /** Recupera todas las subtareas de una asignación */
    public List<Subtarea> findByAsignacion(int idAsig) throws SQLException {
        String sql = "SELECT * FROM subtareas WHERE id_asignacion = ?";
        List<Subtarea> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsig);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subtarea s = new Subtarea();
                    s.setIdSubtarea(rs.getInt("id_subtarea"));
                    s.setIdAsignacion(rs.getInt("id_asignacion"));
                    s.setTituloSubtarea(rs.getString("titulo_subtarea"));
                    s.setDescripcionSubtarea(rs.getString("descripcion_subtarea"));
                    lista.add(s);
                }
            }
        }
        return lista;
    }

    /** Inserta una nueva subtarea */
    public boolean insert(Subtarea s) throws SQLException {
        String sql = "INSERT INTO subtareas (id_asignacion, titulo_subtarea, descripcion_subtarea) VALUES (?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, s.getIdAsignacion());
            stmt.setString(2, s.getTituloSubtarea());
            stmt.setString(3, s.getDescripcionSubtarea());
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        s.setIdSubtarea(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /** Actualiza una subtarea existente */
    public boolean update(Subtarea s) throws SQLException {
        String sql = "UPDATE subtareas SET titulo_subtarea = ?, descripcion_subtarea = ? WHERE id_subtarea = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getTituloSubtarea());
            stmt.setString(2, s.getDescripcionSubtarea());
            stmt.setInt(3, s.getIdSubtarea());
            return stmt.executeUpdate() == 1;
        }
    }

    /** Elimina una subtarea por su ID */
    public boolean remove(int idSubtarea) throws SQLException {
        String sql = "DELETE FROM subtareas WHERE id_subtarea = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSubtarea);
            return stmt.executeUpdate() == 1;
        }
    }
}
