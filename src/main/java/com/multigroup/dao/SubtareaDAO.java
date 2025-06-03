/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * DAO para Subtarea, con operaciones para recuperar, insertar, actualizar y eliminar subtareas.
 */
package com.multigroup.dao;

import com.multigroup.model.Subtarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubtareaDAO {
    // Conexión a la base de datos proporcionada por el servlet context
    private final Connection conn;

    /**
     * Constructor que recibe y almacena la conexión a la base de datos.
     *
     * @param conn Conexión JDBC a utilizar para todas las operaciones.
     */
    public SubtareaDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Recupera todas las subtareas asociadas a una asignación dada.
     *
     * @param idAsig ID de la asignación padre cuyas subtareas se desean listar.
     * @return Lista de objetos Subtarea con los datos de cada fila encontrada.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
    public List<Subtarea> findByAsignacion(int idAsig) throws SQLException {
        // SQL para seleccionar todas las subtareas con el id_asignacion dado
        String sql = "SELECT * FROM subtareas WHERE id_asignacion = ?";
        List<Subtarea> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asigna el parámetro id_asignacion al PreparedStatement
            stmt.setInt(1, idAsig);

            try (ResultSet rs = stmt.executeQuery()) {
                // Itera sobre el ResultSet y mapea cada fila a un objeto Subtarea
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

    /**
     * Inserta una nueva subtarea en la base de datos.
     * Después de la inserción, asigna el id generado al objeto Subtarea.
     *
     * @param s Objeto Subtarea con idAsignacion, tituloSubtarea y descripcionSubtarea.
     * @return true si la inserción afectó una fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean insert(Subtarea s) throws SQLException {
        // SQL para insertar una subtarea (id_asignacion, título y descripción)
        String sql = "INSERT INTO subtareas (id_asignacion, titulo_subtarea, descripcion_subtarea) VALUES (?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asigna valores a los parámetros: idAsignacion, tituloSubtarea y descripcionSubtarea
            stmt.setInt(1, s.getIdAsignacion());
            stmt.setString(2, s.getTituloSubtarea());
            stmt.setString(3, s.getDescripcionSubtarea());

            int affected = stmt.executeUpdate();
            if (affected == 1) {
                // Si se insertó correctamente, recupera la clave generada (id_subtarea)
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

    /**
     * Actualiza los datos de una subtarea existente (título y descripción).
     *
     * @param s Objeto Subtarea con idSubtarea, tituloSubtarea y descripcionSubtarea ya modificados.
     * @return true si la actualización afectó una fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean update(Subtarea s) throws SQLException {
        // SQL para actualizar título y descripción según id_subtarea
        String sql = "UPDATE subtareas SET titulo_subtarea = ?, descripcion_subtarea = ? WHERE id_subtarea = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar parámetros: nuevo título, nueva descripción e ID de la subtarea a actualizar
            stmt.setString(1, s.getTituloSubtarea());
            stmt.setString(2, s.getDescripcionSubtarea());
            stmt.setInt(3, s.getIdSubtarea());

            return stmt.executeUpdate() == 1;
        }
    }

    /**
     * Elimina físicamente una subtarea de la base de datos.
     *
     * @param idSubtarea ID de la subtarea a eliminar.
     * @return true si la eliminación afectó una fila; false en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    public boolean remove(int idSubtarea) throws SQLException {
        // SQL para eliminar una subtarea según id_subtarea
        String sql = "DELETE FROM subtareas WHERE id_subtarea = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar el ID de la subtarea al PreparedStatement
            stmt.setInt(1, idSubtarea);
            return stmt.executeUpdate() == 1;
        }
    }
}
