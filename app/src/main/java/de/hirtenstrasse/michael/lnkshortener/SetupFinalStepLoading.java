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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SetupFinalStepLoading extends Fragment {

    TextView statusText, statusLog;
    ProgressBar statusBar, statusSpinner;
    ImageView doneImage;
    Button finishButton;

    int step;

    public SetupFinalStepLoading() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflater = inflater.inflate(R.layout.fragment_final_step_loading, container, false);

        //  Assign variables for use in voids
        statusBar = (ProgressBar) myInflater.findViewById(R.id.statusBar);
        statusSpinner = (ProgressBar) myInflater.findViewById(R.id.statusSpinner);
        statusText = (TextView) myInflater.findViewById(R.id.statusText);
        doneImage = (ImageView) myInflater.findViewById(R.id.doneIcon);
        finishButton = (Button) myInflater.findViewById(R.id.finishButton);
        statusLog = (TextView) myInflater.findViewById(R.id.statusLogEditText);

        // Step for the Status Log
        step = 1;

        // Retrieve arguments passed to Fragment
        Bundle bundle = this.getArguments();
        if(bundle != null){
            if(bundle.get("statustext")!= null) {
                // We directly set the text, because the default shouldn't be shown in the log
                statusText.setText(bundle.getString("statustext"));
            }
        }


        return myInflater;
    }

    public void updateStatus(String text){
        String oldStatus, oldLog, newLog;

        oldStatus= statusText.getText().toString();
        oldLog = statusLog.getText().toString();

        newLog = "["+step+"] " + oldStatus + "\n" + oldLog;
        step++;

        statusLog.setText(newLog);
        statusText.setText(text);
    }

    public void updateStatusPercent(int status){

        if(status > 0 && status <= 100){

            statusBar.setProgress(status);

        }

        if(status == 100){
            statusSpinner.setVisibility(View.GONE);
            doneImage.setVisibility(View.VISIBLE);
        }

    }

    public void enableFinishButton(){
        finishButton.setEnabled(true);
    }

}
