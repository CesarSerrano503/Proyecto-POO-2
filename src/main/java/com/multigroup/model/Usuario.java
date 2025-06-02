/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 1/6/2025
 */
package com.multigroup.model;

import java.time.LocalDateTime;

/**
 * POJO que mapea la tabla `usuarios` en la base de datos.
 * Contiene atributos que corresponden a las columnas de la tabla,
 * así como getters y setters para manipularlos.
 */
public class Usuario {
    // ID del usuario (clave primaria, autoincremental)
    private int idUsuario;

    // Nombre de usuario único
    private String username;

    // Contraseña en formato hash BCrypt
    private String password;

    // Rol del usuario: "admin" o "colaborador"
    private String rol;

    // Estado: true = Activo, false = Inactivo
    private boolean estado;

    // Nombre de quien creó el registro (puede ser “sistema” o usuario administrador)
    private String creadoPor;

    // Fecha y hora en que se creó el registro
    private LocalDateTime fechaCreacion;

    // Fecha y hora de la última actualización (puede ser null si nunca se actualizó)
    private LocalDateTime fechaActualizacion;

    /** Constructor vacío requerido por frameworks y por JavaBeans */
    public Usuario() { }

    // --- Getters y setters ---

    /**
     * @return idUsuario: clave primaria del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }
    /**
     * @param idUsuario asigna el ID del usuario (utilizado tras insert o recuperación de BD).
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return username: nombre de usuario.
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username asigna el nombre de usuario (debe ser único).
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password: devuelve el hash BCrypt de la contraseña.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password asigna la contraseña en texto plano o hash.
     *                 En DAO se convertirá a hash si proviene de texto plano.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return rol: cadena que representa el rol, p. ej. "admin" o "usuario".
     */
    public String getRol() {
        return rol;
    }
    /**
     * @param rol define el rol del usuario; usado para autorizar accesos.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * @return estado: true si el usuario está activo, false si está inactivo.
     */
    public boolean isEstado() {
        return estado;
    }
    /**
     * @param estado asigna el estado del usuario; true = Activo, false = Inactivo.
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * @return creadoPor: nombre del usuario o sistema que creó este registro.
     */
    public String getCreadoPor() {
        return creadoPor;
    }
    /**
     * @param creadoPor asigna quién creó este usuario (usualmente el administrador).
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * @return fechaCreacion: fecha y hora exacta en que se insertó el registro.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @param fechaCreacion asigna la fecha de creación (normalmente LocalDateTime.now()).
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return fechaActualizacion: fecha y hora de la última modificación (puede ser null).
     */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    /**
     * @param fechaActualizacion asigna la fecha de actualización tras un UPDATE.
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
