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

public class OrderActivity extends Activity implements View.OnClickListener {
    private Button confirmButton;
    private Button backButton;
    private EditText editText;
    private int numOrder = 0;
    private int MAXORDER = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        confirmButton = findViewById(R.id.confermOrderButton);
        confirmButton.setEnabled(false);
        confirmButton.setOnClickListener(this);

        backButton = findViewById(R.id.button6);
        backButton.setOnClickListener(this);

        editText = findViewById(R.id.barcodeOrder);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    confirmButton.setEnabled(true);
            }
        });
    }

    private <T> void launchActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confermOrderButton) { //CONFERM
            System.out.println("Ok");
            Editable code = editText.getText();
            //todo partire chiamata di richiesta di connessione all'ordine
            //todo popup o testo di conferma poi cambio schermata
            numOrder++;
            if (numOrder < MAXORDER){
                AlertDialog.Builder ab = new AlertDialog.Builder(OrderActivity.this);
                ab.setTitle("Would you add an other order?");
                ab.setMessage("Would you add an other order?");
                ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //todo reload the same activity
                        editText.setText("");
                    }
                });
                ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        OrderActivity.this.finish();
                        launchActivity(ShowLocationActivity.class);
                    }
                });
                ab.show();
            } else {
                Toast.makeText(OrderActivity.this, "You have reached the maximum number of orders that can be managed", Toast.LENGTH_SHORT).show();
                launchActivity(ShowLocationActivity.class);
                //OrderActivity.this.finish();
            }

        } else if (view.getId() == R.id.button6) { //BACK
            System.out.println("back");
            //TODO It's right?!?!?
            OrderActivity.this.finish();
            setContentView(R.layout.activity_connect);
        }
    }
}
