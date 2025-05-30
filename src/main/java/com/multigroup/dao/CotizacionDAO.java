/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 28/5/2025
 */
package com.multigroup.dao;

import com.multigroup.model.Cotizacion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Cotizacion, con JOIN para traer el nombre de cliente.
 */
public class CotizacionDAO {
    private final Connection conn;

    public CotizacionDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Busca una cotización por su ID.
     */
    public Cotizacion findById(int id) throws SQLException {
        String sql = """
            SELECT c.*,
                   cli.nombre AS cliente_nombre
              FROM cotizaciones c
              JOIN clientes cli 
                ON c.id_cliente = cli.id_cliente
             WHERE c.id_cotizacion = ?
            """;
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Lista todas las cotizaciones junto con el nombre de cliente.
     */
    public List<Cotizacion> findAll() throws SQLException {
        String sql = """
            SELECT c.*,
                   cli.nombre AS cliente_nombre
              FROM cotizaciones c
              JOIN clientes cli 
                ON c.id_cliente = cli.id_cliente
            """;
        List<Cotizacion> lista = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    /**
     * Inserta una nueva cotización.
     */
    public boolean insert(Cotizacion c) throws SQLException {
        String sql = """
            INSERT INTO cotizaciones(
              id_cliente, estado,
              total_horas, fecha_inicio, fecha_fin,
              costo_asignaciones, costos_adicionales, total,
              creado_por
            ) VALUES (?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, c.getIdCliente());
            st.setString(2, c.getEstado());
            st.setBigDecimal(3, c.getTotalHoras());
            st.setTimestamp(4, c.getFechaInicio());
            st.setTimestamp(5, c.getFechaFin());
            st.setBigDecimal(6, c.getCostoAsignaciones());
            st.setBigDecimal(7, c.getCostosAdicionales());
            st.setBigDecimal(8, c.getTotal());
            st.setString(9, c.getCreadoPor());
            int affected = st.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = st.getGeneratedKeys()) {
                    if (keys.next()) {
                        c.setIdCotizacion(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Actualiza los datos de una cotización existente.
     */
    public boolean update(Cotizacion c) throws SQLException {
        String sql = """
            UPDATE cotizaciones SET
              id_cliente             = ?,
              total_horas            = ?,
              fecha_inicio           = ?,
              fecha_fin              = ?,
              costo_asignaciones     = ?,
              costos_adicionales     = ?,
              total                  = ?,
              estado                 = ?,
              fecha_actualizacion    = CURRENT_TIMESTAMP
             WHERE id_cotizacion      = ?
            """;
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, c.getIdCliente());
            st.setBigDecimal(2, c.getTotalHoras());
            st.setTimestamp(3, c.getFechaInicio());
            st.setTimestamp(4, c.getFechaFin());
            st.setBigDecimal(5, c.getCostoAsignaciones());
            st.setBigDecimal(6, c.getCostosAdicionales());
            st.setBigDecimal(7, c.getTotal());
            st.setString(8, c.getEstado());
            st.setInt(9, c.getIdCotizacion());
            return st.executeUpdate() == 1;
        }
    }

    /**
     * Baja lógica: marca como 'Inactivo' y pone fecha_finalizacion.
     */
    public boolean delete(int idCotizacion) throws SQLException {
        String sql = """
            UPDATE cotizaciones SET
              estado             = 'Inactivo',
              fecha_finalizacion = CURRENT_TIMESTAMP
             WHERE id_cotizacion  = ?
            """;
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idCotizacion);
            return st.executeUpdate() == 1;
        }
    }

    /**
     * Marca como 'Finalizada' y fija fecha_finalizacion.
     */
    public boolean finalize(int idCotizacion) throws SQLException {
        String sql = """
            UPDATE cotizaciones SET
              estado             = 'Finalizada',
              fecha_finalizacion = CURRENT_TIMESTAMP
             WHERE id_cotizacion  = ?
            """;
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idCotizacion);
            return st.executeUpdate() == 1;
        }
    }

    /**
     * Eliminación física de la cotización.
     */
    public boolean remove(int idCotizacion) throws SQLException {
        String sql = "DELETE FROM cotizaciones WHERE id_cotizacion = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idCotizacion);
            return st.executeUpdate() == 1;
        }
    }

    /**
     * Mapea una fila del ResultSet a un objeto Cotizacion.
     */
    private Cotizacion mapRow(ResultSet rs) throws SQLException {
        Cotizacion c = new Cotizacion();
        c.setIdCotizacion(rs.getInt("id_cotizacion"));
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setClienteNombre(rs.getString("cliente_nombre"));
        c.setEstado(rs.getString("estado"));
        c.setTotalHoras(rs.getBigDecimal("total_horas"));
        c.setFechaInicio(rs.getTimestamp("fecha_inicio"));
        c.setFechaFin(rs.getTimestamp("fecha_fin"));
        c.setCostoAsignaciones(rs.getBigDecimal("costo_asignaciones"));
        c.setCostosAdicionales(rs.getBigDecimal("costos_adicionales"));
        c.setTotal(rs.getBigDecimal("total"));
        c.setCreadoPor(rs.getString("creado_por"));
        c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        c.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        c.setFechaFinalizacion(rs.getTimestamp("fecha_finalizacion"));
        return c;
    }
}
