package com.grupom.cwelcatracho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.grupom.cwelcatracho.Configuracion.RestApi;
import com.grupom.cwelcatracho.Spinner.Spinners;
import com.grupom.cwelcatracho.databinding.ActivityMapsBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class ActivityCotizacion extends AppCompatActivity implements View.OnClickListener{

    private String UIDV;
    Button btnhora, btnfecha, btnguardar;
    EditText txtidvehiculo, txtidservicio, txthora, txtfecha, txtubicacion;

    private AsyncHttpClient http;
    private RequestQueue rq;

    //VEHICULOS
    private String[] iddevehiculo= new String[900]; //IDVEHICULO de la posicion
    private String IdVehiculoBD,vehiculo; // ID del Vehiculo tabla CREARVEHICULO

    //SPINNERS
    Spinner sp_vehiculos,sp_servicios,sp_ubicacion;
    private String ItemVehiculo, ItemServicios, ItemUbicacion;
    private ArrayList<Spinners> lista;
    ArrayAdapter<Spinners> adp;
    private ArrayAdapter adapter2;

    //UBICACION
    private int seleccionar; // Opcion spinner ubicacion
    String Latitud,Longitud;
    private String[] contenido; // Array opciones Spinner
    private boolean isFirstTime = true;
    boolean retorno;
    TextView textViewUbicacion;
    private String[] contenidoservicio;
    private int seleccionarservicio;
    int seleccionarV;

    //HORA Y FECHA
    private int dia, mes, anio, hora, minutos;


    GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizacion);

        btnguardar = (Button) findViewById(R.id.btnGuardarC);
        btnfecha = (Button) findViewById(R.id.btnFecha);
        btnhora = (Button) findViewById(R.id.btnHora);

        //txtidvehiculo = (EditText) findViewById(R.id.txtVehiculo);
        //txtidservicio = (EditText) findViewById(R.id.txtTipoServicio);
        txthora = (EditText) findViewById(R.id.txtHora);
        txtfecha = (EditText) findViewById(R.id.txtFecha);
        //txtubicacion = (EditText) findViewById(R.id.txtUbicacion);

        sp_ubicacion = (Spinner) findViewById(R.id.spUbicacion);
        sp_servicios = (Spinner) findViewById(R.id.spServicios);
        sp_vehiculos = (Spinner) findViewById(R.id.spVehiculo);


        //0BTENER FECHA Y HORA
        btnfecha.setOnClickListener(this);
        btnhora.setOnClickListener(this);

        http = new AsyncHttpClient();
        rq = Volley.newRequestQueue(getApplicationContext());

