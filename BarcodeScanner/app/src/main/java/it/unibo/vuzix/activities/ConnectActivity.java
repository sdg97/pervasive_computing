package it.unibo.vuzix.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.api.OrderAPI;
import it.unibo.vuzix.api.RaspberryAPI;

import static it.unibo.vuzix.model.Forklift.FORKLIFT_KEY;

public class ConnectActivity extends Activity implements View.OnClickListener{

    private static final int ACTIVITY_CONNECT_CODE = 1;
    private Forklift forklift;

    private Button confirmButton;
    private Button backButton;
    private EditText boxCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        this.forklift = new Forklift();

        confirmButton = findViewById(R.id.button);
        confirmButton.setOnClickListener(this);
        confirmButton.setEnabled(false);

        backButton = findViewById(R.id.button4);
        backButton.setOnClickListener(this);

        boxCode = findViewById(R.id.barcodetext);
        boxCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            startUse(boxCode.getText().toString()); //I want to start to use the barcode scanned
            setJwtForklift();
            System.out.println("aftrer start");
        } else if (view.getId() == R.id.button4){
            System.out.println("back");
            this.finish();
            setContentView(R.layout.activity_main);
        }
    }

    //TODO min lenght...
    private boolean isValid(String boxBarcode) {
        return !boxBarcode.isEmpty();
    }

    private <T> void launchActivity(Class<T> clazz) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(FORKLIFT_KEY, forklift);
        startActivity(intent);
    }

    private void setJwtForklift() {
        //http://it2.life365.eu/api/auth/admin?login=wh&password=wh365
        String url = OrderAPI.getLoginURL();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    System.out.println(response);
                    try {
                        forklift.setJwt(response.getString("jwt"));
                        System.out.println(forklift.getJwt());
                        launchActivity(OrderActivity.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(ConnectActivity.this, "Can't reach jwt", Toast.LENGTH_SHORT).show();
                });
        Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void startUse(String boxBarcode) {
        System.out.println("I want to connect to raspberry " + boxBarcode);

        if (isValid(boxBarcode)) {
            //set the id of Raspberry = box
            this.forklift.setIdRaspberry(Integer.parseInt(boxBarcode));
            this.forklift.setPlacementNumber(2);
            //localhost:5000/smartForklift/1/action/startUse
            String url = RaspberryAPI.setStartUse(boxBarcode);
            StringRequest jsonObjectRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        System.out.println("Risposta " + response.toString()); //niente
                        //forklift.setPlacementNumber(response.getJSONArray("placements").length());

                    },
                    error -> {
                        System.out.println(error);
                        Toast.makeText(ConnectActivity.this, "Connection error to the box", Toast.LENGTH_SHORT).show();
                    });
            Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);
        } else {
            Toast.makeText(ConnectActivity.this, "Get valid box code", Toast.LENGTH_SHORT).show();
        }
    }
}
