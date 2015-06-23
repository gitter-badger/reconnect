package com.example.peter.reconnect;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 13/06/15.
 */
public class RequestHttp {
    private final String IP = "http://192.168.35.1/verifica.php";

    // Este metodo consiste em enviar um comando POST para o servidor do Senac permitindo o usu�rio logar-se na rede Wifi
    public HttpResponse clientHTTP(String username, String passwd, boolean termo) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(IP);
        HttpResponse response = null;
        StatusLine statusLine;
        List<NameValuePair> pairs = new ArrayList<>();

        pairs.add(new BasicNameValuePair("user", username));
        pairs.add(new BasicNameValuePair("pass", passwd));
        if (termo) {
            pairs.add(new BasicNameValuePair("termo", "on"));
        } else {
            pairs.add(new BasicNameValuePair("termo", "off"));
        }
        pairs.add(new BasicNameValuePair("mode_login.x", "0"));
        pairs.add(new BasicNameValuePair("mode_login.y", "0"));
        pairs.add(new BasicNameValuePair("redirect", "www.google.com.br"));
        pairs.add(new BasicNameValuePair("mac", "$mac"));
        pairs.add(new BasicNameValuePair("token", "$token"));
        pairs.add(new BasicNameValuePair("redirect", "$redirect"));
        pairs.add(new BasicNameValuePair("gateway", "$gateway"));
        pairs.add(new BasicNameValuePair("timeout", "$timeout"));
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
            response = client.execute(post);
            Log.d("Http Post Response:", response.toString());
            //  statusLine = response.getStatusLine();
            // Log.d("Http Post Response:", "code: " + statusLine.getStatusCode());

        } catch (Exception e) {
            e.printStackTrace();
//            statusLine = response.getStatusLine();
            //   Log.d("Http Post Response:", "code: " + statusLine.getStatusCode());
        }
        // Log.d("Http Post Response:", "code: " + statusLine.getStatusCode());
        return response;
    }
}