/*        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
        String UIDV = mSharedPrefs.getString("idusuario","");
        ObtenerUsuario(UIDV);*/

        //GUARDAR DATOS
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String Vehiculo = txtidvehiculo.getText().toString();
                //String Servicio = txtidservicio.getText().toString();
                //String Ubicacion = txtubicacion.getText().toString();
                //String Fecha= txtfecha.getText().toString()+" "+txthora.getText().toString()+":00";
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
                String idusuario = mSharedPrefs.getString("idusuario","");
                guardarCotizacion(idusuario);
            }
        });


        final int interval = 1500; // 1 Second
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
                String UIDV = mSharedPrefs.getString("idusuario","");
                ObtenerVehiculos(UIDV);
                //ObtenerVehiculos();     // Funcion para cargar Vehiculos en Spinner
                //ObtenerServicios();     // Funcion para cargar Servicios en Spinner
            }
        };

        handler.postAtTime(runnable, System.currentTimeMillis() + interval);
        handler.postDelayed(runnable, interval);


        // ESCOGER UBICACION EN EL SPINNER
        contenido = new String[]{"UBICACION","Centro de Servicio", "A Domicilio"};
        ArrayList<String> ubicacion = new ArrayList<>(Arrays.asList(contenido));
        adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ubicacion);
        sp_ubicacion.setAdapter(adapter2);
        sp_ubicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTime){
                    isFirstTime = true;
                }
                if (contenido[position] == "A Domicilio") {
                    seleccionar = 0;

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("opcion", seleccionar);
                    startActivity(intent);


                } else if (contenido[position] == "Centro de Servicio") {
                    seleccionar = 1;

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("opcion", seleccionar);
                    startActivity(intent);
                }
                ItemUbicacion = (String) sp_ubicacion.getAdapter().getItem(position).toString();   // El elemento seleccionado del Spinner


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // SI SERVICIO ES CAMBIO DE ACEITE
        sp_servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ItemServicios = (String) sp_servicios.getAdapter().getItem(position).toString();   // El elemento seleccionado del Spinner

                String CA = "Cambio de Aceite";

                if (ItemServicios.equals(CA)) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityCotizacion.this);
                    alerta.setMessage("Unicamente se hace en centro de servicio")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sp_ubicacion.setSelection(adapter2.getPosition("Centro de Servicio"));
                                    sp_ubicacion.setEnabled(false);
                                }
                            });
                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Aviso");
                    titulo.show();

                }
                else{
                    sp_ubicacion.setEnabled(true);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ESCOGER SERVICIO
        contenidoservicio = new String[]{
                "SERVICIO",
                "Lavado General",
                "Lavado General - Solo por fuera",
                "Cambio de Aceite",
                "Lavado de Motor",
                "Lavado Completo"};
        ArrayList<String> servicio = new ArrayList<>(Arrays.asList(contenidoservicio));
        adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, servicio);
        sp_servicios.setAdapter(adapter2);
        sp_servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstTime){
                    isFirstTime = true;
                }
                if (contenidoservicio[position] == "Lavado General") {
                    seleccionarservicio = 0;
                } else if (contenidoservicio[position] == "Lavado General - Solo por fuera") {
                    seleccionarservicio = 1;
                } else if (contenidoservicio[position] == "Cambio de Aceite") {
                    seleccionarservicio = 2;
                } else if (contenidoservicio[position] == "Lavado de Motor") {
                    seleccionarservicio = 3;
                } else if (contenidoservicio[position] == "Lavado Completo") {
                    seleccionarservicio = 4;
                }
                ItemServicios = (String) sp_servicios.getAdapter().getItem(position).toString();   // El elemento seleccionado del Spinner

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

            // OBTENER VEHICULOS DEL USUARIO ACTUAL

            public void ObtenerVehiculos(String userv) {

                http.get("https://educationsofthn.com/API/obtenerVehiculosid.php?id_usuario='"+userv+"'", new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        System.out.println("ON SUCCESS");
                        if(statusCode == 200){
                            ListaVehiculos(new String (responseBody));

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("ERROR HTTP");
                    }
                });
            }

            // OBTENER LA LISTA DE VEHICULOS DE LA BD
            private void ListaVehiculos(String URL){
                lista = new ArrayList<Spinners>();
                try {
                    JSONObject jsonRespuesta = new JSONObject(URL);
                    JSONArray jsonArreglo = jsonRespuesta.getJSONArray("vehiculo");
                    for(int i = 0; i < jsonArreglo.length(); i++){
                        Spinners b = new Spinners();
                        b.setId(jsonArreglo.getJSONObject(i).getInt("id"));
                        b.setNombre(jsonArreglo.getJSONObject(i).getString("marcamodelo"));
                        lista.add(b);
                    }

                    adp = new ArrayAdapter(ActivityCotizacion.this, android.R.layout.simple_spinner_dropdown_item, lista);
                    sp_vehiculos.setAdapter(adp);

                    sp_vehiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ItemVehiculo = (String) sp_vehiculos.getAdapter().getItem(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
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
                        iddevehiculo[i] = jsonArreglo.getJSONObject(i).getString("id");
                        vehiculo = jsonArreglo.getJSONObject(i).getString("marcamodelo");
                        System.out.println("VEHICULO: "+vehiculo);
                    }
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }



    // OBTENER SERVICIOS DE LA BD
    /*public void ObtenerServicios() {
        String URL = RestApi.EndPointObtenerServicios;    // URL de recurso PHP

        http.post(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    ListaServicios(new String (responseBody));

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    // OBTENER LA LISTA DE SERVICIOS DE LA BD EN EL SPINNER
    private void ListaServicios(String URL){
        lista = new ArrayList<Spinners>();
        try {
            JSONObject jsonRespuesta = new JSONObject(URL);
            JSONArray jsonArreglo = jsonRespuesta.getJSONArray("servicio");
            for(int i = 0; i < jsonArreglo.length(); i++){
                Spinners a = new Spinners();
                Spinners b = new Spinners();
                a.setId(jsonArreglo.getJSONObject(i).getInt("id_servicio"));
                b.setNombre(jsonArreglo.getJSONObject(i).getString("tipo_servicio"));
                lista.add(b);
            }

            adp = new ArrayAdapter(ActivityCotizacion.this, android.R.layout.simple_spinner_dropdown_item, lista);
            sp_servicios.setAdapter(adp);

            sp_servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ItemServicios = (String) sp_servicios.getAdapter().getItem(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception e1){
                e1.printStackTrace();
            }
        }
*/


        //Obtener hora y fecha
    @Override
    public void onClick(View v) {

        if (v == btnfecha) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            anio= c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtfecha.setText(dayOfMonth +"/"+ (monthOfYear + 1)+"/"+year);

                        }
                    }, anio, mes, dia);
            datePickerDialog.show();
        }
        if (v == btnhora) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            hora= c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txthora.setText(hourOfDay+":"+minute);
                        }
                    }, hora, minutos, false);
            timePickerDialog.show();
        }
    }


    private void guardarCotizacion(String idUsuario) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        String Fecha= txtfecha.getText().toString()+" "+txthora.getText().toString()+":00";

        parametros.put("id_usuario", idUsuario);
        parametros.put("id_vehiculo", ItemVehiculo);
        parametros.put("ubicacion", ItemUbicacion);
        parametros.put("fecha", Fecha);
        parametros.put("tipo_servicio", ItemServicios);
        parametros.put("estado", "Aprovado");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApi.EndPointCreateCotizacion,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // try {
                    Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();
                    createNotification();
                    //Toast.makeText(getApplicationContext(), response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                /*} catch (JSONException e) {
                    e.printStackTrace();*/
                //}
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
        txtfecha.setText("");
        txthora.setText("");

    }

    // PEDIR PERMISOS PARA LA UBICACION
    public void Permisos(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    // OBTENER UBICACION
    @SuppressLint("MissingPermission")
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);

        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }

    // PERMISO UBICACION ACTIVO
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    // OBTENER UBICACION MEDIANTE LA LONGITUD Y LATITUD
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // OBTENER LONGITUD Y LATITUD
    public class Localizacion implements LocationListener {
        ActivityCotizacion mainActivity;

        public void setMainActivity(ActivityCotizacion mainActivity){
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {

            loc.getLatitude();
            loc.getLongitude();

            Latitud =  ""+loc.getLatitude();
            Longitud =  ""+loc.getLongitude();
            this.mainActivity.setLocation(loc);

        }
    }

    private void createNotification(){
        String id="mensaje";
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setShowBadge(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder.setAutoCancel(true).setWhen(System.currentTimeMillis())
                .setContentTitle("Cotización por Servicio del Carwash").setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Su cotizacion fue aceptada con exito - Muchas Gracias por su Preferencia -.")
                .setColor(Color.BLUE)
                .setContentIntent(sendNotification())
                .setContentInfo("nuevo");
        Random random = new Random();
        int id_notification = random.nextInt(8000);

        assert notificationManager != null;
        notificationManager.notify(id_notification,builder.build());
    }

    public PendingIntent sendNotification(){
        Intent intent = new Intent(this.getApplicationContext(), ActivityHome.class);
        intent.putExtra("color", "rojo");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,intent,0);
    }

/*    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/

}