package de.hirtenstrasse.michael.lnkshortener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;

public class ScreenshotTestActivity extends AppCompatActivity {

    WebView webview;
    ImageView imageview, imageview2;
    EditText editText;
    String url;
    ProgressBar progressBar;
    private RequestQueue queue;

    @JavascriptInterface
    public void processHTML(final String source) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parsePage(source);
            }
        });
        }

    public void parsePage(String source){

        Log.d("INFO", "Invoking parsePage");

        webview.destroy();

        String og_image = "";

        Document doc = Jsoup.parse(source);
        Element meta_tag = doc.select("meta[property=og:image]").first();
        if(meta_tag != null) {
            og_image = meta_tag.attr("content");
        }

        Element meta_author = doc.select("meta[name=author]").first();
        Element meta_keywords = doc.select("meta[name=keywords]").first();
        Element meta_description = doc.select("meta[name=description]").first();
        Element meta_title = doc.select("title").first();
        Element meta_news_keywords = doc.select("meta[name=news_keywords]").first();

        if(meta_author != null){
            TextView author = (TextView) findViewById(R.id.metaAuthor);
            author.setText(meta_author.attr("content"));
        }

        if(meta_keywords != null){
            TextView keywords = (TextView) findViewById(R.id.metaKeywords);
            keywords.setText(meta_keywords.attr("content"));
        } else if (meta_news_keywords != null){
            TextView keywords = (TextView) findViewById(R.id.metaKeywords);
            keywords.setText(meta_news_keywords.attr("content"));

        }

        if(meta_description != null){
            TextView description = (TextView) findViewById(R.id.meteDescription);
            description.setText(meta_description.attr("content"));
        }

        if(meta_title != null){
            TextView title = (TextView) findViewById(R.id.metaTitle);
            title.setText(meta_title.text());
        }




        if(!og_image.matches("")) {

            if (Character.toString(og_image.charAt(0)).matches("/")) {
                og_image = url + og_image;
            }


            new DownloadImageTask(imageview2).execute(og_image);
            Log.d("IMG", "Found image: " + og_image);
        }

        progressBar.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        queue = Volley.newRequestQueue(getApplicationContext());


    }

    public void getScreenshot(View view){

        progressBar.setVisibility(View.VISIBLE);

        webview = new WebView(ScreenshotTestActivity.this);

        // Declaring the UI-Elements
        imageview = (ImageView) findViewById(R.id.imageview);
        imageview2 = (ImageView) findViewById(R.id.imageview2);
        editText = (EditText) findViewById(R.id.urlEditText);
        url = editText.getText().toString();

        // For the width of the image we want to grab the width of the device
        Display disp = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        disp.getSize(size);
        createScreenshot();


    }

    private void createScreenshot(){
        // Declaring the UI-Elements
        imageview = (ImageView) findViewById(R.id.imageview);
        editText = (EditText) findViewById(R.id.urlEditText);
        url = editText.getText().toString();

        // For the width of the image we want to grab the width of the device
        Display disp = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        disp.getSize(size);



        Log.d("HTML", "Render HTML "+url);
        webview.loadUrl(url);
        webview.measure(size.x,size.y);
        webview.layout(0,0,size.x,size.y);
        // webview.setInitialScale(0);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(this, "HTMLOUT");


        webview.setWebViewClient(new WebViewClient(){
            public void onPageFinished(final WebView webview1, String url){
                webview1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        webview1.measure(View.MeasureSpec.makeMeasureSpec(
                                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                        webview1.setDrawingCacheEnabled(true);
                        webview1.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(webview1.getMeasuredWidth(),
                                1000, Bitmap.Config.ARGB_8888);
                        Canvas c = new Canvas(bitmap);

                        int iHeight = bitmap.getHeight();
                        Paint paint = new Paint();

                        c.drawBitmap(bitmap, 0,iHeight,paint);

                        webview1.draw(c);

                        imageview.setImageBitmap(bitmap);
                    }
                }, 0);
            }
        });

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}