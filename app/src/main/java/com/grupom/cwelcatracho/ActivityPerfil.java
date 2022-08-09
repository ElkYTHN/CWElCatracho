package com.grupom.cwelcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupom.cwelcatracho.Configuracion.RestApi;
import com.grupom.cwelcatracho.Configuracion.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityPerfil extends AppCompatActivity {

    EditText txtnombre, txtpais, txtapellido, txtcorreo, txttelefono;
    Button btnEditar;
    ImageView perfilfoto;
    Usuario usuario;
    ArrayList<String> arrayUsuario;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtnombre = (EditText) findViewById(R.id.fhtxtnombre);
        txtapellido = (EditText) findViewById(R.id.fhtxtapellido);
        txtcorreo = (EditText) findViewById(R.id.fhcorreo);
        txtpais = (EditText) findViewById(R.id.fhtxtPais);
        txttelefono= (EditText) findViewById(R.id.fgtelefono);
        perfilfoto = (ImageView) findViewById(R.id.fhImage);
        btnEditar = (Button) findViewById(R.id.perfilbtnActualizar);
        //btnAtras = (TextView) findViewById(R.id.perbtnAtras);

        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
        String correo = mSharedPrefs.getString("correo","");
        email = correo;

        listarUsuarios(email);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editar();
                finish();
            }
        });


    }

    private void listarUsuarios(String correo) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("correo", correo);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApi.EndPointBuscarCorreo,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray usuarioArray = response.getJSONArray( "usuario");

                    arrayUsuario = new ArrayList<>();
                    for (int i=0; i<usuarioArray.length(); i++)
                    {
                        JSONObject RowUsuario = usuarioArray.getJSONObject(i);
                        usuario = new Usuario(  RowUsuario.getInt("id_usuario"),
                                RowUsuario.getString("nombre"),
                                RowUsuario.getString("apellido"),
                                RowUsuario.getString("pais"),
                                RowUsuario.getString("telefono"),
                                RowUsuario.getString("foto"),
                                RowUsuario.getString("correo")
                        );

                        txtnombre.setText(usuario.getNombre());
                        txtapellido.setText(usuario.getApellido());
                        txtpais.setText(usuario.getPais());
                        txttelefono.setText(usuario.getTelefono());
                        txtcorreo.setText(usuario.getCorreo());

                        mostrarFoto(usuario.getFoto().toString());

                    }


                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "mensaje"+ex, Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

/*    private void editar() {
        Intent intent = new Intent(getApplicationContext(),Activity_Actualizar_Perfil.class);
        intent.putExtra("email", email);
        intent.putExtra("nombres", usuario.getNombres()+"");
        intent.putExtra("apellidos", usuario.getApellidos()+"");
        intent.putExtra("fechanac", usuario.getFechaNac()+"");
        intent.putExtra("codigo_pais", usuario.getCodigo_pais()+"");
        intent.putExtra("telefono", usuario.getTelefono()+"");
        intent.putExtra("peso", usuario.getPeso()+"");
        intent.putExtra("altura", usuario.getAltura()+"");
        intent.putExtra("foto", usuario.getFoto()+"").toString();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
        finish();
    }*/



    public void mostrarFoto(String foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            perfilfoto.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }



}