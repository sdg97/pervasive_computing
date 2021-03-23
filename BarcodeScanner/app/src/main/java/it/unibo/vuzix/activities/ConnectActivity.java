package it.unibo.vuzix.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class ConnectActivity extends Activity { //implements AsyncRequest.AsyncRequestListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void onCompleteRequest(String result) {

    }

}
