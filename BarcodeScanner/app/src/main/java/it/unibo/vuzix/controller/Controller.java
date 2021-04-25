package it.unibo.vuzix.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Controller {
    private static Controller controller;
    private RequestQueue requestQueue;
    private static Context context;

    private Controller(Context context){
        this.context = context;
        //requestQueue = getRequestQueue();
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized Controller getInstance(Context context) {
        if (controller == null) {
            controller = new Controller(context);
        }
        return controller;
    }

    public RequestQueue getRequestQueue() {
       /* if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }*/
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }
}
