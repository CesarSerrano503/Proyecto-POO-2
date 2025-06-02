/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 1/6/2025
 */
package com.multigroup.dao;

import com.multigroup.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Usuario.
 * Proporciona métodos CRUD (crear, leer, actualizar, eliminar) en la tabla `usuarios`.
 */
public class UsuarioDAO {

    /**
     * Obtiene una nueva conexión a la base de datos multigroup.
     *
     *
     * @return Connection a la base de datos.
     * @throws SQLException si hay un problema al conectarse.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/multigroup",
                "root",
                ""
        );
    }

    /**
     * Recupera todos los usuarios de la tabla `usuarios`.
     *
     * @return Lista de objetos Usuario.
     * @throws SQLException si ocurre un error en la consulta.
     */
    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            // Itera sobre cada fila y la mapea a un objeto Usuario
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    /**
     * Busca un usuario por su nombre de usuario (username). Se utiliza en el login.
     *
     * @param username el nombre de usuario a buscar.
     * @return Usuario si se encuentra, o null si no existe.
     * @throws SQLException si ocurre un error en la consulta.
     */
    public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                // Si hay resultado, devolvemos el primer registro mapeado
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Busca un usuario por su ID (id_usuario). Se utiliza para edición.
     *
     * @param id el ID del usuario.
     * @return Usuario si existe, o null si no encuentra.
     * @throws SQLException si ocurre un error en la consulta.
     */
    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Crea un nuevo usuario en la tabla `usuarios`. Siempre hashea la contraseña con BCrypt.
     *
     * @param u objeto Usuario con los datos a insertar. Debe tener username, password en texto plano,
     *          rol, estado, creadoPor y fechaCreacion.
     * @throws SQLException si ocurre un error al insertar.
     */
    public void create(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios "
                + "(username, password, rol, estado, creado_por, fecha_creacion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1) Genera hash BCrypt de la contraseña suministrada
            String hashed = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
            p.setString(1, u.getUsername());
            p.setString(2, hashed);
            p.setString(3, u.getRol());
            // Guarda “Activo” o “Inactivo” según el booleano u.isEstado()
            p.setString(4, u.isEstado() ? "Activo" : "Inactivo");
            p.setString(5, u.getCreadoPor());
            p.setTimestamp(6, Timestamp.valueOf(u.getFechaCreacion()));
            p.executeUpdate();

            // 2) Recuperar la clave generada (ID autoincremental)
            try (ResultSet keys = p.getGeneratedKeys()) {
                if (keys.next()) {
                    u.setIdUsuario(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Actualiza un usuario existente. Si u.getPassword() no es null ni vacío, incluye el campo
     * `password` en el UPDATE (y genera el hash). Si es null o "", omite actualizar la columna `password`.
     *
     * @param u objeto Usuario con los datos actualizados. Debe tener idUsuario y los campos a cambiar.
     * @throws SQLException si ocurre un error al actualizar.
     */
    public void update(Usuario u) throws SQLException {
        // Caso 1: se proporcionó contraseña nueva (no es null ni vacía)
        if (u.getPassword() != null && !u.getPassword().isEmpty()) {
            String sql = "UPDATE usuarios "
                    + "   SET username = ?, "
                    + "       password = ?, "
                    + "       rol = ?, "
                    + "       estado = ?, "
                    + "       fecha_actualizacion = ? "
                    + " WHERE id_usuario = ?";
            try (Connection c = getConnection();
                 PreparedStatement p = c.prepareStatement(sql)) {

                // Generar hash BCrypt de la contraseña proporcionada
                String hashed = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
                p.setString(1, u.getUsername());
                p.setString(2, hashed);
                p.setString(3, u.getRol());
                p.setString(4, u.isEstado() ? "Activo" : "Inactivo");
                p.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                p.setInt(6, u.getIdUsuario());
                p.executeUpdate();
            }
        } else {
            // Caso 2: no se proporcionó contraseña o viene vacía → actualizamos todo menos password
            String sql = "UPDATE usuarios "
                    + "   SET username = ?, "
                    + "       rol = ?, "
                    + "       estado = ?, "
                    + "       fecha_actualizacion = ? "
                    + " WHERE id_usuario = ?";
            try (Connection c = getConnection();
                 PreparedStatement p = c.prepareStatement(sql)) {

                p.setString(1, u.getUsername());
                p.setString(2, u.getRol());
                p.setString(3, u.isEstado() ? "Activo" : "Inactivo");
                p.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                p.setInt(5, u.getIdUsuario());
                p.executeUpdate();
            }
        }
    }

    /**
     * Elimina un usuario de la tabla mediante su ID.
     *
     * @param id el ID del usuario a eliminar.
     * @throws SQLException si ocurre un error al eliminar.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }
    }

    /**
     * Mapea la fila actual del ResultSet a un objeto Usuario.
     * Lee campos, convierte “estado” texto a boolean, y convierte Timestamps a LocalDateTime.
     *
     * @param rs ResultSet posicionado en una fila de la consulta.
     * @return objeto Usuario con datos de esa fila.
     * @throws SQLException si hay error al leer campos.
     */
    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));            // Columna id_usuario
        u.setUsername(rs.getString("username"));             // Columna username
        u.setPassword(rs.getString("password"));             // Aquí puede venir hash BCrypt o, en migración, texto plano
        u.setRol(rs.getString("rol"));                       // Columna rol

        // La columna estado almacena “Activo” o “Inactivo” (texto), convertimos a boolean
        String estadoStr = rs.getString("estado");
        u.setEstado("Activo".equalsIgnoreCase(estadoStr));

        u.setCreadoPor(rs.getString("creado_por"));          // Columna creado_por

        // Fecha de creación (Timestamp → LocalDateTime)
        Timestamp tc = rs.getTimestamp("fecha_creacion");
        if (tc != null) {
            u.setFechaCreacion(tc.toLocalDateTime());
        }
        // Fecha de actualización (Timestamp → LocalDateTime)
        Timestamp ta = rs.getTimestamp("fecha_actualizacion");
        if (ta != null) {
            u.setFechaActualizacion(ta.toLocalDateTime());
        }
        return u;
    }
}
