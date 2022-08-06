package com.grupom.cwelcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupom.cwelcatracho.Configuracion.RestApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityVehiculo extends AppCompatActivity {

    Button btnSaveV;
    EditText txtmarca, txtmodelo, txtaceite, txtanio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        btnSaveV = (Button) findViewById(R.id.btnGuardarV);
        txtmarca = (EditText) findViewById(R.id.txtMarca);
        txtmodelo = (EditText) findViewById(R.id.txtModelo);
        txtaceite = (EditText) findViewById(R.id.txtAceite);
        txtanio = (EditText) findViewById(R.id.txtAnio);

        btnSaveV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Marcav = txtmarca.getText().toString();
                String ModeloV = txtmodelo.getText().toString();
                String AceiteV = txtaceite.getText().toString();
                String AnioV = txtanio.getText().toString();
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
                String idusuario = mSharedPrefs.getString("idusuario","");
                guardarUsuario(idusuario,Marcav,ModeloV,AceiteV,AnioV);
            }
        });
    }


/*  private void validarDatos(){
        if (txtmarca.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if(txtmodelo.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un apellido", Toast.LENGTH_LONG).show();
        }else if(txtaceite.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Selecione una fecha de nacimiento", Toast.LENGTH_LONG).show();
        }else if(txtanio.getText().toString().equals(""))  {
            Toast.makeText(getApplicationContext(), "Debe selecionar un pais", Toast.LENGTH_LONG).show();
        }else{
            guardarUsuario();
        }
    }*/


    private void guardarUsuario(String idUsuario, String Marca, String Modelo, String Aceite, String Anio) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();


        parametros.put("id_usuario", idUsuario);
        parametros.put("marca", Marca);
        parametros.put("modelo", Modelo);
        parametros.put("aceite", Aceite);
        parametros.put("anio", Anio);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApi.EndPointCreateVehiculo,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        limpiar();
    }

    private void limpiar(){
        txtmarca.setText("");
        txtmodelo.setText("");
        txtaceite.setText("");
        txtanio.setText("");

    }

}