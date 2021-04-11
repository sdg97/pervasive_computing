package it.unibo.vuzix.utils;

public class RaspberryAPI {
    private static final String BASE_URL = "localhost:5000/";
    private static final String FORKLIFT = "smartForklift/";
    private static final String ACTION = "action/";
    private static final String STARTUSE = "startUse";
    private static final String SETPLACEMENT = "setPlacement";
    private static final String PLACEMENTS = "placements";
    private static final String PUTITHERE = "putItHere";
    private static final String PICKED = "picked";
    private static final String ORDERDONE = "orderDone";


    private static String getBaseResourceURL () {
        return BASE_URL + FORKLIFT;
    }

    public static String setStartUse(String idRaspberry){
        return getBaseResourceURL() + idRaspberry + "/" +
                ACTION +
                STARTUSE;
    }

    /**
     * POST localhost:5000/smartForklift/raspberry/action/setPlacement
     * body :{
     *     "placement_id": 1,
     *     "order_id": 447499
     * }
     * @param idRaspberry
     * @return
     */
    public static String setPlacement(String idRaspberry){
        return getBaseResourceURL() + idRaspberry + "/" +
                ACTION +
                SETPLACEMENT;
    }

    /**
     * Per dire "Raspberry fai vedere dove il magazziniere deve mettere il prodotto'
     * POST localhost:5000/smartForklift/1/placements/1/putItHere
     * body: {
     *     "product_code": 2314332434,
     *     "qty": 5
     * }
     * @param idRaspberry
     * @param idPlacement
     * @return
     */
    public static String getProductQuantity(String idRaspberry, String idPlacement){
        return getBaseResourceURL() +
                idRaspberry + "/" +
                PLACEMENTS +
                idPlacement + "/" +
                PUTITHERE;
    }

    public static String setProductPicked(String idRaspberry, String idPlacement){
        return getBaseResourceURL() +
                idRaspberry + "/" +
                PLACEMENTS +
                idPlacement + "/" +
                PICKED;
    }

    public static String setOrderPicked(String idRaspberry, String idPlacement){
        return getBaseResourceURL() +
                idRaspberry + "/" +
                PLACEMENTS +
                idPlacement + "/" +
                ORDERDONE;
    }


}
