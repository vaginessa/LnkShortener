package de.hirtenstrasse.michael.lnkshortener;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class ScreenshotTestActivity extends AppCompatActivity {

    WebView webview;
    ImageView imageview;
    String url = "http://facebook.de";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = new WebView(ScreenshotTestActivity.this);
        imageview = (ImageView) findViewById(R.id.imageview);

        Display disp = getWindowManager().getDefaultDisplay();

        Point size = new Point();

        disp.getSize(size);


        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
        webview.measure(size.y,1000);
        webview.layout(0,0,size.y,1000);


        webview.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView webview1, String url){
                webview1.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                webview1.setDrawingCacheEnabled(true);
                webview1.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(webview1.getMeasuredWidth(),
                        800, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap);

                int iHeight = bitmap.getHeight();
                Paint paint = new Paint();

                c.drawBitmap(bitmap, 0,iHeight,paint);

                webview1.draw(c);

                imageview.setImageBitmap(bitmap);

            }
        });
    }

}
