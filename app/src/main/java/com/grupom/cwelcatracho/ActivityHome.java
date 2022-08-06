package com.grupom.cwelcatracho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.grupom.cwelcatracho.Configuracion.RestApi;

public class ActivityHome extends AppCompatActivity {

    Button btnCerrarSesion, btnAgregarVehiculos, btnVerVehiculo, btnCotizacion, btnHistorialLabados, btnHistorialAceite, btnPerfilUsuario;

    public static final String home_correo = RestApi.correo;
    public static final String home_id_usuario = RestApi.id_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCerrarSesion = (Button) findViewById(R.id.tabCerrarSesion);
        btnAgregarVehiculos = (Button) findViewById(R.id.btnAgregarVehiculo);
        btnVerVehiculo = (Button) findViewById(R.id.btnVehiculos);
        btnCotizacion = (Button) findViewById(R.id.btnCotizacion);

        btnHistorialLabados = (Button) findViewById(R.id.btnLavados);
        btnHistorialAceite = (Button) findViewById(R.id.btnAceite);
        btnPerfilUsuario = (Button) findViewById(R.id.btnPerfil);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPrefs = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putString("usuario","");
                editor.putString("password","");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
                finish();

            }
        });

        btnAgregarVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityVehiculo.class);
                startActivity(intent);
            }
        });

        btnVerVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityVerVehiculo.class);
                startActivity(intent);
            }
        });

        btnCotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityCotizacion.class);
                startActivity(intent);
            }
        });

        btnHistorialLabados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityHistorial.class);
                startActivity(intent);
            }
        });

        btnHistorialAceite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityHistorialAceite.class);
                startActivity(intent);
            }
        });

        btnPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityPerfil.class);
                startActivity(intent);
            }
        });

    }
}