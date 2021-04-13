package it.unibo.vuzix.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

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
    private EditText productCodeScanned;

    private Order order = new Order();
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
        forklift = (Forklift) bundle.get(FORKLIFT_KEY);
        order = (Order) bundle.get(ORDER_KEY);

        System.out.println("SLA   Order " + order);
        System.out.println("SLA   Forklift " + forklift);

        this.locationElement = findViewById(R.id.textElemLocation);
        this.quantity = findViewById(R.id.quantity);
        this.idProduct = findViewById(R.id.productCode);

        this.productCodeScanned = findViewById(R.id.barcodeProduct);

        updateViewProduct(0);
    }

    @Override
    public void onClick(View view) {
        if(checkProductId()) {
            if (view.getId() == R.id.okButton) {
                if (!productCodeScanned.getText().toString().isEmpty())
                    putProductHere();
                else
                    Toast.makeText(ShowLocationActivity.this, "scan the product code", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == R.id.pickedButton) {
                setProductPicker();
                checkOrderPicked();
                counter++;
                if (counter < order.getProducts().size())
                    updateViewProduct(counter);
            }
        }

    }

    private boolean checkProductId(){
        return (productCodeScanned.getText().toString()).equals(String.valueOf(order.getProducts().get(counter).getId()));
    }

    private void putProductHere(){
        //https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181

        int idOrder = order.getProducts().get(counter).getProductInfo().getIdOrder();
        Integer placement = forklift.getOrderPlacementMap().get(idOrder);
        if(placement == 0){
            Toast.makeText(ShowLocationActivity.this, "idOrder not found", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        //CREATE JsonObject that represents the body request
        try {
            jsonObject.put("product_code", productCodeScanned.getText().toString());//order.getProducts().get(counter).getId());
            jsonObject.put("qty", order.getProducts().get(counter).getProductInfo().getQuantity());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonObject.toString();

        //POST localhost:5000/smartForklift/1/placements/1/putItHere
        // body: {
        //    "product_code": 2314332434,
        //            "qty": 5
        //}
        String url = RaspberryAPI.setPutHere(String.valueOf(forklift.getIdRaspberry()), String.valueOf(placement));
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> { },
                error -> {
                    System.out.println(url);
                    Toast.makeText(ShowLocationActivity.this, "Error to put a Product", Toast.LENGTH_SHORT).show();
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
        };
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void checkOrderPicked() {
        int idOrder = order.getProducts().get(counter).getProductInfo().getIdOrder();
        System.out.println("CHECK ORDER PICKED " + counter + "  su  " + order.getProducts().size());
        if((order.getProducts().size()-1) == counter || findProductOfOrder(idOrder, order.getProducts()).isEmpty()){
            System.out.println("CHECK ORDER PICKED");
            Integer placement = forklift.getOrderPlacementMap().get(idOrder);
            if(placement == 0){
                Toast.makeText(ShowLocationActivity.this, "idOrder not found", Toast.LENGTH_SHORT).show();
                return;
            }
            //localhost:5000/smartForklift/1/placements/1/orderDone
            String url = RaspberryAPI.setOrderPicked(String.valueOf(forklift.getIdRaspberry()), String.valueOf(placement));
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> { System.out.println("PICKED!!!!!!!" + response);
                    if((order.getProducts().size()-1) >= counter){
                        Toast.makeText(ShowLocationActivity.this, "Orders COMPLETED!!", Toast.LENGTH_SHORT).show();
                        this.finish();
                        //   startActivity(new Intent(this, MainActivity.class));
                    }},
                    error -> {
                        Toast.makeText(ShowLocationActivity.this, "Error to set picked product", Toast.LENGTH_SHORT).show();
                    });
            Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
        if ((order.getProducts().size()-1) >= counter ){
            Toast.makeText(ShowLocationActivity.this, "Orders COMPLETED!!", Toast.LENGTH_SHORT).show();
            this.finish();
            startActivity(new Intent(this, MainActivity.class));
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
        Integer placement = forklift.getOrderPlacementMap().get(idOrder);
            if(placement == 0){
                Toast.makeText(ShowLocationActivity.this, "idOrder not found", Toast.LENGTH_SHORT).show();
                return;
            }

        JSONObject jsonObject = new JSONObject();
        //https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181
        //CREATE JsonObject that represents the body request
        //{
        //    "placement_id": 2,
        //    "order_id": 447499
        //}
        try {
            jsonObject.put("placement_id", placement);
            jsonObject.put("order_id", order.getProducts().get(counter).getProductInfo().getIdOrder());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonObject.toString();
        //POST localhost:5000/smartForklift/1/placements/1/picked
        String url = RaspberryAPI.setProductPicked(String.valueOf(forklift.getIdRaspberry()), String.valueOf(placement));
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> { },
                error -> {
                    System.out.println(url);
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
            };
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void updateViewProduct(int index){
/*        System.out.println("0. "+order.getProducts());
        System.out.println("0. "+order.getProducts().get(index));
        System.out.println("0. " + (order.getProducts().get(index)).getWarehousePlace());
        */
        locationElement.setText((order.getProducts().get(index)).getWarehousePlace());
        quantity.setText(""+((order.getProducts().get(index)).getProductInfo()).getQuantity());
        idProduct.setText(""+(order.getProducts().get(index)).getId());
    }

}
