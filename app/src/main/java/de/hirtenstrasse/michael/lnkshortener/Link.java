package de.hirtenstrasse.michael.lnkshortener;

import android.content.Context;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michael on 12.12.17.
 */

public class Link {

    private String url;
    private String text;
    private int urlType;
    private PolrAPI api;
    private String shortenedURL;
    private boolean shortening;

    private Context context;

    // Constructor expecting an URL
    public Link(Context context, String url){
        setUrl(url);
        this.context = context;
    }

    // Constructor expecting an URL and api
    public Link(Context context, String url, PolrAPI api){
        setUrl(url);
        this.context = context;
    }

    // Constructor api only
    public Link(Context context, PolrAPI api){
        this.api = api;
        this.context = context;
    }

    // Empty Constructor
    public Link(Context context){
        this.context = context;
    }


    // Returns the url @Override toString
    @Override
    public String toString(){
        return this.url;
    }


    // Returns the url as String
    public String getUrl(){
        return this.url;
    }

    // Returns the shortURL
    public String getShortenedURL(){
        return this.shortenedURL;
    }

    // Check whether there is already a shorturl on the shelve
    public boolean hasShortUrl(){
        if(this.shortenedURL.isEmpty()) return false;
        else return true;
    }

    // Set the URL
    public void setUrl(String url){
        this.url = url;
    }

    // Determine whether url is a valid URL
    public boolean isValid(){
        return false;
    }

    // Check whether url has been set
    public boolean isEmpty(){
        return this.url.isEmpty();
    }

    // Add the URL from text.
    public void urlFromText(String text){
        List<String> urls = findURLs(text);

        if(urls.size() > 0){
            this.url = urls.get(0);
        }

    }


    // Returns the type of url
    public int getUrlType(){
        return 0;
    }


    // Shortens the url and return shortenedURL
    public void shorten(){
        try {
            this.api.shortenURL(this.url);
            // TODO: Das muss raus!
            while(!this.api.isCallFinished()){
                shortening = true;
            }
            shortenedURL = this.api.getShortURL();
            shortening = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setApi(PolrAPI api){
        this.api = api;
    }

    public PolrAPI getApi(){
        return this.getApi();
    }


    private void guessUrl(){

    }





    /*
     * Finds URLs in any Text and returns them as a List
     */

    private List<String> findURLs(String url){

        List<String> urls = new ArrayList<String>();
        String regex = "((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher urlMatcher = pattern.matcher(url);

        while(urlMatcher.find()){
            urls.add(url.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }


        return urls;
    }


}
