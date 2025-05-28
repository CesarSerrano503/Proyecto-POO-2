
package com.multigroup.model;

import java.util.Date;

/**
 * Modelo de datos de Empleado según la tabla empleados.
 */
public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String documento;
    private String tipoPersona;
    private String tipoContratacion;
    private String telefono;
    private String correo;
    private String direccion;
    private String estado;
    private String creadoPor;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Date fechaInactivacion;

    public Empleado() { }

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

    // Getters y Setters
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTipoPersona() { return tipoPersona; }
    public void setTipoPersona(String tipoPersona) { this.tipoPersona = tipoPersona; }

    public String getTipoContratacion() { return tipoContratacion; }
    public void setTipoContratacion(String tipoContratacion) { this.tipoContratacion = tipoContratacion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Date fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public Date getFechaInactivacion() { return fechaInactivacion; }
    public void setFechaInactivacion(Date fechaInactivacion) { this.fechaInactivacion = fechaInactivacion; }
}