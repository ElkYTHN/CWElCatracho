package com.grupom.cwelcatracho;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grupom.cwelcatracho.Configuracion.RestApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ActivityRegistrar extends AppCompatActivity {

    Button btnSave,btnGaleria;
    FloatingActionButton btnTomarfoto;
    ImageView Foto;
    EditText txtNombre,txtApellido,txtPais,txtTelefono, txtCorreo, txtPass;

    Bitmap imagen;

    static final int RESULT_GALLERY_IMG = 200;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        Foto = (ImageView) findViewById(R.id.imageView);
        btnSave = (Button) findViewById(R.id.btnActualizar);
        btnTomarfoto = (FloatingActionButton) findViewById(R.id.fbtnTomarFoto);
        btnGaleria = (Button) findViewById(R.id.btnGaleria);
        txtNombre = (EditText) findViewById(R.id.actutxtnombre);
        txtTelefono = (EditText) findViewById(R.id.actutxtTelefono);
        txtApellido = (EditText) findViewById(R.id.actutxtapellido);
        txtPais = (EditText) findViewById(R.id.actutxtPais);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtPass = (EditText) findViewById(R.id.txtPass);

        btnTomarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();

            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriaImagenes();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validarDatos();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Debe de tomarse una foto ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void validarDatos() {
        if(txtNombre.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (txtApellido.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un apellido" ,Toast.LENGTH_LONG).show();
        }else if (txtPais.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe seleccionar un pais" ,Toast.LENGTH_LONG).show();
        }else if (txtTelefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (txtCorreo.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un correo" ,Toast.LENGTH_LONG).show();
        }else if (txtPass.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir una contrasena" ,Toast.LENGTH_LONG).show();
        }
        else {
            crearUsuario();
        }
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, RESULT_GALLERY_IMG);
    }

    //***Metodo para convertir imagen***//
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesitan permisos",Toast.LENGTH_LONG).show();
        }
        /*//permiso para GPS
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }*/
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
                imagen=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);

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


    private void crearUsuario() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();

        String fotoString = GetStringImage(imagen);


        //setear los parametros mediante put
        parametros.put("nombre", txtNombre.getText().toString());
        parametros.put("apellido", txtApellido.getText().toString());
        parametros.put("pais", txtPais.getText().toString());
        parametros.put("telefono", txtTelefono.getText().toString());
        parametros.put("foto", fotoString);
        parametros.put("correo", txtCorreo.getText().toString());
        parametros.put("clave", txtPass.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApi.EndPointCreateUsuario,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
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

        limpiar();
    }

    private void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtPais.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPass.setText("");
    }




}

