package it.unibo.vuzix.api;

public class RaspberryAPI {
    private static final String BASE_URL = "http://52.19.198.62/";
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

    /**
     * I want to use the raspberry
     * @param idRaspberry id of Raspberry (SmartForklift device)
     * @return url
     */
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
     * @param idRaspberry id of Raspberry (SmartForklift device)
     * @return url
     */
    public static String setPlacement(String idRaspberry){
        return getBaseResourceURL() + idRaspberry + "/" +
                ACTION +
                SETPLACEMENT;
    }

    /**
     * Raspberry show where the warehouseman has to put the product
     * POST http://52.19.198.62/smartForklift/1/placements/1/action/putItHere
     * body: {
     *     "product_code": 2314332434,
     *     "qty": 5
     * }
     * @param idRaspberry id of Raspberry (SmartForklift device)
     * @param idPlacement id of placement (label)
     * @return url
     */
    public static String setPutHere(String idRaspberry, String idPlacement){
        return getBaseResourceURL() +
                idRaspberry + "/" +
                PLACEMENTS + "/" +
                idPlacement + "/" +
                PUTITHERE;
    }

    /**
     * set the product of the placement picked
     * @param idRaspberry id of Raspberry (SmartForklift device)
     * @param idPlacement id of placement (label)
     * @return url
     */
    public static String setProductPicked(String idRaspberry, String idPlacement){
        return getBaseResourceURL() +
                idRaspberry + "/" +
                PLACEMENTS + "/" +
                idPlacement + "/" +
                PICKED;
    }

    /**
     * order completed
     * @param idRaspberry id of Raspberry (SmartForklift device)
     * @param idPlacement id of placement (label)
     * @return url
     */
    public static String setOrderPicked(String idRaspberry, String idPlacement){
        return getBaseResourceURL() +
                idRaspberry + "/" +
                PLACEMENTS + "/" +
                idPlacement + "/" +
                ORDERDONE;
    }


}