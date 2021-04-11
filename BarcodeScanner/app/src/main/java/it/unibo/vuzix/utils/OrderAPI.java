package it.unibo.vuzix.utils;

public class OrderAPI {
    private static final String BASE_URL = "https://it2.life365.eu/";
    private static final String API = "api/";
    private static final String AUTH = "auth/";
    private static final String LOGIN = "admin?login=wh&password=wh365/";
    private static final String ORDER = "order/";


    private static String getBaseResourceURL () {
        return BASE_URL + API;
    }

    public static String getLoginURL(){
        return getBaseResourceURL() +
                AUTH +
                LOGIN;
    }

    public static String getOrderURL(String idOrder, String jwt){
        return getBaseResourceURL() +
                ORDER + "/" + idOrder + "?jwt=" + jwt;
    }

}