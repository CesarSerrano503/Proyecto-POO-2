// src/main/java/com/multigroup/model/Asignacion.java
package com.multigroup.model;

import java.time.LocalDateTime;

public class Asignacion {
    private int idAsignacion;
    private int idCotizacion;
    private int idEmpleado;
    private String area;
    private double costoHora;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int horasEstimadas;
    private String tituloActividad;
    private String tareas;
    private double costoBase;
    private double incrementoPct;
    private double total;

    // Getters y setters
    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int id) { this.idAsignacion = id; }

    public int getIdCotizacion() { return idCotizacion; }
    public void setIdCotizacion(int id) { this.idCotizacion = id; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int id) { this.idEmpleado = id; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public double getCostoHora() { return costoHora; }
    public void setCostoHora(double c) { this.costoHora = c; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime f) { this.fechaInicio = f; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime f) { this.fechaFin = f; }

    public int getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(int h) { this.horasEstimadas = h; }

    public String getTituloActividad() { return tituloActividad; }
    public void setTituloActividad(String t) { this.tituloActividad = t; }

    public String getTareas() { return tareas; }
    public void setTareas(String t) { this.tareas = t; }

    public double getCostoBase() { return costoBase; }
    public void setCostoBase(double c) { this.costoBase = c; }

    public double getIncrementoPct() { return incrementoPct; }
    public void setIncrementoPct(double p) { this.incrementoPct = p; }

    public double getTotal() { return total; }
    public void setTotal(double t) { this.total = t; }
}
