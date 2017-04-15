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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


// This Class is a dirty wrapper for Signing App users up automatically an
// anonymously. If there was an API things would be a lot easier und cleaner.
// For the moment this Setup will do in order to prevent spam abuse with 1n.pm


public class InitialSetup {

    private String APIKey, DeviceID, username, password, email;
    String android_id = UUID.randomUUID().toString();

    private static Context context;
    private static SharedPreferences sharedPref;
    private final RequestQueue queue;

    public InitialSetup(Context context){
        this.context = context;
        // Receive the apiKey and Url from the shared preferences
        PreferenceManager.setDefaultValues(context, R.xml.main_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        queue = Volley.newRequestQueue(this.context);
    }

    public void signUp(){

        // TODO: The android_id needs to be written to the shared preferences
        username = "android-"+android_id;
        password = randomPassword();
        email = android_id+"@android.com";

        Log.d("USER", username);
        Log.d("MAIL", email);
        Log.d("PASS", password);


        // Assembles the URL and starts the API-Request
        final String url;
        url = "https://1n.pm/signup";

        // Actual Request to the API
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        final String token;
                        token = renderSignupPage(response);


                        // Actual Request to the API
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Now everything should be fine, new User should be set-up.
                                        // Next we want to save the unique Android-id and a random password
                                        // in the Shared preferences, before we update the API-Key
                                        saveAccountInfo(android_id, username, password);
                                        queryNewApiKey();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", username);
                                params.put("password", password);
                                params.put("email", email);
                                params.put("_token", token);

                                return params;
                            }
                        };
                        queue.add(stringRequest);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        }) {
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

    private void queryNewApiKey(){

        // First of all we will need to login with the account info from SharedPreferences

        final Map<String, String> accountInfo = retrieveAccountInfo();



        // Assembles the URL and starts the API-Request
        final String url;
        url = "https://1n.pm/login";

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
                        String url = "https://1n.pm/login";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        // Now, if we're logged in we're ready for the last request
                                        // which finally obtains an API-Key
                                        String url = "https://1n.pm/admin";
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        String api_key = renderApiKey(response);
                                                        saveApiKey(api_key);
                                                        Log.d("API", api_key);

                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {


                                            }
                                        }) {
                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("User-Agent", "LnkShortener App");
                                                return params;
                                            }
                                        };
                                        queue.add(stringRequest);


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }) {
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        }) {
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

    private void setAPIKey(){

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

    private void saveAccountInfo(String androidId, String username, String password){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("android_id", androidId);
        editor.putString("username", username);
        // We only save passwords which have been randomly created by the device
        // due to safety concerns user passwords will never be saved anywhere!
        editor.putString("password", password);
        editor.commit();
    }

    private Map<String, String> retrieveAccountInfo(){

        Map<String, String> accountInfo = new HashMap<String, String>();

        accountInfo.put("android_id", sharedPref.getString("android_id", null));
        accountInfo.put("username", sharedPref.getString("username", null));
        accountInfo.put("password", sharedPref.getString("password",null));

        return accountInfo;

    }

    private String renderSignupPage(String html){

        String csrf_token = "";

        Document doc = Jsoup.parse(html);
        Element csrf_input = doc.select("input[name=_token]").first();

        csrf_token = csrf_input.attr("value");

        return csrf_token;
    }

    private String renderApiKey(String html){
        String api_key = "";

        Document doc = Jsoup.parse(html);
        Element apiField = doc.select(".status-display").first();

        api_key = apiField.attr("value");

        return api_key;

    }

    private void saveApiKey(String api_key){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("api_key", api_key);
        editor.commit();
    }






}
