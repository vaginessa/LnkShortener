package de.hirtenstrasse.michael.lnkshortener;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SetupStep2CustomFragment extends Fragment {


    public SetupStep2CustomFragment() {
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
        View myInflater = inflater.inflate(R.layout.fragment_setup_step2_custom, container, false);


        // Listener for enabling the login button

        final EditText urlEditText = (EditText) myInflater.findViewById(R.id.urlEditText);
        final EditText apiEditText = (EditText) myInflater.findViewById(R.id.apiEditText);
        final Button checkButton = (Button) myInflater.findViewById(R.id.checkButton);

        urlEditText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!urlEditText.getText().toString().matches("") && !apiEditText.getText().toString().matches("")){
                    checkButton.setEnabled(true);
                } else {
                    checkButton.setEnabled(false);
                }
                return false;
            }
        });

        apiEditText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!urlEditText.getText().toString().matches("") && !apiEditText.getText().toString().matches("")){
                    checkButton.setEnabled(true);
                } else {
                    checkButton.setEnabled(false);
                }
                return false;
            }
        });


        return myInflater;

    }
}

