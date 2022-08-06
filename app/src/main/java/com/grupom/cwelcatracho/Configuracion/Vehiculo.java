package com.grupom.cwelcatracho.Configuracion;

public class Vehiculo {

    public int id;
    public int id_usuario;
    public String marca;
    public String modelo;
    public String tipo_aceite;
    public String anio;

    public Vehiculo(int id, int id_usuario, String marca, String modelo, String tipo_aceite, String anio) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo_aceite = tipo_aceite;
        this.anio = anio;
    }

    public Vehiculo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo_aceite() {
        return tipo_aceite;
    }

    public void setTipo_aceite(String tipo_aceite) {
        this.tipo_aceite = tipo_aceite;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
}

