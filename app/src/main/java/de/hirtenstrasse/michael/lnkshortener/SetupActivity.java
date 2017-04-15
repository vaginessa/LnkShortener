package de.hirtenstrasse.michael.lnkshortener;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Preparing Fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // MainFragment is the start screen
        SetupStep1Fragment setupStep1Fragment = new SetupStep1Fragment();

        // Finally MainFragment is added to the main container
        transaction.replace(R.id.fragment_container, setupStep1Fragment);
        transaction.commit();

    }

    public void nextClickSetup1(View view){


        RadioGroup firstSelection = (RadioGroup) findViewById(R.id.firstSelectionRadioGroup);

        int selected = firstSelection.getCheckedRadioButtonId();

        // Preparing Fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (selected) {
            case R.id.customRadio:


                // SetupStep2CustomFragment is the fragment for Custom Setup
                SetupStep2CustomFragment custom = new SetupStep2CustomFragment();
                // Finally MainFragment is added to the main container
                transaction.replace(R.id.fragment_container, custom);

                transaction.addToBackStack("");
                transaction.commit();
                break;

            case R.id.defaultRadio:
                // SetupStep2DefaultFragment is the fragment for the 1n.pm Setup
                SetupStep2DefaultFragment npm = new SetupStep2DefaultFragment();
                // Finally MainFragment is added to the main container
                transaction.replace(R.id.fragment_container, npm);

                transaction.addToBackStack("");
                transaction.commit();
                break;

            case R.id.keepRadio:
                // We simply set a bool to sharedprefs that tells us to keep the prefs.
                // Also we Intent back to the MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }



    }

    public void nextClickSetup2Default(View view){

        RadioGroup firstSelection = (RadioGroup) findViewById(R.id.npmRadioGroup);

        int selected = firstSelection.getCheckedRadioButtonId();

        // Preparing Fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (selected) {
            case R.id.radioAnonymous:
                //
                SetupStep3AnonymousFragment anon = new SetupStep3AnonymousFragment();
                //
                transaction.replace(R.id.fragment_container, anon);

                transaction.addToBackStack("");
                transaction.commit();

                break;
            case R.id.radioLogin:
                break;
        }



    }
}
