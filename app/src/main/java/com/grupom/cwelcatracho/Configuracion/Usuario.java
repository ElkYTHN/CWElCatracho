package com.grupom.cwelcatracho.Configuracion;

public class Usuario {

    public int id;
    public String nombre;
    public String apellido;
    public String pais;
    public String telefono;
    public String foto;
    public String correo;
    public String clave;

    public Usuario(int id, String nombre, String apellido, String pais, String telefono, String foto, String correo, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
        this.telefono = telefono;
        this.foto = foto;
        this.correo = correo;
        this.clave = clave;
    }

    public Usuario(int id, String nombre, String apellido, String pais, String telefono, String foto, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
        this.telefono = telefono;
        this.foto = foto;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}

