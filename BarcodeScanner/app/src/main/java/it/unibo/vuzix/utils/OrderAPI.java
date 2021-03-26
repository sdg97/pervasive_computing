package it.unibo.vuzix.utils;

public class OrderAPI {
    private static final String BASE_URL = "https://it2.life365.eu/";
    private static final String API = "api/";
    private static final String AUTH = "auth/";
    private static final String LOGIN = "wharehouse?login=wh&password=wh365/";
    private static final String ORDER = "order/";


    private static String getBaseResourceURL () {
        return BASE_URL + API + AUTH
                + LOGIN
                + ORDER;
    }
}