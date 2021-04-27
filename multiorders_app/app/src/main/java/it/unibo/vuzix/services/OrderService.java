package it.unibo.vuzix.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import it.unibo.vuzix.activities.OrderActivity;
import it.unibo.vuzix.activities.ShowLocationActivity;
import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.model.Order;
import it.unibo.vuzix.model.Product;
import it.unibo.vuzix.api.OrderAPI;

import static it.unibo.vuzix.model.Forklift.FORKLIFT_KEY;

//https://developer.android.com/guide/components/services
public class OrderService extends Service {

    public static final String ORDER_KEY = "ORDER";

    private Forklift forklift;
    private List<Integer> orderList;
    private List<Product> productList;

    @Override
    public void onCreate() {
        super.onCreate();
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
        if(bundle != null) {
            forklift = (Forklift) bundle.get(FORKLIFT_KEY);
            assert forklift != null;
            orderList = forklift.getAllOrders();
            System.out.println("Orders " + orderList);

            //for each order I send the request, when the answer arrives I keep everything in the list of products
            populateProductList();
        }

        return START_STICKY;
    }

    private void populateProductList(){
        System.out.println("ORder list "+ orderList);
        List<Integer> list = new ArrayList<>();

        for(int i = 0; i<orderList.size(); i++){

            String urls = OrderAPI.getOrderURL(orderList.get(i).toString(), forklift.getJwt());
            System.out.println(urls);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    urls,
                   null,
                    response -> { productList.addAll(Objects.requireNonNull(Product.from(response)));
                        System.out.println("ORDER SERVICE product list " + productList);
                        list.add(0);

                        if(list.size() == orderList.size())     {
                            Comparator<Product> comparebyWerehouseplace = (Product p1, Product p2) -> p1.getWarehousePlace().compareTo(p2.getWarehousePlace());
                            Collections.sort(productList, comparebyWerehouseplace);
                            Order order = new Order();
                            order.setProducts(productList);
                            System.out.println("ORDER SERVICE Order " + order);

                            Intent intent = new Intent(this, ShowLocationActivity.class);
                            Bundle b = new Bundle();
                            b.putParcelable(FORKLIFT_KEY, this.forklift);
                            b.putParcelable(ORDER_KEY, order);
                            intent.putExtras(b);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    },
                    error -> {
                        System.out.println("Not able to reach product list for order");
                    });
            Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
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
}