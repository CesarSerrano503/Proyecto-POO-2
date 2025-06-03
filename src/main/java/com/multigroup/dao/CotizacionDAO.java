/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 28/5/2025
 *
 * DAO para la entidad Cotizacion, con JOIN para traer el nombre de cliente.
 * Incluye operaciones CRUD, baja lógica, finalización y eliminación física.
 */
package com.multigroup.dao;

import com.multigroup.model.Cotizacion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CotizacionDAO {
    // Conexión a la base de datos proporcionada por el servlet context
    private final Connection conn;

    /**
     * Constructor que recibe y almacena la conexión a la base de datos.
     *
     * @param conn Conexión JDBC a utilizar para todas las operaciones.
     */
    public CotizacionDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Busca una cotización por su ID, devolviendo el objeto completo junto con el nombre de cliente.
     *
     * @param id ID de la cotización a buscar.
     * @return Objeto Cotizacion si se encuentra; null si no existe.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
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
     * Lista todas las cotizaciones junto con el nombre de cliente asociado.
     *
     * @return Lista de objetos Cotizacion representando cada fila.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
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
                // Mapear cada fila a un objeto Cotizacion y añadir a la lista
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    /**
     * Inserta una nueva cotización en la base de datos.
     * IMPORTANTE: El campo 'fecha_creacion' se gestiona automáticamente en la BD.
     *
     * @param c Objeto Cotizacion con datos a insertar (excepto ID y fechas automáticas).
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
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
            // Asignar parámetros en orden: index 1 a 9
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
                // Recuperar la clave generada automáticamente (id_cotizacion)
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
     * También establece 'fecha_actualizacion' con CURRENT_TIMESTAMP.
     *
     * @param c Objeto Cotizacion con ID y nuevos valores a actualizar.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
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
            // Asignar parámetros en el mismo orden que en la consulta
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
     * Baja lógica de la cotización: marca 'estado' como 'Inactivo' y fija 'fecha_finalizacion' con CURRENT_TIMESTAMP.
     *
     * @param idCotizacion ID de la cotización a inactivar.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
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
     * Marca la cotización como 'Finalizada' y fija 'fecha_finalizacion' con CURRENT_TIMESTAMP.
     *
     * @param idCotizacion ID de la cotización a finalizar.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
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
     * Eliminación física de la cotización de la base de datos.
     *
     * @param idCotizacion ID de la cotización a eliminar.
     * @return true si la eliminación afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean remove(int idCotizacion) throws SQLException {
        String sql = "DELETE FROM cotizaciones WHERE id_cotizacion = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idCotizacion);
            return st.executeUpdate() == 1;
        }
    }

    /**
     * Mapea una fila del ResultSet a un objeto Cotizacion, incluyendo el nombre del cliente.
     *
     * @param rs ResultSet posicionado en la fila a mapear.
     * @return Instancia de Cotizacion con todos los campos poblados desde la BD.
     * @throws SQLException si ocurre un error al leer del ResultSet.
     */
    private Cotizacion mapRow(ResultSet rs) throws SQLException {
        Cotizacion c = new Cotizacion();
        c.setIdCotizacion(rs.getInt("id_cotizacion"));
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setClienteNombre(rs.getString("cliente_nombre")); // Nombre del cliente desde JOIN
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
