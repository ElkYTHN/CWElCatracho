package com.grupom.cwelcatracho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.grupom.cwelcatracho.Configuracion.Cotizacion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ActivityHistorialLavados extends AppCompatActivity {

    private AsyncHttpClient http;
    private int idUser;          // ID del Usuario en MySQL
    private String URLQuotation;      // URL de Lista Cotizacion Vehiculo

    RecyclerView recycler;

    ArrayList<Cotizacion> historials;
    ArrayList<Cotizacion> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_lavados);
        http = new AsyncHttpClient();
        items = new ArrayList<>();

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);

        // Usar un administrador para LinearLayout
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        final int interval = 2000; // 1 Second
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
                String UIDV = mSharedPrefs.getString("idusuario","");
                ObtenerCotizacion(UIDV);
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis() + interval);
        handler.postDelayed(runnable, interval);

    }


    //-------- FINAL DE EVENTO DE BUSCAR ID DE USUARIO --------///

    //-------- INICIO DE EVENTO DE RECYCLERVIEW CON MYSQL --------///

    // Obtener Cotizaciones de Usuario con MySQL
    public void ObtenerCotizacion(String userv) {
        http.post("https://educationsofthn.com/API/obtenercotizacionid.php?id_usuario='"+userv+"'", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    ListaCotizacion(new String (responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    // OBTENER LA LISTA DE COTIZACIONES DE LA BD
    private void ListaCotizacion(String URL){
        historials = new ArrayList<Cotizacion>();
        try {
            JSONObject jsonRespuesta = new JSONObject(URL);
            JSONArray jsonArreglo = jsonRespuesta.getJSONArray("cotizacion");
            for(int i=0; i<jsonArreglo.length(); i++){
                Cotizacion h = new Cotizacion();
                h.setVehiculo(jsonArreglo.getJSONObject(i).getString("id_vehiculo"));
                h.setTipo_servicio(jsonArreglo.getJSONObject(i).getString("tipo_servicio"));
                h.setUbicacion(jsonArreglo.getJSONObject(i).getString("ubicacion"));
                h.setFecha(jsonArreglo.getJSONObject(i).getString("fecha"));
                h.setEstado(jsonArreglo.getJSONObject(i).getString("estado"));
                historials.add(h);
            }

            CotizacionList();

            // Crear un nuevo adaptador
            ActivityHistorial adapter = new ActivityHistorial(items);
            recycler.setAdapter(adapter);
        }
        catch (Exception e1){
            e1.printStackTrace();
        }
    }

    // RELLENAR EL LISTVIEW
    private void CotizacionList() {

        for (int i = 0;  i < historials.size(); i++){
            items.add(new Cotizacion(
                    historials.get(i).getVehiculo(),
                    historials.get(i).getTipo_servicio(),
                    historials.get(i).getUbicacion(),
                    historials.get(i).getFecha(),
                    historials.get(i).getEstado()));
        }
    }

}