package it.unibo.vuzix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.utils.OrderAPI;
import it.unibo.vuzix.utils.RaspberryAPI;

public class ConnectActivity extends AppCompatActivity {

    public static final String FORKLIFT_KEY = "FORKLIFT";
    private static final int ACTIVITY_CONNECT_CODE = 1;
    private Forklift forklift;
    private Button confirmButton;
    private Button backButton;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        this.forklift = new Forklift();

        confirmButton = findViewById(R.id.button);
        confirmButton.setOnClickListener(v -> {
            startUse();
            setJwtForklift();

            //shared forklift with OrderActivity
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(FORKLIFT_KEY, forklift);
            startActivityForResult(intent, ACTIVITY_CONNECT_CODE);

            launchActivity(OrderActivity.class);
        });
        confirmButton.setEnabled(false);

        backButton = findViewById(R.id.button4);
        backButton.setOnClickListener(v -> {
            System.out.println("back");
            //TODO It's right?!?!?
            ConnectActivity.this.finish();
            setContentView(R.layout.activity_main);
        });

        editText = findViewById(R.id.barcodetext);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    confirmButton.setEnabled(true);
                else
                    confirmButton.setEnabled(false);
            }
        });
    }

    private boolean isValid(String boxBarcode) {
        return !boxBarcode.isEmpty();
    }

    private <T> void launchActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }

    private void setJwtForklift() {
        //http://it2.life365.eu/api/auth/admin?login=wh&password=wh365
        String url = OrderAPI.getLoginURL();
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
                    Toast.makeText(ConnectActivity.this, "Can't reach jwt", Toast.LENGTH_SHORT).show();
                });
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void startUse() {

        String boxBarcode = editText.getText().toString(); //1
        System.out.println("Confirm to connect to box " + boxBarcode);

        if (isValid(boxBarcode)) {
            this.forklift.setIdRaspberry(Integer.getInteger(boxBarcode));
            //localhost:5000/smartForklift/1/action/startUse
            String url = RaspberryAPI.setStartUse(boxBarcode);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        //{placements: [0, 1]}
                        //todo
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Risposta " + response.toString());
                            try {
                                //placements dovrebbe essere un array di Int
                                forklift.setPlacementNumber(response.getJSONArray("placements").length());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //TODO ERROR
                            Toast.makeText(ConnectActivity.this, "Connection error to the box", Toast.LENGTH_SHORT).show();
                        }
                    });
            Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(ConnectActivity.this, "Get valid box code", Toast.LENGTH_SHORT).show();
        }
    }
}
