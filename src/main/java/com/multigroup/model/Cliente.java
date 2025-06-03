/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * Modelo de datos para el módulo de Clientes, con todos los campos según especificación.
 */
package com.multigroup.model;

import java.util.Date;

public class Cliente {
    /** ID único del cliente (clave primaria). */
    private int idCliente;
    /** Nombre completo del cliente. */
    private String nombre;
    /** Documento de identidad o NIT del cliente. */
    private String documento;
    /** Tipo de persona (por ejemplo: "Natural" o "Jurídica"). */
    private String tipoPersona;
    /** Número de teléfono del cliente. */
    private String telefono;
    /** Dirección de correo electrónico del cliente. */
    private String correo;
    /** Dirección física del cliente. */
    private String direccion;
    /** Estado del cliente (por ejemplo: "Activo" o "Inactivo"). */
    private String estado;
    /** Usuario que creó el registro del cliente. */
    private String creadoPor;
    /** Fecha en que se creó el registro (se asigna automáticamente en BD). */
    private Date fechaCreacion;
    /** Fecha de la última actualización del registro. */
    private Date fechaActualizacion;
    /** Fecha en que se inactivó el cliente (si corresponde). */
    private Date fechaInactivacion;

    /** Constructor vacío para instanciar un Cliente sin datos iniciales. */
    public Cliente() {}

    // Getters y Setters

    /**
     * @return ID del cliente.
     */
    public int getIdCliente() {
        return idCliente;
    }
    /**
     * @param idCliente Asigna el ID al cliente.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return Nombre completo del cliente.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre Asigna el nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return Documento de identidad o NIT.
     */
    public String getDocumento() {
        return documento;
    }
    /**
     * @param documento Asigna el documento del cliente.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return Tipo de persona ("Natural", "Jurídica", etc.).
     */
    public String getTipoPersona() {
        return tipoPersona;
    }
    /**
     * @param tipoPersona Asigna el tipo de persona.
     */
    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    /**
     * @return Número de teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }
    /**
     * @param telefono Asigna el teléfono del cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return Correo electrónico del cliente.
     */
    public String getCorreo() {
        return correo;
    }
    /**
     * @param correo Asigna el correo electrónico del cliente.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return Dirección física del cliente.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @param direccion Asigna la dirección del cliente.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return Estado del cliente ("Activo" o "Inactivo").
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna el estado del cliente.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return Usuario que creó el registro del cliente.
     */
    public String getCreadoPor() {
        return creadoPor;
    }
    /**
     * @param creadoPor Asigna el nombre del usuario creador.
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * @return Fecha de creación del registro.
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @param fechaCreacion Asigna la fecha de creación (generalmente desde BD).
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return Fecha de la última actualización.
     */
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }
    /**
     * @param fechaActualizacion Asigna la fecha de actualización.
     */
    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * @return Fecha de inactivación del cliente, si aplica.
     */
    public Date getFechaInactivacion() {
        return fechaInactivacion;
    }
    /**
     * @param fechaInactivacion Asigna la fecha en que se inactivó el cliente.
     */
    public void setFechaInactivacion(Date fechaInactivacion) {
        this.fechaInactivacion = fechaInactivacion;
    }
}
