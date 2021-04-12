package it.unibo.vuzix.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.unibo.vuzix.activities.ShowLocationActivity;
import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.model.Order;
import it.unibo.vuzix.model.Product;
import it.unibo.vuzix.utils.OrderAPI;

import static it.unibo.vuzix.model.Forklift.FORKLIFT_KEY;

//https://developer.android.com/guide/components/services
public class OrderService extends Service {

    public static final int ACTIVITY_SHOWLOCATION_CODE = 4;
    public static final String ORDER_KEY = "ORDER";

    private Forklift forklift;
    private List<Integer> orderList;
    private List<Product> productList;

    @Override
    public void onCreate() {
        super.onCreate();
        forklift = new Forklift();
        orderList = new ArrayList<>();
        productList = new ArrayList<>();
    }

    /**
     * "onStartCommand" is the main method of a service of type "started".
     *  Called when the above service is started by a component via the "startService" method.
     *  The service body is executed in this method.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        forklift = (Forklift) bundle.get(FORKLIFT_KEY);
        orderList = forklift.getAllOrders();

        //per ogni ordine invio la richiesta, quando arriva la risposta mantengo tutto nella lista dei prodotti
        populateProductList();
        Comparator<Product> comparebyWerehouseplace = (Product p1, Product p2) -> p1.getWarehousePlace().compareTo(p2.getWarehousePlace());
        Collections.sort(productList, comparebyWerehouseplace); //TODO check ordinato

        Order order = new Order();
        order.setProducts(productList);

        Intent intentLocation = new Intent(this, ShowLocationActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(ORDER_KEY, order);
        intentLocation.putExtras(b);
        //intentLocation.putExtra(PRODUCTS_KEY, order);


        //startActivityForResult(intentLocation, ACTIVITY_SHOWLOCATION_CODE);
        /*
         * Return value of the "onStartCommand" method.
         * The most used constants in this context are:
         * - START_STICKY: The service is restarted if it is terminated. The intent passed to the "onStartCommand" method is null.
         * - START_NOT_STICKY: The service is not restarted. Used for services that are periodically started in each case.
         * - START_REDELIVER_INTENT: similar to START_STICKY but the original intent is "re-delivered" to the "onStartCommand" method.
         */
        return START_REDELIVER_INTENT;
    }

    private void populateProductList(){
        //https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181

        //http://it2.life365.eu/api/order/447499?jwt=...
        String url;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //TODO possibili errori
            orderList.forEach(i -> {
                String urls = OrderAPI.getOrderURL(i.toString(), forklift.getJwt());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        urls,
                       null,
                        response -> { productList.addAll(Product.from(response));},
                        error -> {
                            //TODO ERROR
                        });
                Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
            });
        }
    }

    /**
     * The "onBind" method must be implemented as the MyService class extends Service.
     * In the case of a "started" service, this method is not used, so it returns null to the caller.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Method for receiving messages from the activity handler.
     * I add the request to the service request queue.
     * The request is of the correct type.

    public void ActivitySendMessage(Message msg, ActivityAPI cls){
        this.activityTesting = cls;
        Bundle bundle = msg.getData();
        this.request = bundle.getParcelable(KEY);


        this.userRequestQueue.add(request);
        executeRequest(getNextRequest());
    }*/

    /**
     * Method that implements thread waiting.
     *
     * @param second, second to waiting
     */
    private static void waitSecond(int second){
        try{
            Thread.sleep(second*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
