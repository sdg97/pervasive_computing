package it.unibo.vuzix.utils.netutils;

import android.os.AsyncTask;

public class AsyncRequest extends AsyncTask<String, Void, String> {

    public interface AsyncRequestListener {
        void onCompleteRequest(String result);
    }

    private AsyncRequestListener listener;

    public void attach(AsyncRequestListener listener) {
        this.listener = listener;
    }

    public void detach() {
        this.listener = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!isCancelled())
            this.listener.onCompleteRequest(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        final String url = (strings.length > 0) ? strings[0] : "";
        if(!url.isEmpty()) {
            HttpResponse<String> response = HttpUtils.sendRequest(url, HttpMethod.GET);
            if(response.isSuccess()) return response.getResult();
        }
        return null;
    }


}
