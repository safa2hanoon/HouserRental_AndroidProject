package edu.berziet.houserental.tools;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class HttpManager {
    public static String getData(String URL) {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection httpURLConnection =
                    (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new
                    InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line + '\n');
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            Log.d("HttpURLConnection", ex.toString());
        }
        return null;
    }

    public static String sendRequest(String URL, String requestMethod, String requestBody) {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection httpURLConnection =
                    (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setRequestProperty("Content-Length", "" + requestBody.getBytes().length);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setUseCaches(false);//set true to enable Cache for the req
            httpURLConnection.setDoOutput(true);//enable to write data to output stream
            if(requestBody!=null && ! requestBody.isEmpty() ) {
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(requestBody.getBytes());
                os.flush();
                os.close();
            }

            bufferedReader = new BufferedReader(new
                    InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line + '\n');
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            Log.d("HttpURLConnection", ex.toString());
        }
        return null;
    }
}
