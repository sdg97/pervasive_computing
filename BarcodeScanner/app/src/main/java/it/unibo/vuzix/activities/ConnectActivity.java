package it.unibo.vuzix.activities;

import android.app.Activity;
import android.content.Context;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import it.unibo.vuzix.controller.Controller;
import it.unibo.vuzix.utils.RaspberryAPI;

public class ConnectActivity extends AppCompatActivity {

    private Button confirmButton;
    private Button backButton;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        confirmButton = findViewById(R.id.button);
        confirmButton.setOnClickListener(v -> {
            System.out.println("Confirm");
            Editable boxBarcode = editText.getText(); //1
            if(isValid(boxBarcode.toString())){
                //localhost:5000/smartForklift/1/action/startUse
                String url = RaspberryAPI.setStartUse(boxBarcode.toString());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            //{placements: [0, 1]}
                            //todo
                            @Override
                            public void onResponse(JSONObject response) {

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
