package it.unibo.vuzix.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowLocationActivity extends Activity implements View.OnClickListener {

    private Button okButton;
    private Button pickedButton;
    private TextView locationElement;
    private TextView quantity;
    private TextView idProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlocation);

        this.pickedButton = findViewById(R.id.pickedButton);
        this.pickedButton.setOnClickListener(this);

        this.okButton = findViewById(R.id.okButton);
        this.okButton.setOnClickListener(this);

        this.locationElement = findViewById(R.id.textElemLocation);
        this.quantity = findViewById(R.id.quantity);
        this.idProduct = findViewById(R.id.productCode);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.okButton){



        } else if(view.getId() == R.id.pickedButton){
            //get next element
            this.locationElement.setText("99.B.25.30");
        }
    }
}
