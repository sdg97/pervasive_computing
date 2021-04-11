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

import org.json.JSONObject;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.model.Forklift;
import it.unibo.vuzix.utils.RaspberryAPI;

public class ConnectActivity extends AppCompatActivity {

    private static final int ACTIVITY_CONNECT_CODE = 1;
    public static final String FORKLIFT_KEY = "FORKLIFT";

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
            String boxBarcode = editText.getText().toString(); //1
            System.out.println("Confirm to connect to box " + boxBarcode);

            if(isValid(boxBarcode)){
                this.forklift.setIdRaspberry(Integer.getInteger(boxBarcode));
                this.forklift.setPlacementNumber(2);

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
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                //TODO ERROR
                                Toast.makeText(ConnectActivity.this, "Connection error to the box", Toast.LENGTH_SHORT).show();
                            }
                        });
                Controller.getInstance(this).addToRequestQueue(jsonObjectRequest);

                //shared forklift with OrderActivity
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putExtra(FORKLIFT_KEY, forklift);
                startActivityForResult(intent, ACTIVITY_CONNECT_CODE);
            } else {
                Toast.makeText(ConnectActivity.this, "Get valid box code", Toast.LENGTH_SHORT).show();
            }
            //todo partire chiamata di richiesta di connessione al box
            //todo popup o testo di conferma poi cambio schermata
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
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
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

}
