package it.unibo.vuzix.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

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
            System.out.println("confirm");
        });
        confirmButton.setEnabled(false);

        backButton = findViewById(R.id.button4);
        backButton.setOnClickListener(v -> {
            System.out.println("back");
        });

        editText = findViewById(R.id.barcodetext);
        editText.setOnClickListener(v -> confirmButton.setEnabled(true));

    }

}
