package com.grupom.cwelcatracho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupom.cwelcatracho.Configuracion.RestApi;
import com.grupom.cwelcatracho.Configuracion.Vehiculo;
import com.grupom.cwelcatracho.Spinner.Spinners;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ActivityVerVehiculo extends AppCompatActivity {

    private AsyncHttpClient http;


    private String iduser,id_usuario;          // ID del Usuario en MySQL
    private String URLVehicle;      // URL de Spinner Vehiculo
    private int idUser;
    private String[] iddevehiculo= new String[900]; //IDVEHICULO de la posicion
    private ArrayList<Spinners> lista;
    private String IdVehiculoBD; // Parametro String
    private int id_vehiculo; // Parametro entero
    private Boolean SelectedRow = false;
    private RequestQueue rq;

    ArrayAdapter<Spinners> adp;
    ArrayList ArrayLista;

    ListView Lista;
    Button btnEliminar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_vehiculo);

        Lista = (ListView) findViewById(R.id.lista);
       // btnEliminar = (Button) findViewById(R.id.btneliminar);
        http = new AsyncHttpClient();


        rq = Volley.newRequestQueue(getApplicationContext());


        final int interval = 1500; // 1 Second
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
                String UIDV = mSharedPrefs.getString("idusuario","");
                ObtenerVehiculos(UIDV);
                Lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        SelectedRow = true;
                        IdVehiculoBD = iddevehiculo[position];
                    }
                });
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis() + interval);
        handler.postDelayed(runnable, interval);

    }


    // BUSCAR UID DEL USUARIO EN BD
   /* private void SearchUID(String URL) {
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        idUser = jsonObject.getInt("id_users");
                        iduser = String.valueOf(idUser);
                        URLVehicle = "https://sitiosweb2021.000webhostapp.com/Carwash/consultarVehiculo.php?iduser="+iduser;
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Falló la conexion", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }*/

    // OBTENER VEHICULOS DEL USUARIO ACTUAL
    public void ObtenerVehiculos(String userv) {

        http.post("https://educationsofthn.com/API/obtenerVehiculosid.php?id_usuario='"+userv+"'", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    ListaVehiculos(new String (responseBody));
                    ListarIDVehiculos(new String (responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    // OBTENER LA LISTA DE VEHICULOS DE LA BD
    private void ListaVehiculos(String URL){
        lista = new ArrayList<Spinners>();
        try {
            JSONObject jsonRespuesta = new JSONObject(URL);
            JSONArray jsonArreglo = jsonRespuesta.getJSONArray("vehiculo");
            for(int i=0; i<jsonArreglo.length(); i++){
                Spinners m = new Spinners();
                m.setNombre(jsonArreglo.getJSONObject(i).getString("marcamodelo"));
                lista.add(m);
            }

            adp = new ArrayAdapter(ActivityVerVehiculo.this, android.R.layout.simple_list_item_single_choice, lista);
            Lista.setAdapter(adp);
        }
        catch (Exception e1){
            e1.printStackTrace();
        }
    }

    // OBTENER EL ID DE LOS VEHICULOS EN UN ARREGLO PARA LA POSICION
    private void ListarIDVehiculos(String URL){
        try {

            JSONArray jsonArreglo = new JSONArray(URL);
            for(int i=0; i<jsonArreglo.length(); i++){
                iddevehiculo[i] = jsonArreglo.getJSONObject(i).getString("idvehi");
            }
        }
        catch (Exception e1){
            e1.printStackTrace();
        }
    }



}