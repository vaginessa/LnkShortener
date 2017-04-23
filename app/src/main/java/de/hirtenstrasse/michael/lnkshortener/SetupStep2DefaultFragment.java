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
 * Step 2 in the Setup Assistant. Shows the options for the use of 1n.pm
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


        //  Loading the TOS from local html file
        WebView webview = (WebView) myInflater.findViewById(R.id.webViewTOS);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.setBackgroundColor(0x00000000);
        // It is saved in the assets
        webview.loadUrl("file:///android_asset/tos.html");

        return myInflater;

    }

}
