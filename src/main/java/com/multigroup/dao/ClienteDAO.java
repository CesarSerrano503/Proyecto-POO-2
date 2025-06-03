/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * DAO para Clientes, con CRUD completo, baja lógica, eliminación física y chequeo de duplicados.
 */
package com.multigroup.dao;

import com.multigroup.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    // Conexión a la base de datos proporcionada por el servlet context
    private final Connection conn;

    /**
     * Constructor que recibe y almacena la conexión a la base de datos.
     *
     * @param conn Conexión JDBC a utilizar para todas las operaciones.
     */
    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Obtiene todos los registros de la tabla 'clientes'.
     *
     * @return Lista de objetos Cliente representando cada fila.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT * FROM clientes";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Mapear cada fila a un objeto Cliente y añadir a la lista
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param idCliente ID del cliente a buscar.
     * @return Objeto Cliente si se encuentra; null si no existe.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public Cliente findById(int idCliente) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Inserta un nuevo cliente en la base de datos.
     * El campo 'fecha_creacion' se establece automáticamente con CURRENT_TIMESTAMP.
     *
     * @param c Objeto Cliente con datos a insertar (excepto ID y fecha).
     * @return true si la inserción fue exitosa (affectó 1 fila); false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean insert(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes(" +
                "nombre, documento, tipo_persona, telefono, correo, direccion, estado, creado_por, fecha_creacion" +
                ") VALUES(?,?,?,?,?,?,?,? ,CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignar parámetros con setters según orden de columnas
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
                // Recuperar la clave generada automáticamente (id_cliente)
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

    /**
     * Actualiza los datos de un cliente ya existente.
     * También establece 'fecha_actualizacion' a CURRENT_TIMESTAMP.
     *
     * @param c Objeto Cliente con ID y nuevos valores en sus campos.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean update(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET " +
                "nombre=?, documento=?, tipo_persona=?, telefono=?, correo=?, direccion=?, estado=?, fecha_actualizacion=CURRENT_TIMESTAMP " +
                "WHERE id_cliente=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar parámetros en el mismo orden que en la consulta
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

    /**
     * Realiza una baja lógica del cliente: cambia 'estado' a 'Inactivo' y fija 'fecha_inactivacion' con CURRENT_TIMESTAMP.
     *
     * @param idCliente ID del cliente a inactivar.
     * @return true si la actualización afectó 1 fila; false de lo contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean delete(int idCliente) throws SQLException {
        String sql = "UPDATE clientes SET estado='Inactivo', fecha_inactivacion=CURRENT_TIMESTAMP WHERE id_cliente=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Reactiva un cliente que estuvo inactivo: pone 'estado' en 'Activo',
     * borra 'fecha_inactivacion' (NULL) y actualiza 'fecha_actualizacion'.
     *
     * @param idCliente ID del cliente a reactivar.
     * @return true si la actualización afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean activate(int idCliente) throws SQLException {
        String sql = "UPDATE clientes SET estado='Activo', fecha_inactivacion=NULL, fecha_actualizacion=CURRENT_TIMESTAMP WHERE id_cliente=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Elimina físicamente un cliente de la base de datos.
     *
     * @param idCliente ID del cliente a eliminar.
     * @return true si la eliminación afectó 1 fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean remove(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Comprueba si ya existe un cliente con el mismo documento, para evitar duplicados
     * al crear uno nuevo.
     *
     * @param documento Valor del campo 'documento' a verificar.
     * @return true si existe al menos un registro con ese documento; false si no hay ninguno.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public boolean existsByDocumento(String documento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE documento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                // Si el conteo es mayor a 0, existe duplicado
                return rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Mapea una fila del ResultSet a un objeto Cliente.
     *
     * @param rs ResultSet posicionado en la fila a mapear.
     * @return Instancia de Cliente con todos los campos poblados desde la BD.
     * @throws SQLException si ocurre error al leer del ResultSet.
     */
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
        // Timestamp a java.util.Date o java.sql.Timestamp según modelo
        c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        c.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        c.setFechaInactivacion(rs.getTimestamp("fecha_inactivacion"));
        return c;
    }
}
