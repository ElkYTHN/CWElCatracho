package com.grupom.cwelcatracho;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.grupom.cwelcatracho.Configuracion.Cotizacion;
import com.grupom.cwelcatracho.Configuracion.Vehiculo;
import com.grupom.cwelcatracho.R;

import android.os.Bundle;

import java.util.ArrayList;

public class ActivityHistorial extends RecyclerView.Adapter<ActivityHistorial.HistorialViewHolder> {
    ArrayList<Cotizacion> items;

    public ActivityHistorial(ArrayList<Cotizacion> items) {
        this.items = items;
    }

    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_historial, null, false);
        return new HistorialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistorialViewHolder viewHolder, int i) {
        viewHolder.TVvehiculo.setText(items.get(i).getVehiculo());
        viewHolder.TVservicio.setText(items.get(i).getTipo_servicio());
        viewHolder.TVubicacion.setText(items.get(i).getUbicacion());
        viewHolder.TVfecha.setText("Fecha Emisión: "+items.get(i).getFecha());
        viewHolder.TVestado.setText(items.get(i).getEstado());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView TVvehiculo, TVservicio, TVubicacion, TVfecha, TVestado;

        public HistorialViewHolder(View v) {
            super(v);
            TVvehiculo = (TextView) v.findViewById(R.id.txtvehiculo);
            TVservicio = (TextView) v.findViewById(R.id.txtservicio);
            TVubicacion = (TextView) v.findViewById(R.id.txtTipoUbicacion);
            TVfecha = (TextView) v.findViewById(R.id.txtFechaEmision);
            TVestado = (TextView) v.findViewById(R.id.txtEstado);
        }
    }
}
