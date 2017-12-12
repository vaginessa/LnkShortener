package de.hirtenstrasse.michael.lnkshortener;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by michael on 12.12.17.
 */

public class PolrAPI {

    private String key;
    private String apiUrl;
    private boolean callFinished;
    private String shortURL;
    private Context context;

    public RequestQueue requestQueue;

    public PolrAPI(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public PolrAPI(Context context, String key, String apiUrl){
        this.key = key;
        this.apiUrl = apiUrl;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        this.context = context;

    }

    public void setKey(String key){
        this.key = key;
    }

    public void setApiUrl(String apiUrl){
        this.apiUrl = apiUrl;
    }

    public String getKey(){
        return this.key;
    }

    public String getApiUrl(){
        return this.apiUrl;
    }


    /* TODO Sinnvolles update um mit den Threads und Asynchronen Ergebnissen zu
     arbeiten benötigt. */
    public void shortenURL (String url) throws Exception{

        // Setup variables
        String encodedOriginalUrl = null;

        // Tries to encode the URL
        try {
            encodedOriginalUrl = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Assembles the URL and starts the API-Request
        String requestURL = apiUrl+"/api/v2/action/shorten?key="+key+"&url=" + encodedOriginalUrl;
        // Actual Request to the API
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // If we receive a proper Response we set the response as the shortened URL
                        shortURL = response;
                        callFinished = true;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error);
            }
        }
        );

        // For volley we need to add our request to the queue. The queue starts automatically
        // Do NOT add a queue.start(), it provokes errors.
        requestQueue.add(stringRequest);

        return;
    }

    public String getShortURL(){
        return this.shortURL;
    }

    public boolean isCallFinished(){
        return this.callFinished;
    }
}
