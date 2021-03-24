package it.unibo.vuzix.utils.netutils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class HttpUtils {

    private static final String TAG = HttpUtils.class.getName();
    private static final int TIMEOUT = 10000;

    private HttpUtils() {}

    public static HttpResponse<String> sendRequest(String url, HttpMethod httpMethod) {
        return sendRequest(url, httpMethod, TIMEOUT);
    }

    public static HttpResponse<String> sendRequest(String url, HttpMethod httpMethod, int timeout) {
        return doRequest(url, httpMethod, null, timeout);
    }

    public static HttpResponse<String> sendRequestWithBody(String url, HttpMethod httpMethod, String body) {
        return doRequest(url, httpMethod, body, TIMEOUT);
    }

    public static HttpResponse<String> sendRequestWithBody(String url, HttpMethod httpMethod, String body, int timeout) {
        return doRequest(url, httpMethod, body, timeout);
    }

    private static HttpResponse<String> doRequest(String url, HttpMethod httpMethod, String body, int timeout) {
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection(url, httpMethod, timeout);
            if(body != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
            }
            connection.connect();

            if(body != null) {
                connection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();

            if(status >= 200 && status < 300) {
                return HttpResponse.success(status, readAll(connection.getInputStream()));
            } else {
                return HttpResponse.fail(status, connection.getResponseMessage());
            }
        } catch (MalformedURLException ex) {
            Log.d(TAG, ex.toString());
        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Log.d(TAG, ex.toString());
                }
            }
        }
        return HttpResponse.fail(500, "");
    }

    private static HttpURLConnection getHttpURLConnection(String url, HttpMethod httpMethod, int timeout) throws IOException {
        URL u = new URL(url);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestMethod(httpMethod.getName());
        c.setRequestProperty("Content-Type", "application/json");
        c.setRequestProperty("Accept", "application/json");
        c.setUseCaches(false);
        c.setAllowUserInteraction(false);
        c.setConnectTimeout(timeout);
        c.setReadTimeout(timeout);
        return c;
    }

    private static String readAll(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
