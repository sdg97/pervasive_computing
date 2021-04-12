package it.unibo.vuzix.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.model.Order;
import it.unibo.vuzix.model.Product;
import it.unibo.vuzix.api.RaspberryAPI;

import static it.unibo.vuzix.model.Forklift.FORKLIFT_KEY;
import static it.unibo.vuzix.services.OrderService.ORDER_KEY;

public class ShowLocationActivity extends Activity implements View.OnClickListener {

    private Button okButton;
    private Button pickedButton;
    private TextView locationElement;
    private TextView quantity;
    private TextView idProduct;

    private Order order;
    private int counter = 0;
    private Forklift forklift;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlocation);

        this.pickedButton = findViewById(R.id.pickedButton);
        this.pickedButton.setOnClickListener(this);

        this.okButton = findViewById(R.id.okButton);
        this.okButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(ORDER_KEY);
        forklift = (Forklift) bundle.get(FORKLIFT_KEY);

        this.locationElement = findViewById(R.id.textElemLocation);
        this.quantity = findViewById(R.id.quantity);
        this.idProduct = findViewById(R.id.productCode);

        updateViewProduct(0);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.okButton){



        } else if(view.getId() == R.id.pickedButton){
            setProductPicker();
            checkOrderPicked();
            counter++;
            updateViewProduct(counter);
        }
    }

    private void putProductHere(){
        JSONObject jsonObject = new JSONObject();
        //https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181
        //CREATE JsonObject that represents the body request
        try {
            jsonObject.put("product_code", order.getProducts().get(counter).getId());
            jsonObject.put("qty", order.getProducts().get(counter).getProductInfo().getQuantity());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonObject.toString();

        int idOrder = order.getProducts().get(counter).getProductInfo().getIdOrder();
        Integer placement = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //TODO check possibili errori
            placement = forklift.getOrderPlacementMap().getOrDefault(idOrder, 0);
            if(placement == 0){
                Toast.makeText(ShowLocationActivity.this, "idOrder not found", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //POST localhost:5000/smartForklift/1/placements/1/putItHere
        // body: {
        //    "product_code": 2314332434,
        //            "qty": 5
        //}
        String url = RaspberryAPI.setPutHere(String.valueOf(forklift.getIdRaspberry()), String.valueOf(placement));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                response -> { /*RESPOND, ma non risponde*/},
                error -> {
                    //TODO ERROR
                    Toast.makeText(ShowLocationActivity.this, "Error to picked a Product", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            //TODO https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181
        };
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void checkOrderPicked() {
        int idOrder = order.getProducts().get(counter).getProductInfo().getIdOrder();
        if(findProductOfOrder(idOrder, order.getProducts()).isEmpty()){
            //Order picked
            Integer placement = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //TODO check possibili errori
                placement = forklift.getOrderPlacementMap().getOrDefault(idOrder, 0);
                if(placement == 0){
                    Toast.makeText(ShowLocationActivity.this, "idOrder not found", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //localhost:5000/smartForklift/1/placements/1/orderDone
            String url = RaspberryAPI.setOrderPicked(String.valueOf(forklift.getIdRaspberry()), String.valueOf(placement));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    response -> { System.out.println(response); },
                    error -> {
                        //TODO ERROR
                        Toast.makeText(ShowLocationActivity.this, "Error to set picked product", Toast.LENGTH_SHORT).show();
                    });
            Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private List<Integer> findProductOfOrder(Integer orderId, List<Product> list){
        final List<Integer> indexList = new ArrayList<>();
        for (int i = counter; i < list.size(); i++) {
            if (orderId.equals(list.get(i).getProductInfo().getIdOrder())) {
                indexList.add(i);
            }
        }
        return indexList;
    }

    private void setProductPicker(){
        order.getProducts().get(counter).getProductInfo().setPicked(true);

        int idOrder = order.getProducts().get(counter).getProductInfo().getIdOrder();
        Integer placement = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //TODO check possibili errori
            placement = forklift.getOrderPlacementMap().getOrDefault(idOrder, 0);
            if(placement == 0){
                Toast.makeText(ShowLocationActivity.this, "idOrder not found", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        JSONObject jsonObject = new JSONObject();
        //https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181
        //CREATE JsonObject that represents the body request
        try {
            jsonObject.put("product_code", order.getProducts().get(counter).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonObject.toString();

        //POST localhost:5000/smartForklift/1/placements/1/picked
        String url = RaspberryAPI.setProductPicked(String.valueOf(forklift.getIdRaspberry()), String.valueOf(placement));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                response -> { /*RESPOND, ma non risponde*/},
                error -> {
                    //TODO ERROR
                    Toast.makeText(ShowLocationActivity.this, "Error to picked a Product", Toast.LENGTH_SHORT).show();
                }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                //TODO https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181
            };
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void updateViewProduct(int index){
        locationElement.setText(order.getProducts().get(index).getWarehousePlace());
        quantity.setText(order.getProducts().get(index).getProductInfo().getQuantity());
        idProduct.setText(order.getProducts().get(index).getId());
    }
}
