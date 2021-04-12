package it.unibo.vuzix.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.model.Order;
import it.unibo.vuzix.utils.OrderAPI;
import it.unibo.vuzix.utils.RaspberryAPI;

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

    private void checkOrderPicked(){

    }

    private void setProductPicker(){
        order.getProducts().get(counter).getProductInfo().setPicked(true);

        //POST localhost:5000/smartForklift/1/placements/1/picked
        String url = "";//RaspberryAPI.setOrderPicked();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                response -> {
                    System.out.println(response);
                    try {
                        //System.out.println(response.getString("jwt"));
                        forklift.setJwt(response.getString("jwt"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    //TODO ERROR
                    Toast.makeText(ShowLocationActivity.this, "Error to set picked product", Toast.LENGTH_SHORT).show();
                });
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void updateViewProduct(int index){
        locationElement.setText(order.getProducts().get(index).getWarehousePlace());
        quantity.setText(order.getProducts().get(index).getProductInfo().getQuantity());
        idProduct.setText(order.getProducts().get(index).getId());
    }
}
