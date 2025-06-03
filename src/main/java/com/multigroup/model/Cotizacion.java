/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 28/5/2025
 *
 * Modelo de datos para la entidad Cotización.
 * Representa una oferta de servicio o proyecto para un cliente, con detalles de horas y costos.
 */
package com.multigroup.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Cotizacion {
    /** ID único de la cotización (clave primaria). */
    private int idCotizacion;
    /** ID del cliente asociado a esta cotización (clave foránea). */
    private int idCliente;
    /** Nombre del cliente (obtenido mediante JOIN en DAO). */
    private String clienteNombre;
    /** Estado actual de la cotización (por ejemplo: "En proceso", "Finalizada", "Inactivo"). */
    private String estado;
    /** Total de horas presupuestadas para la cotización. */
    private BigDecimal totalHoras;
    /** Fecha y hora de inicio prevista para el proyecto. */
    private Timestamp fechaInicio;
    /** Fecha y hora de fin prevista para el proyecto. */
    private Timestamp fechaFin;
    /** Costo total de las asignaciones asociadas (suma de costos de actividades). */
    private BigDecimal costoAsignaciones;
    /** Costos adicionales (por ejemplo: materiales, gastos administrativos). */
    private BigDecimal costosAdicionales;
    /** Costo total final de la cotización (costoAsignaciones + costosAdicionales). */
    private BigDecimal total;
    /** Usuario que creó la cotización. */
    private String creadoPor;
    /** Fecha en que se creó el registro de cotización. */
    private Timestamp fechaCreacion;
    /** Fecha de la última actualización del registro. */
    private Timestamp fechaActualizacion;
    /** Fecha en que se finalizó o inactivó la cotización. */
    private Timestamp fechaFinalizacion;

    /**
     * @return ID de la cotización.
     */
    public int getIdCotizacion() {
        return idCotizacion;
    }
    /**
     * @param idCotizacion Asigna el ID de la cotización.
     */
    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    /**
     * @return ID del cliente asociado.
     */
    public int getIdCliente() {
        return idCliente;
    }
    /**
     * @param idCliente Asigna el ID del cliente a la cotización.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return Nombre del cliente (para mostrar en interfaces).
     */
    public String getClienteNombre() {
        return clienteNombre;
    }
    /**
     * @param clienteNombre Asigna el nombre del cliente (obtenido en DAO).
     */
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    /**
     * @return Estado actual de la cotización.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna el estado de la cotización (por ejemplo: "Finalizada").
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return Total de horas presupuestadas.
     */
    public BigDecimal getTotalHoras() {
        return totalHoras;
    }
    /**
     * @param totalHoras Asigna el total de horas.
     */
    public void setTotalHoras(BigDecimal totalHoras) {
        this.totalHoras = totalHoras;
    }

    /**
     * @return Fecha y hora de inicio prevista.
     */
    public Timestamp getFechaInicio() {
        return fechaInicio;
    }
    /**
     * @param fechaInicio Asigna la fecha y hora de inicio.
     */
    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return Fecha y hora de fin prevista.
     */
    public Timestamp getFechaFin() {
        return fechaFin;
    }
    /**
     * @param fechaFin Asigna la fecha y hora de fin.
     */
    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return Costo total de las asignaciones.
     */
    public BigDecimal getCostoAsignaciones() {
        return costoAsignaciones;
    }
    /**
     * @param costoAsignaciones Asigna el costo de asignaciones.
     */
    public void setCostoAsignaciones(BigDecimal costoAsignaciones) {
        this.costoAsignaciones = costoAsignaciones;
    }

    /**
     * @return Costos adicionales asociados.
     */
    public BigDecimal getCostosAdicionales() {
        return costosAdicionales;
    }
    /**
     * @param costosAdicionales Asigna los costos adicionales.
     */
    public void setCostosAdicionales(BigDecimal costosAdicionales) {
        this.costosAdicionales = costosAdicionales;
    }

    /**
     * @return Costo total final de la cotización.
     */
    public BigDecimal getTotal() {
        return total;
    }
    /**
     * @param total Asigna el costo total final de la cotización.
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return Usuario que creó la cotización.
     */
    public String getCreadoPor() {
        return creadoPor;
    }
    /**
     * @param creadoPor Asigna el nombre de usuario que creó la cotización.
     */
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * @return Fecha de creación del registro.
     */
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @param fechaCreacion Asigna la fecha de creación proporcionada por la BD.
     */
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return Fecha de la última actualización del registro.
     */
    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }
    /**
     * @param fechaActualizacion Asigna la fecha de actualización proporcionada por la BD.
     */
    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * @return Fecha en que se finalizó o inactivó la cotización.
     */
    public Timestamp getFechaFinalizacion() {
        return fechaFinalizacion;
    }
    /**
     * @param fechaFinalizacion Asigna la fecha de finalización proporcionada por la BD.
     */
    public void setFechaFinalizacion(Timestamp fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}
