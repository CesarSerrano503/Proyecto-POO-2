/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * Modelo de datos para la entidad Asignación.
 * Representa una tarea asignada a un empleado dentro de una cotización.
 */
package com.multigroup.model;

import java.time.LocalDateTime;

public class Asignacion {
    /** ID único de la asignación (clave primaria). */
    private int idAsignacion;
    /** ID de la cotización a la que pertenece esta asignación (clave foránea). */
    private int idCotizacion;
    /** ID del empleado asignado a esta asignación (clave foránea). */
    private int idEmpleado;
    /** Área o departamento al que corresponde la asignación. */
    private String area;
    /** Costo por hora pactado para esta asignación. */
    private double costoHora;
    /** Fecha y hora de inicio de la asignación. */
    private LocalDateTime fechaInicio;
    /** Fecha y hora de fin de la asignación. */
    private LocalDateTime fechaFin;
    /** Horas estimadas que tomará completar la asignación. */
    private int horasEstimadas;
    /** Título o nombre breve de la actividad asignada. */
    private String tituloActividad;
    /** Descripción de las tareas a realizar en esta asignación. */
    private String tareas;
    /** Costo base calculado: costoHora * horasEstimadas. */
    private double costoBase;
    /** Porcentaje de incremento aplicado sobre el costo base. */
    private double incrementoPct;
    /** Costo total final de la asignación (costoBase con incremento aplicado). */
    private double total;

    // Getters y setters

    /**
     * @return ID de la asignación.
     */
    public int getIdAsignacion() {
        return idAsignacion;
    }
    /**
     * @param id ID a asignar a la propiedad idAsignacion.
     */
    public void setIdAsignacion(int id) {
        this.idAsignacion = id;
    }

    /**
     * @return ID de la cotización asociada.
     */
    public int getIdCotizacion() {
        return idCotizacion;
    }
    /**
     * @param id ID de la cotización a asignar.
     */
    public void setIdCotizacion(int id) {
        this.idCotizacion = id;
    }

    /**
     * @return ID del empleado asignado.
     */
    public int getIdEmpleado() {
        return idEmpleado;
    }
    /**
     * @param id ID del empleado a asignar.
     */
    public void setIdEmpleado(int id) {
        this.idEmpleado = id;
    }

    /**
     * @return Área o departamento de la asignación.
     */
    public String getArea() {
        return area;
    }
    /**
     * @param area Nombre del área o departamento.
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return Costo por hora pactado.
     */
    public double getCostoHora() {
        return costoHora;
    }
    /**
     * @param c Valor del costo por hora.
     */
    public void setCostoHora(double c) {
        this.costoHora = c;
    }

    /**
     * @return Fecha y hora de inicio de la asignación.
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    /**
     * @param f Fecha y hora de inicio a asignar.
     */
    public void setFechaInicio(LocalDateTime f) {
        this.fechaInicio = f;
    }

    /**
     * @return Fecha y hora de fin de la asignación.
     */
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    /**
     * @param f Fecha y hora de fin a asignar.
     */
    public void setFechaFin(LocalDateTime f) {
        this.fechaFin = f;
    }

    /**
     * @return Horas estimadas para completar la asignación.
     */
    public int getHorasEstimadas() {
        return horasEstimadas;
    }
    /**
     * @param h Número de horas estimadas.
     */
    public void setHorasEstimadas(int h) {
        this.horasEstimadas = h;
    }

    /**
     * @return Título o nombre de la actividad.
     */
    public String getTituloActividad() {
        return tituloActividad;
    }
    /**
     * @param t Título de la actividad a asignar.
     */
    public void setTituloActividad(String t) {
        this.tituloActividad = t;
    }

    /**
     * @return Descripción detallada de las tareas.
     */
    public String getTareas() {
        return tareas;
    }
    /**
     * @param t Descripción de las tareas a asignar.
     */
    public void setTareas(String t) {
        this.tareas = t;
    }

    /**
     * @return Costo base calculado (costoHora * horasEstimadas).
     */
    public double getCostoBase() {
        return costoBase;
    }
    /**
     * @param c Valor del costo base a asignar.
     */
    public void setCostoBase(double c) {
        this.costoBase = c;
    }

    /**
     * @return Porcentaje de incremento aplicado sobre el costo base.
     */
    public double getIncrementoPct() {
        return incrementoPct;
    }
    /**
     * @param p Porcentaje de incremento a asignar.
     */
    public void setIncrementoPct(double p) {
        this.incrementoPct = p;
    }

    /**
     * @return Costo total final de la asignación (con incremento).
     */
    public double getTotal() {
        return total;
    }
    /**
     * @param t Valor del costo total a asignar.
     */
    public void setTotal(double t) {
        this.total = t;
    }
}
