/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 27/5/2025
 *
 * Modelo de datos de Empleado según la tabla empleados.
 * Representa la información de un colaborador dentro de la organización.
 */
package com.multigroup.model;

import java.util.Date;

public class Empleado {
    /** ID único del empleado (clave primaria). */
    private int idEmpleado;
    /** Nombre completo del empleado. */
    private String nombre;
    /** Documento de identidad o NIT del empleado. */
    private String documento;
    /** Tipo de persona (por ejemplo: "Natural" o "Jurídica"). */
    private String tipoPersona;
    /** Tipo de contratación (por ejemplo: "Tiempo completo", "Contrato", etc.). */
    private String tipoContratacion;
    /** Número de teléfono del empleado. */
    private String telefono;
    /** Dirección de correo electrónico del empleado. */
    private String correo;
    /** Dirección física del empleado. */
    private String direccion;
    /** Estado del empleado (por ejemplo: "Activo" o "Inactivo"). */
    private String estado;
    /** Usuario que creó el registro del empleado. */
    private String creadoPor;
    /** Fecha en que se creó el registro (se asigna automáticamente en BD). */
    private Date fechaCreacion;
    /** Fecha de la última actualización del registro. */
    private Date fechaActualizacion;
    /** Fecha en que se inactivó el empleado (si corresponde). */
    private Date fechaInactivacion;

    /** Constructor vacío para instanciar un Empleado sin datos iniciales. */
    public Empleado() { }

    /**
     * Constructor completo para instanciar un Empleado con todos sus campos.
     *
     * @param idEmpleado         ID del empleado.
     * @param nombre             Nombre completo.
     * @param documento          Documento de identidad.
     * @param tipoPersona        Tipo de persona ("Natural", "Jurídica").
     * @param tipoContratacion   Tipo de contratación.
     * @param telefono           Teléfono de contacto.
     * @param correo             Correo electrónico.
     * @param direccion          Dirección física.
     * @param estado             Estado ("Activo", "Inactivo").
     * @param creadoPor          Usuario que creó el registro.
     * @param fechaCreacion      Fecha de creación del registro.
     * @param fechaActualizacion Fecha de última actualización.
     * @param fechaInactivacion  Fecha de inactivación (si aplica).
     */
    public Empleado(int idEmpleado, String nombre, String documento,
                    String tipoPersona, String tipoContratacion,
                    String telefono, String correo, String direccion,
                    String estado, String creadoPor,
                    Date fechaCreacion, Date fechaActualizacion, Date fechaInactivacion) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.documento = documento;
        this.tipoPersona = tipoPersona;
        this.tipoContratacion = tipoContratacion;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaInactivacion = fechaInactivacion;
    }

    /**
     * @return ID del empleado.
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }
    /**
     * @param idEmpleado Asigna el ID al empleado.
     */
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * @return Nombre completo del empleado.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre Asigna el nombre del empleado.
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
     * @param documento Asigna el documento del empleado.
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
     * @return Tipo de contratación ("Tiempo completo", "Contrato", etc.).
     */
    public String getTipoContratacion() {
        return tipoContratacion;
    }
    /**
     * @param tipoContratacion Asigna el tipo de contratación.
     */
    public void setTipoContratacion(String tipoContratacion) {
        this.tipoContratacion = tipoContratacion;
    }

    /**
     * @return Teléfono de contacto del empleado.
     */
    public String getTelefono() {
        return telefono;
    }
    /**
     * @param telefono Asigna el teléfono del empleado.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return Correo electrónico del empleado.
     */
    public String getCorreo() {
        return correo;
    }
    /**
     * @param correo Asigna el correo del empleado.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return Dirección física del empleado.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @param direccion Asigna la dirección del empleado.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return Estado del empleado ("Activo" o "Inactivo").
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna el estado del empleado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return Usuario que creó el registro de empleado.
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
     * @return Fecha de creación del registro en la BD.
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @param fechaCreacion Asigna la fecha de creación.
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return Fecha de la última actualización del registro.
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
     * @return Fecha en que se inactivó el empleado, si aplica.
     */
    public Date getFechaInactivacion() {
        return fechaInactivacion;
    }
    /**
     * @param fechaInactivacion Asigna la fecha de inactivación.
     */
    public void setFechaInactivacion(Date fechaInactivacion) {
        this.fechaInactivacion = fechaInactivacion;
    }
}
