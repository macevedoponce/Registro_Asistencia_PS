package com.acevedo.registroasistencia.Clases;

public class Participante {
    int id;
    String dni;
    String nombre;

    String estado;

    public Participante() {
    }

    public Participante(int id, String dni, String nombre, String estado) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
