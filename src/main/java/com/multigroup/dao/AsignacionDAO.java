/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * DAO para manejar operaciones CRUD de la entidad Asignación.
 * Provee métodos para encontrar, insertar, actualizar y eliminar registros
 * en la tabla "asignaciones" de la base de datos.
 */
package com.multigroup.dao;

import com.multigroup.model.Asignacion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {
    // Conexión a la base de datos provista por el ServletContext
    private final Connection conn;

    /**
     * Constructor que inicializa DAO con la conexión a la BD.
     * @param conn Conexión JDBC establecida
     */
    public AsignacionDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Obtiene todas las asignaciones asociadas a una cotización específica.
     * @param idCot ID de la cotización
     * @return Lista de objetos Asignación
     * @throws SQLException si ocurre error en la consulta SQL
     */
    public List<Asignacion> findByCotizacion(int idCot) throws SQLException {
        String sql = "SELECT * FROM asignaciones WHERE id_cotizacion = ?";
        List<Asignacion> lista = new ArrayList<>();
        // Prepara sentencia con parámetro para evitar SQL Injection
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCot);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Mapea cada fila a un objeto Asignacion y lo agrega a la lista
                    lista.add(mapRow(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Busca una asignación por su ID.
     * @param idAsig ID de la asignación
     * @return Objeto Asignacion si existe, o null en caso contrario
     * @throws SQLException si ocurre error en la consulta SQL
     */
    public Asignacion findById(int idAsig) throws SQLException {
        String sql = "SELECT * FROM asignaciones WHERE id_asignacion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsig);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retorna el objeto mapeado si se encuentra el registro
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    /**
     * Inserta una nueva asignación en la base de datos.
     * @param a Objeto Asignacion con los datos a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     * @throws SQLException si ocurre error en la inserción SQL
     */
    public boolean insert(Asignacion a) throws SQLException {
        String sql = "INSERT INTO asignaciones(" +
                "id_cotizacion, id_empleado, area, costo_hora, fecha_inicio, fecha_fin, horas_estimadas, " +
                "titulo_actividad, tareas, costo_base, incremento_pct, total" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        // Uso de Statement.RETURN_GENERATED_KEYS para obtener el ID auto-generado
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
                // Si se insertó correctamente, obtiene el ID generado
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

    /**
     * Actualiza una asignación existente en la base de datos.
     * @param a Objeto Asignacion con los datos actualizados (incluye ID)
     * @return true si la actualización afectó a un registro, false en caso contrario
     * @throws SQLException si ocurre error en la actualización SQL
     */
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
            // Ejecuta la actualización y verifica si se afectó un registro
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Elimina físicamente una asignación de la base de datos.
     * @param idAsig ID de la asignación a eliminar
     * @return true si se eliminó un registro, false en caso contrario
     * @throws SQLException si ocurre error en la eliminación SQL
     */
    public boolean remove(int idAsig) throws SQLException {
        String sql = "DELETE FROM asignaciones WHERE id_asignacion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAsig);
            // Ejecuta el borrado y verifica si se afectó un registro
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Mapea el ResultSet a un objeto Asignacion.
     * Este método se utiliza internamente tras ejecutar consultas.
     * @param rs ResultSet apuntando a la fila actual
     * @return Objeto Asignacion con los datos de la fila
     * @throws SQLException si ocurre error al leer columnas
     */
    private Asignacion mapRow(ResultSet rs) throws SQLException {
        Asignacion a = new Asignacion();
        a.setIdAsignacion(rs.getInt("id_asignacion"));
        a.setIdCotizacion(rs.getInt("id_cotizacion"));
        a.setIdEmpleado(rs.getInt("id_empleado"));
        a.setArea(rs.getString("area"));
        a.setCostoHora(rs.getDouble("costo_hora"));
        // Convierte Timestamp a LocalDateTime para el modelo
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
