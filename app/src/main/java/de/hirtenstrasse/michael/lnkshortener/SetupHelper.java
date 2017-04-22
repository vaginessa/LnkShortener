package de.hirtenstrasse.michael.lnkshortener;

// Copyright (C) 2017 Michael Achmann

//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


// This Class is a dirty wrapper for Signing App users up automatically an
// anonymously. If there was an API things would be a lot easier und cleaner.
// For the moment this Setup will do in order to prevent spam abuse with 1n.pm


public class SetupHelper {

    private String username, password, email, serverURL, apiKey;
    // type is a variable to show which to of setup we just attempted.
    // 0 = anonymous, 1 = user, 2 = custom
    private int type = -1;
    private final String android_id = UUID.randomUUID().toString();

    private static Context context;
    private static SharedPreferences sharedPref;
    private final RequestQueue queue;

    public SetupHelper(Context context){
        this.context = context;
        username = "";
        password = "";
        email = "";
        serverURL = "";
        // Initiate Volley Request Queue
        queue = Volley.newRequestQueue(this.context);
    }

    public void setData(String serverURL, String username){
        this.serverURL = serverURL;
        this.username = username;
    }

    public int getType() {
        return this.type;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getServerURL(){
        return this.serverURL;
    }

    public String getApiKey(){
        return this.apiKey;
    }

    public void setApiKey(String apiKey){
        this.apiKey = apiKey;
    }

    public void setServerURL(String serverURL){
        this.serverURL = serverURL;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public void createAnonymousAccountData()
    {
        String locale = Locale.getDefault().getCountry();
        locale = locale.toLowerCase();

        String lanloc = Locale.getDefault().toString();

        username = "android-"+android_id+"-"+lanloc;
        password = randomPassword();
        email = android_id+"@android.com";

        type = 0;

        Log.d("USER", username);
        Log.d("MAIL", email);
        Log.d("PASS", password);

    }

    public void signUp(final Response.Listener<String> listenerResponse, final Response.ErrorListener listenerError){

        // Assembles the URL and starts the API-Request
        final String url;
        url = serverURL+"/signup";

        // Actual Request to the API
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        final String token;
                        token = renderSignupPage(response);


                        // Actual Request to the API
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                listenerResponse, listenerError) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", username);
                                params.put("password", password);
                                params.put("email", email);
                                params.put("_token", token);

                                return params;
                            }
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("User-Agent", "LnkShortener App");
                                return params;
                            }
                        } ;
                        queue.add(stringRequest);
                    }
                },listenerError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "LnkShortener App");
                return params;
            }
        };



        // Access the RequestQueue through your singleton class.
        queue.add(getRequest);
    }

    public void queryNewApiKey(final Response.Listener<String> listenerResponse, final Response.ErrorListener listenerError){

        // First of all we will need to login with the account info from SharedPreferences

        final Map<String, String> accountInfo = retrieveAccountInfo();



        // Assembles the URL and starts the API-Request
        final String url;
        url = serverURL+"/login";

        // Actual Request to the API
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        final String token;
                        token = renderSignupPage(response);

                        Log.d("TOK", "Retrieved token for Login:");
                        Log.d("TOK", token);


                        // Network request for the Loginpage
                        String url = serverURL+"/login";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        // Now, if we're logged in we're ready for the last request
                                        // which finally obtains an API-Key
                                        String url = serverURL+"/admin";
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                listenerResponse, listenerError) {
                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("User-Agent", "LnkShortener App");
                                                return params;
                                            }
                                        };
                                        queue.add(stringRequest);


                                    }
                                }, listenerError) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", accountInfo.get("username"));
                                params.put("password", accountInfo.get("password"));
                                params.put("_token", token);

                                return params;
                            }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("User-Agent", "LnkShortener App");
                                return params;
                            }
                        };
                        queue.add(stringRequest);

                    }
                }, listenerError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "LnkShortener App");
                return params;
            }
        };



        // Access the RequestQueue through your singleton class.
        queue.add(getRequest);

    }

    private String randomPassword(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int length = 32;
        char tempChar;
        for (int i = 0; i < length; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private Map<String, String> retrieveAccountInfo(){

        Map<String, String> accountInfo = new HashMap<String, String>();

        accountInfo.put("android_id", android_id);
        accountInfo.put("username", username);
        accountInfo.put("password", password);

        return accountInfo;

    }

    private String renderSignupPage(String html){

        String csrf_token = "";

        Document doc = Jsoup.parse(html);
        Element csrf_input = doc.select("input[name=_token]").first();

        csrf_token = csrf_input.attr("value");

        return csrf_token;
    }

    public String renderApiKey(String html){
        String api_key = null;

        Document doc = Jsoup.parse(html);
        Element apiField = doc.select(".status-display").first();

        try {
            api_key = apiField.attr("value");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return api_key;

    }

    private void saveApiKey(String api_key){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("api_key", api_key);
        editor.commit();
    }

    public void testAPI(final Response.Listener<String> responseListener, final Response.ErrorListener errorListener){

        String testUrl = "https://google.com";

        // Assembles the URL and starts the API-Request
        String url = serverURL+"/api/v2/action/shorten?key="+apiKey+"&url=" + testUrl;
        // Actual Request to the API
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // If we receive a proper Response query the API in inverse
                        Log.d("SHORT", response);

                        String encodedEnding = null;

                        URL shortURL = null;
                        try {
                            shortURL = new URL(response);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        String ending = shortURL.getPath();
                        ending = ending.substring(1);

                        // Tries to encode the URL
                        try {
                            encodedEnding = URLEncoder.encode(ending, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String reverseUrl = serverURL+"/api/v2/action/lookup?key="+apiKey+"&url_ending="+encodedEnding;
                        // Actual Request to the API
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, reverseUrl,
                                responseListener, errorListener
                        );

                        queue.add(stringRequest);

                    }
                },errorListener);

        queue.add(stringRequest);

    }






}
