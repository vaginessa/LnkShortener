package de.hirtenstrasse.michael.lnkshortener;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

        // We check for old data, if it exists oldData is true
        Bundle bundle = new Bundle();

        boolean oldData = false;
        // We check the shared preferences for non-stock API-Settings
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = sharedPref.getString("api_key", null);
        if(apiKey != "8a4a2c54d582048c31aa85baaeb3f8" && apiKey != ""){
            oldData = true;
        }
        bundle.putBoolean("oldData", oldData);

        // Sending data to fragment
        setupStep1Fragment.setArguments(bundle);

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
                //
                SetupStep3LoginFragment login = new SetupStep3LoginFragment();
                //
                transaction.replace(R.id.fragment_container, login);

                transaction.addToBackStack("");
                transaction.commit();
                break;
        }



    }

    public void openBrowserResetPassword(View view){
        // Opens the Reset Password page when Button is pressed.
        Uri webpage = Uri.parse("https://1n.pm/lost_password");
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(webpage);
        startActivity(webIntent);

    }

    public void openBrowserSignup(View view){
        // Opens the Reset Password page when Button is pressed.
        Uri webpage = Uri.parse("https://1n.pm/signup");
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(webpage);
        startActivity(webIntent);

    }

    public void backToSetup1Fragment(View view){

        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));

    }

    public void backToStep2DefaultFragment(View view){

        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }


}
