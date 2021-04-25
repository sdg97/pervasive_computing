package it.unibo.vuzix.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    Button connectBoxButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBoxButton = findViewById(R.id.boxConnectButton);
        connectBoxButton.setOnClickListener(this);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
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
        if (view.getId() == R.id.boxConnectButton) {
            System.out.println("Press Button to connect");
            launchActivity(ConnectActivity.class);
        } else if (view.getId() == R.id.exitButton) {
            System.out.println("Press Button to exit");
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("myDialog");
            ab.setMessage("Are you sure to exit?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //if you want to kill app . from other then your main avtivity.(Launcher)
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    //if you want to finish just current activity
                    MainActivity.this.finish();
                }
            });
            ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ab.show();
        }
    }

    private <T> void launchActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
