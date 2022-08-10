package com.grupom.cwelcatracho.Configuracion;

public class RestApi {
    private static final String ipaddress = "educationsofthn.com";
    public static final String StringHttp = "https://";

    //URL USUARIOS

    private static final String GetLogin = "/API/validarLogin.php";
    private static final String getBuscarCorreo = "/API/listasingleusuario.php";

    private static final String setUpdate = "/API/actualizarusuario.php";

    //
    private static final String CreateUsuario = "/API/crearusuario.php";
    private static final String CreateVehiculo = "/API/crearvehiculo.php";
    private static final String ObtenerVehiculo = "/API/listaidvehiculo.php";
    private static final String CreateCotizacion = "/API/crearcotizacion.php";
    private static final String ObtenerServicio = "/API/obtenerlistaServicios.php";
    private static final String ObtenerVehiculos = "/API/obtenerlistaVehiculos.php";

    //METODOS USUARIOS

    public static final String EndPointCreateCotizacion = StringHttp + ipaddress + CreateCotizacion;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
    public static final String EndPointCreateVehiculo = StringHttp + ipaddress + CreateVehiculo;
    public static final String EndPointObtenerVehiculo = StringHttp + ipaddress + ObtenerVehiculo;
    public static final String EndPointObtenerServicios = StringHttp + ipaddress + ObtenerVehiculos;
    public static final String EndPointSetUpdateUser= StringHttp + ipaddress + setUpdate;

    public static final String EndPointValidarLogin = StringHttp + ipaddress + GetLogin;
    public static final String EndPointBuscarCorreo = StringHttp + ipaddress + getBuscarCorreo;




    public static String EndPointBuscarUsuario;

    public static  String correo = "";
    public static  String id_usuario = "";

    String prueba = "https://educationsofthn.com/API/listasingleusuariopais.php";

}
