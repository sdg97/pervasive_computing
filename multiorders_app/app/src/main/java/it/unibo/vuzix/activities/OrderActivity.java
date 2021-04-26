package it.unibo.vuzix.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.services.OrderService;
import it.unibo.vuzix.api.RaspberryAPI;

import static it.unibo.vuzix.model.Forklift.FORKLIFT_KEY;

public class OrderActivity extends Activity implements View.OnClickListener {
    private Button confirmButton;
    private EditText orderEditText;
    private EditText placementEditText;
    private int numOrder = 0;
    private Forklift forklift;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Bundle bundle = getIntent().getExtras();
        forklift = (Forklift) bundle.get(FORKLIFT_KEY);
        System.out.println("Forklift " + forklift);

        confirmButton = findViewById(R.id.confermOrderButton);
        confirmButton.setEnabled(false);
        confirmButton.setOnClickListener(this);

        orderEditText = findViewById(R.id.barcodeOrder);
        orderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    confirmButton.setEnabled(true);
                else
                    confirmButton.setEnabled(false);
            }
        });

        placementEditText = findViewById(R.id.barcodePlacement);
        placementEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    confirmButton.setEnabled(true);
                else
                    confirmButton.setEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confermOrderButton) { //CONFERM
            setPlacementOrder();
            numOrder++;

            System.out.println("N.Placement " + forklift.getPlacementNumber() );
            if (numOrder < forklift.getPlacementNumber()) {
                addNewOrderDialog();
            } else {
                startOrderService();
                Toast.makeText(OrderActivity.this, "You have reached the maximum number of orders that can be managed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPlacementOrder() {
        Integer orderCode = Integer.valueOf(orderEditText.getText().toString());
        Integer placementCode = Integer.valueOf(placementEditText.getText().toString());
        System.out.println("Confirm to connect order " + orderCode + " to placement " + placementCode);
        JSONObject jsonObject = new JSONObject();
        forklift.addElementMap(orderCode, placementCode);

        //CREATE JsonObject that represents the body request
        try {
            jsonObject.put("placement_id", placementCode);
            jsonObject.put("order_id", orderCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonObject.toString();

        //POST localhost:5000/smartForklift/idRASPBERRRY/action/setPlacement
        //body: {
        //    "placement_id": 1,
        //    "order_id": 447499
        //}
        String url = RaspberryAPI.setPlacement(String.valueOf(this.forklift.getIdRaspberry()));
        System.out.println("ORDER ACTIVITY " + url);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {},
                error -> {
                    Toast.makeText(OrderActivity.this, "Error to set placement-order", Toast.LENGTH_SHORT).show();
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

    private void addNewOrderDialog(){
        AlertDialog.Builder ab = new AlertDialog.Builder(OrderActivity.this);
        ab.setTitle("Would you add an other order?");
        ab.setMessage("Would you add an other order?");
        ab.setPositiveButton("yes", (dialog, which) -> {
            dialog.dismiss();
            orderEditText.setText("");
            placementEditText.setText("");
            confirmButton.setEnabled(false);
            this.placementEditText.requestFocus();
        });
        ab.setNegativeButton("no", (dialog, which) -> {
            startOrderService();
            Toast.makeText(OrderActivity.this, "Loading . . .", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            OrderActivity.this.finish();
        });
        ab.show();
    }

    private void startOrderService(){
        Intent intent = new Intent(this, OrderService.class);
        intent.putExtra(FORKLIFT_KEY, forklift);
        startService(intent);
    }

}
