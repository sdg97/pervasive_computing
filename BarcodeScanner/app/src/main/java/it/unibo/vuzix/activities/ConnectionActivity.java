package it.unibo.vuzix.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.vuzix.sample.barcode_scan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.Random;

import it.unibo.vuzix.netutils.Http;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
       /* findViewById(R.id.connectionStatusBtn).setOnClickListener(v -> {
            final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            final NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();

            if(activeNetwork.isConnectedOrConnecting()){
                ((TextView)findViewById(R.id.statusLabel)).setText("Network is connected");
            }
        });

        findViewById(R.id.getBtn).setOnClickListener(v -> {
            tryHttpGet();
        });

        findViewById(R.id.postBtn).setOnClickListener(v -> {
            try {
                tryHttpPost();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });*/
    }

    private void tryHttpGet(){
        final String url = "http://dummy.restapiexample.com/api/v1/employee/1";

        /*Http.get(url, response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                try {
                    ((TextView)findViewById(R.id.resLabel)).setText(response.contentAsString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    private void tryHttpPost() throws JSONException {

        final String url = "http://dummy.restapiexample.com/api/v1/create";
        final String content = new JSONObject()
                .put("name","test" + new Random().nextLong())
                .put("salary","1000")
                .put("age","30").toString();

        /*Http.post(url, content.getBytes(), response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                try {
                    ((TextView)findViewById(R.id.resLabel)).setText(response.contentAsString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/
}