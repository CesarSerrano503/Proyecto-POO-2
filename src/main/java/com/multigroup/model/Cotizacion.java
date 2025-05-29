package com.multigroup.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Cotizacion {
    private int         idCotizacion;
    private int         idCliente;
    private String      clienteNombre;
    private String      estado;
    private BigDecimal  totalHoras;
    private Timestamp   fechaInicio;
    private Timestamp   fechaFin;
    private BigDecimal  costoAsignaciones;
    private BigDecimal  costosAdicionales;
    private BigDecimal  total;
    private String      creadoPor;
    private Timestamp   fechaCreacion;
    private Timestamp   fechaActualizacion;
    private Timestamp   fechaFinalizacion;

    public int getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(int idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(BigDecimal totalHoras) {
        this.totalHoras = totalHoras;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getCostoAsignaciones() {
        return costoAsignaciones;
    }

    public void setCostoAsignaciones(BigDecimal costoAsignaciones) {
        this.costoAsignaciones = costoAsignaciones;
    }

    public BigDecimal getCostosAdicionales() {
        return costosAdicionales;
    }

    public void setCostosAdicionales(BigDecimal costosAdicionales) {
        this.costosAdicionales = costosAdicionales;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Timestamp getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Timestamp fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}
