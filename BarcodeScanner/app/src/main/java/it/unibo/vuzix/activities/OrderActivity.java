package it.unibo.vuzix.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.services.OrderService;
import it.unibo.vuzix.api.RaspberryAPI;

import static it.unibo.vuzix.model.Forklift.FORKLIFT_KEY;

public class OrderActivity extends Activity implements View.OnClickListener {
    private static final int ACTIVITY_ORDER_CODE = 2;
    private Button confirmButton;
    private Button backButton;
    private EditText orderEditText;
    private EditText placementEditText;
    private int numOrder = 0;
    private Forklift forklift;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //TODO
        Bundle bundle = getIntent().getExtras();
        forklift = (Forklift) bundle.get(FORKLIFT_KEY);

        //TODO
        //shared forklift with OrderActivity
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(FORKLIFT_KEY, forklift);
        startActivityForResult(intent, ACTIVITY_ORDER_CODE);

        confirmButton = findViewById(R.id.confermOrderButton);
        confirmButton.setEnabled(false);
        confirmButton.setOnClickListener(this);

        backButton = findViewById(R.id.backButtonOrder);
        backButton.setOnClickListener(this);

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

    private <T> void launchActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confermOrderButton) { //CONFERM
            if(setPlacementOrder());
                numOrder++;

            if (numOrder < forklift.getPlacementNumber()) {
                addNewOrderDialog();
            } else {
                startOrderService();
                Toast.makeText(OrderActivity.this, "You have reached the maximum number of orders that can be managed", Toast.LENGTH_SHORT).show();
                launchActivity(ShowLocationActivity.class);
            }
        } else if (view.getId() == R.id.backButtonOrder) { //BACK
            System.out.println("back");
            //TODO It's right?!?!?
            OrderActivity.this.finish();
            setContentView(R.layout.activity_connect);
        }
    }

    private boolean isValid(String toString) {
        return !toString.isEmpty();
    }

    private boolean setPlacementOrder() {
        String orderCode = orderEditText.getText().toString(); //TODO String direttamente?
        String placementCode = placementEditText.getText().toString();
        System.out.println("Confirm to connect to box " + orderCode + " placement " + placementCode);
        JSONObject jsonObject = new JSONObject();
        //https://stackoverflow.com/questions/48424033/android-volley-post-request-with-json-object-in-body-and-getting-response-in-str/48424181
        if (isValid(orderCode) && isValid(placementCode)) {
            //CREATE JsonObject that represents the body request
            try {
                jsonObject.put("placement_id", placementCode); //TODO string o int??
                jsonObject.put("order_id", orderCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String mRequestBody = jsonObject.toString();
            forklift.addElementMap(Integer.getInteger(orderCode), Integer.getInteger(placementCode));

            //POST localhost:5000/smartForklift/idRASPBERRRY/action/setPlacement
            //body: {
            //    "placement_id": 1,
            //    "order_id": 447499
            //}
            String url = RaspberryAPI.setPlacement("" + this.forklift.getIdRaspberry());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    response -> { /*RESPOND, ma non risponde*/},
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

                        //TODO PROVA REMOVE?!?!
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            JSONObject responseString = new JSONObject();
                            if (response != null) {
                                try {
                                    responseString.put("statusCode", response.statusCode);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };
            Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(OrderActivity.this, "Get valid box/order code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addNewOrderDialog(){
        AlertDialog.Builder ab = new AlertDialog.Builder(OrderActivity.this);
        ab.setTitle("Would you add an other order?");
        ab.setMessage("Would you add an other order?");
        ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //todo reload the same activity
                orderEditText.setText("");
                placementEditText.setText("");
                confirmButton.setEnabled(false);
            }
        });
        ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startOrderService();

                dialog.dismiss();
                OrderActivity.this.finish();
                launchActivity(ShowLocationActivity.class);
            }
        });
        ab.show();
    }

    private void startOrderService(){
        Intent intent = new Intent(this, OrderService.class);
        intent.putExtra(FORKLIFT_KEY, forklift);//TODO penso di poterlo fare
        startService(intent);
    }

}
