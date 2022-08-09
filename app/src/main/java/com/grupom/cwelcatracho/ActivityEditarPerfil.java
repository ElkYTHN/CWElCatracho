package com.grupom.cwelcatracho;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grupom.cwelcatracho.Configuracion.RestApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityEditarPerfil extends AppCompatActivity {

    EditText txtNombre,txtApellido,txtTelefono, txtPais;

    Spinner SpiPais;

    TextView btnActualizar;
    LinearLayout btnTomarFoto,btnSelectGaleria;
    ImageView Foto;
    String fotoString,email;
    Bitmap imagen;
    static final int RESULT_GALLERY_IMG = 200;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;


    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        txtNombre = (EditText) findViewById(R.id.actutxtnombre);
        txtApellido =  (EditText) findViewById(R.id.actutxtapellido);
        txtPais =  (EditText) findViewById(R.id.actutxtPais);
        txtTelefono =(EditText) findViewById(R.id.actutxtTelefono);
        //SpiPais =  (Spinner)  findViewById(R.id.actucmbPais);

        btnActualizar = findViewById(R.id.actubtnActualizar);
        btnTomarFoto = findViewById(R.id.actuTomarFoto);
        btnSelectGaleria = findViewById(R.id.actubtnGaleria);


        email = getIntent().getStringExtra("correo");
        String nombre =getIntent().getStringExtra("nombre");
        String apellido =getIntent().getStringExtra("apellido");
        String pais =getIntent().getStringExtra("pais");
        String telefono = getIntent().getStringExtra("telefono");
        fotoString =getIntent().getStringExtra("foto");

        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtPais.setText(pais);
        txtTelefono.setText(telefono);
        mostrarFoto(fotoString);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
                String correo = mSharedPrefs.getString("correo","");
                ActualizarDatos(correo);
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnSelectGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GaleriaImagenes();
            }
        });


    }

    public void mostrarFoto(String foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Foto.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        //obtener la iamgen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==RESULT_GALLERY_IMG)
        {

            imageUri = data.getData();
            Foto.setImageURI(imageUri);
            try {
                imagen= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Error al seleccionar imagen", Toast.LENGTH_SHORT).show();
            }
        }
        //obtener la iamgen por la camara
        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            Foto.setImageBitmap(imagen);
        }

    }


    //--------------------------------metodo actualizar imagen-------------------------------------------------
    private void ActualizarDatos(String correo) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();

        String fotoString2 = GetStringImage(imagen);
        if (fotoString2.equals("")||fotoString2.isEmpty()||fotoString2.equals(null)){
            fotoString2 = fotoString;
        }

        //setear los parametros mediante put
        parametros.put("nombre", txtNombre.getText().toString());
        parametros.put("apellido", txtApellido.getText().toString());
        parametros.put("pais", txtPais.getText().toString());
        parametros.put("telefono",txtTelefono.getText().toString());
        parametros.put("foto", fotoString2);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApi.EndPointSetUpdateUser,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent intent = new Intent(getApplicationContext(), ActivityPerfil.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(getApplicationContext(),ActivityPerfil.class);
        startActivity(intent);
    }

    //-------------------------------------------------------------------------------------------------------------------


    private String GetStringImage(Bitmap photo) {

        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 70, ba);
            byte[] imagebyte = ba.toByteArray();
            String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);

            return encode;
        }catch (Exception ex)
        {
            ex.toString();
        }
        return "";
    }

    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    //**Entrar a la carpeta de fotos del telefono**//
    private void GaleriaImagenes() {
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, RESULT_GALLERY_IMG);*/

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),RESULT_GALLERY_IMG);

    }

}