package it.unibo.vuzix.api;

public class OrderAPI {

    private static final String BASE_URL = "http://it2.life365.eu/";
    private static final String API = "api/";
    private static final String AUTH = "auth/";
    private static final String LOGIN = "warehouse?login=wh&password=wh365";
    private static final String ORDER = "order/";


    private static String getBaseResourceURL () {
        return BASE_URL + API;
    }

    /**
     * http://it2.life365.eu/api/auth/warehouse?login=wh&password=wh365
     * @return url
     */
    public static String getLoginURL(){
        return getBaseResourceURL() +
                AUTH +
                LOGIN;
    }

    /**
     * url to get informations of a order given jwt
     * @param idOrder id of order
     * @param jwt jwt to id warehouse worker
     * @return url
     */
    public static String getOrderURL(String idOrder, String jwt){
        return getBaseResourceURL() +
                ORDER + idOrder + "?jwt=" + jwt;
    }

}