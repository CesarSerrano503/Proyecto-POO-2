/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * DAO para Empleado, con CRUD completo, baja lógica, reactivación,
 * eliminación física, chequeo de duplicados y búsqueda por documento.
 */
package com.multigroup.dao;

import com.multigroup.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    // Conexión a la base de datos proporcionada por el servlet context
    private final Connection conn;

    /**
     * Constructor que recibe y almacena la conexión a la base de datos.
     *
     * @param conn Conexión JDBC a utilizar para todas las operaciones.
     */
    public EmpleadoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Obtiene todos los registros de la tabla 'empleados'.
     *
     * @return Lista de objetos Empleado representando cada fila.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Empleado> findAll() throws SQLException {
        String sql = "SELECT * FROM empleados";
        List<Empleado> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Mapear cada fila a un objeto Empleado y añadir a la lista
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    /**
     * Busca un empleado por su ID.
     *
     * @param idEmpleado ID del empleado a buscar.
     * @return Objeto Empleado si se encuentra; null si no existe.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public Empleado findById(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Busca un empleado por su documento.
     * Esto se utiliza para la validación de duplicados, tanto en creación como en edición.
     *
     * @param documento Valor del campo 'documento' a buscar.
     * @return Objeto Empleado si se encuentra uno con ese documento; null si no existe.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public Empleado findByDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE documento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Inserta un nuevo empleado en la base de datos.
     * El campo 'fecha_creacion' se establece automáticamente con CURRENT_TIMESTAMP.
     *
     * @param e Objeto Empleado con datos a insertar (excepto ID y fecha).
     * @return true si la inserción fue exitosa (affectó 1 fila); false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean insert(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleados(" +
                "nombre, documento, tipo_persona, tipo_contratacion, telefono, " +
                "correo, direccion, estado, creado_por, fecha_creacion" +
                ") VALUES(?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignar parámetros con setters según orden de columnas
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
                // Recuperar la clave generada automáticamente (id_empleado)
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        e.setIdEmpleado(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Actualiza los datos de un empleado ya existente.
     * También establece 'fecha_actualizacion' a CURRENT_TIMESTAMP.
     *
     * @param e Objeto Empleado con ID y nuevos valores en sus campos.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean update(Empleado e) throws SQLException {
        String sql = "UPDATE empleados SET " +
                "nombre=?, documento=?, tipo_persona=?, tipo_contratacion=?, telefono=?, " +
                "correo=?, direccion=?, estado=?, fecha_actualizacion=CURRENT_TIMESTAMP " +
                "WHERE id_empleado=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar parámetros en el mismo orden que en la consulta
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

    /**
     * Baja lógica de un empleado: marca 'estado' como 'Inactivo' y fija 'fecha_inactivacion' con CURRENT_TIMESTAMP.
     *
     * @param idEmpleado ID del empleado a inactivar.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean delete(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET " +
                "estado='Inactivo', fecha_inactivacion=CURRENT_TIMESTAMP " +
                "WHERE id_empleado=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Reactiva un empleado que estuvo inactivo: pone 'estado' en 'Activo',
     * borra 'fecha_inactivacion' (NULL) y actualiza 'fecha_actualizacion'.
     *
     * @param idEmpleado ID del empleado a reactivar.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean activate(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET " +
                "estado='Activo', fecha_inactivacion=NULL, fecha_actualizacion=CURRENT_TIMESTAMP " +
                "WHERE id_empleado=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Eliminación física de un empleado de la base de datos.
     *
     * @param idEmpleado ID del empleado a eliminar.
     * @return true si la eliminación afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean remove(int idEmpleado) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id_empleado = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Mapea una fila del ResultSet a un objeto Empleado.
     *
     * @param rs ResultSet posicionado en la fila a mapear.
     * @return Instancia de Empleado con todos los campos poblados desde la BD.
     * @throws SQLException si ocurre un error al leer del ResultSet.
     */
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
}
