package com.grupom.cwelcatracho.Configuracion;

public class Cotizacion {
    public int id;
    public int id_usuario;
    public String vehiculo;
    public String ubicacion;
    public String fecha;
    public String tipo_servicio;
    public String estado;

    public Cotizacion(int id, int id_usuario, String vehiculo, String ubicacion, String fecha, String tipo_servicio, String estado) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.vehiculo = vehiculo;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.tipo_servicio = tipo_servicio;
        this.estado = estado;
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

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(String tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

