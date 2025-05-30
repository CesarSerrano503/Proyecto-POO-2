/**
 * Código hecho por: Cesar Antonio Serrano Gutiérrez
 * Fecha de creación: 29/5/2025
 */
package com.multigroup.model;

public class Subtarea {
    private int idSubtarea;
    private int idAsignacion;
    private String tituloSubtarea;
    private String descripcionSubtarea;

    public int getIdSubtarea() {
        return idSubtarea;
    }
    public void setIdSubtarea(int idSubtarea) {
        this.idSubtarea = idSubtarea;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }
    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getTituloSubtarea() {
        return tituloSubtarea;
    }
    public void setTituloSubtarea(String tituloSubtarea) {
        this.tituloSubtarea = tituloSubtarea;
    }

    public String getDescripcionSubtarea() {
        return descripcionSubtarea;
    }
    public void setDescripcionSubtarea(String descripcionSubtarea) {
        this.descripcionSubtarea = descripcionSubtarea;
    }
}