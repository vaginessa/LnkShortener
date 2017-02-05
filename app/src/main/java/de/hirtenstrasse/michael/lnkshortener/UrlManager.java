package de.hirtenstrasse.michael.lnkshortener;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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

public class UrlManager {

    private static Context context;
    private static SharedPreferences sharedPref;

    public UrlManager(Context context){
        this.context = context;
        // Receive the apiKey and Url from the shared preferences
        PreferenceManager.setDefaultValues(context, R.xml.main_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // URL Validation
    public static boolean validateURL(String url){

        return Patterns.WEB_URL.matcher(url).matches();
    }

    // If the URL is valid this function adds e.g. http:// (the protocol) to the URL, if missing
    public static String guessUrl(String url){
        String returnurl = URLUtil.guessUrl(url);

        return returnurl;
    }

    /* Returns an int which corresponds with a certain type of URL
     *
     * Return Values:
     * 0 = No System URL
     * 1 = Shortened URL
     * 2 = System URL
     *
     */

    public static int getURLType(String url){
        int type = 0;

        if(!isOnApiHost(url)){
            return 0;
        } else if(isShortenedURL(url)){
            return 1;
        } else if (isSystemURL(url)){
            return 2;
        }

        return type;
    }

    public static boolean isShortenedURL(String url){

        if(!isOnApiHost(url)){
            return false;
        } else if(!isSystemURL(url)){
            return true;
        }

        return false;
    }

    public static boolean isSystemURL(String url){

        URL receivedUrl = null;
        String[] systemPaths = {"/about", "/signup", "/signin", "/lost_password", "/admin", "/setup", "/", "/api"};

        try {
            receivedUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("Path", receivedUrl.getPath());

        if(!isOnApiHost(url)){
            return false;
        } else if(Arrays.asList(systemPaths).contains(receivedUrl.getPath())){
            Log.d("Match", "Matched one of the SystemPaths");
            return true;
        }

        return false;
    }

    public static boolean isOnApiHost(String url){
        // Instantiate Variables
        URL receivedURL = null;
        URL apiURL = null;
        boolean isOnApi = false;

        // Let's try and get a URL from both of them
        try {
            receivedURL = new URL(url);
            apiURL = new URL(sharedPref.getString("url", null));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("receivedURL:", receivedURL.getHost());
        Log.d("apiURL", apiURL.getHost());

        if(receivedURL.getHost().equals(apiURL.getHost())){
            isOnApi = true;
            Log.d("Match","Received and API matched");
        }



        return isOnApi;
    }


}
