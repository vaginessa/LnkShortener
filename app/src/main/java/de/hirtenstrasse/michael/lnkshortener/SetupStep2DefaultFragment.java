package de.hirtenstrasse.michael.lnkshortener;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupStep2DefaultFragment extends Fragment {


    public SetupStep2DefaultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflater =  inflater.inflate(R.layout.fragment_setup_step2_default, container, false);


        CheckBox tos = (CheckBox) myInflater.findViewById(R.id.tosCheckBox);
        final Button next = (Button) myInflater.findViewById(R.id.nextButton);

        tos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                    if(isChecked) {
                                                        next.setEnabled(true);
                                                    } else {
                                                        next.setEnabled(false);
                                                    }
                                               }
                                           }
        );


        // Adding the Github Buttons
        WebView webview = (WebView) myInflater.findViewById(R.id.webViewTOS);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.setBackgroundColor(0x00000000);


        webview.loadUrl("file:///android_asset/tos.html");

        return myInflater;

    }

}
