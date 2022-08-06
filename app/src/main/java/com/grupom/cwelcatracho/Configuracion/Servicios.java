package com.grupom.cwelcatracho.Configuracion;

public class Servicios {

    public int id_servicio;
    public String tipo_servicio;
    public String precio;

    public Servicios(){

    }

    public Servicios(int id_servicio, String tipo_servicio, String precio) {
        this.id_servicio = id_servicio;
        this.tipo_servicio = tipo_servicio;
        this.precio = precio;

    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(String tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
