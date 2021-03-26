package it.unibo.vuzix.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

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

public class ConnectActivity extends Activity { //implements AsyncRequest.AsyncRequestListener {

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
            Editable code = editText.getText();
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

    private <T> void launchActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }

}
