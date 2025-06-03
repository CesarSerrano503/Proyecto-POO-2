/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 *
 * Modelo de datos de Subtarea según la tabla subtareas.
 * Representa una tarea detallada vinculada a una asignación.
 */
package com.multigroup.model;

public class Subtarea {
    /** ID único de la subtarea (clave primaria). */
    private int idSubtarea;
    /** ID de la asignación padre a la que pertenece esta subtarea (clave foránea). */
    private int idAsignacion;
    /** Título breve que describe la subtarea. */
    private String tituloSubtarea;
    /** Descripción detallada de la subtarea. */
    private String descripcionSubtarea;

    /**
     * @return ID de la subtarea.
     */
    public int getIdSubtarea() {
        return idSubtarea;
    }
    /**
     * @param idSubtarea Asigna el ID de la subtarea.
     */
    public void setIdSubtarea(int idSubtarea) {
        this.idSubtarea = idSubtarea;
    }

    /**
     * @return ID de la asignación padre.
     */
    public int getIdAsignacion() {
        return idAsignacion;
    }
    /**
     * @param idAsignacion Asigna el ID de la asignación padre.
     */
    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    /**
     * @return Título de la subtarea.
     */
    public String getTituloSubtarea() {
        return tituloSubtarea;
    }
    /**
     * @param tituloSubtarea Asigna el título de la subtarea.
     */
    public void setTituloSubtarea(String tituloSubtarea) {
        this.tituloSubtarea = tituloSubtarea;
    }

    /**
     * @return Descripción de la subtarea.
     */
    public String getDescripcionSubtarea() {
        return descripcionSubtarea;
    }
    /**
     * @param descripcionSubtarea Asigna la descripción de la subtarea.
     */
    public void setDescripcionSubtarea(String descripcionSubtarea) {
        this.descripcionSubtarea = descripcionSubtarea;
    }
}
