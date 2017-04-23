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

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SetupActivity extends AppCompatActivity {

    private SetupHelper helper;
    private SharedPreferences sharedPref;
    private UrlManager urlmanager;
    Context context;

    public Response.Listener<String> signupListener = new Response.Listener<String>()
    {
        @Override
        public void onResponse(String response){
            // Now everything should be fine, new User should be set-up.
            // Next we extract the API-Key before we can test it.
            // The API-Key is extracted by logging the user into the /admin site of serverUrl

            updateLoadingText(getString(R.string.setup_anonymous_username, helper.getUsername()));
            updateLoadingText(getString(R.string.setup_signup_done, helper.getUsername()));
            updateLoadingStatus(33);

            updateLoadingText(getString(R.string.setup_extracting_api_key));
            helper.queryNewApiKey(getAPIListener,getAPIErrorListener);

        }
    };

    public Response.ErrorListener signupErrorListener = new Response.ErrorListener(){

        @Override
        public void onErrorResponse(VolleyError error) {

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                // Is thrown if there's no network connection or server is down
                Toast.makeText(context, getString(R.string.error_network_timeout),
                        Toast.LENGTH_LONG).show();
                // We return to the last fragment
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }

            } else {
                    // Is thrown if there's no network connection or server is down
                    Toast.makeText(context, getString(R.string.error_network),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
            }
        }
    };


    public Response.Listener<String> testAPIListener = new Response.Listener<String>()
    {
        @Override
        public void onResponse(String response){

            // Here we check the response to see whether its google.com. If it is true we can assume that
            // Everything is working alright.


            Log.d("GOG", response);

            if(response.matches("https://google.com")){
                // It matches soo:

                // Everything done, so status = 100
                updateLoadingStatus(100);
                updateLoadingText(getString(R.string.setup_tested_api, helper.getApiKey()));

                // Now we still need to save the API-Key to the SharedPrefs
                saveAPIKey(helper.getType());

                // We can enable the finish button
                // and display success message
                enableFinishButton();

                if(helper.getType()==0) {
                    // We're dealing with an anonymous account.
                    updateLoadingText(getString(R.string.setup_signup_done));
                } else if(helper.getType()==1) {
                    // We're dealing with a 1n.pm user account
                    updateLoadingText(getString(R.string.setup_useraccount_successfull, helper.getUsername()));
                } else if(helper.getType()==2){
                    // We're dealing with a custom server
                    updateLoadingText(getString(R.string.setup_custom_successfull, helper.getServerURL()));
                }

            } else {
                // Something went wrong, so we send an error Message.
                Toast.makeText(context, getString(R.string.error_testing_api),
                        Toast.LENGTH_LONG).show();
                // We return to the last fragment
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
            }



        }
    };

    public Response.ErrorListener testAPIErrorListener = new Response.ErrorListener(){

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("ERR", error.toString());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                // Is thrown if there's no network connection or server is down
                Toast.makeText(context, getString(R.string.error_network_timeout),
                        Toast.LENGTH_LONG).show();
                // We return to the last fragment
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }

            } else if (error instanceof AuthFailureError) {
                // Is thrown if the API-Key is wrong
                Toast.makeText(context, getString(R.string.error_apikey),
                        Toast.LENGTH_LONG).show();
                // We return to the last fragment
                if (getFragmentManager().getBackStackEntryCount() != 0) {
                    getFragmentManager().popBackStack();
                }
            }
                else if (error instanceof ServerError) {
                    // Is thrown if 404 or server down
                    Toast.makeText(context, getString(R.string.error_server),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                } else {
                    // Some other problem.
                    Toast.makeText(context, getString(R.string.error_network),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }

                }
            }
    };

    public Response.Listener<String> getAPIListener = new Response.Listener<String>()
    {
        @Override
        public void onResponse(String response){

            // First the var apiKey is initialized - it always need to be null in the first place
            String apiKey = null;

            // The response is the plain HTML of the /admin page. We need to extract the API-Key
            // from the HTMML, therefore we pass it to renderApiKey. After that we test the API-Key.
            for( String line : response.split("\n") ) {
                Log.d( "HTML", line );
            }
            apiKey = helper.renderApiKey(response);
            if(apiKey==null){
                // The reason for that is probably that the user isn't API-Enabled or Login failed.
                helper.setApiKey(null);
                Toast.makeText(context, getString(R.string.error_login_failed),
                        Toast.LENGTH_LONG).show();
                // We return to the last fragment
                if (getFragmentManager().getBackStackEntryCount() != 0) {

                    getFragmentManager().popBackStack();
                }

                return;
            } else {
                Log.d("API", apiKey);
                helper.setApiKey(apiKey);

                if (helper.getType() == 0) {
                    // ANONYMOUS
                    // Inform the User
                    updateLoadingText(getString(R.string.setup_extracted_api_key, helper.getUsername()));
                    updateLoadingStatus(66);
                } else if (helper.getType() == 1) {
                    // SIGNED UP USER
                    // Inform the User
                    updateLoadingText(getString(R.string.setup_extracted_api_key));
                    updateLoadingStatus(50);
                }
                // Now we verify whether API-Key is working
                updateLoadingText(getString(R.string.setup_testing_api));
                helper.testAPI(testAPIListener, testAPIErrorListener);

            }

        }
    };

    public Response.ErrorListener getAPIErrorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERR", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    // Is thrown if there's no network connection or server is down
                    Toast.makeText(context, getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }

                } else if (error instanceof AuthFailureError) {
                    // Is thrown if the API-Key is wrong
                    Toast.makeText(context, getString(R.string.error_apikey),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                }
                else if (error instanceof ServerError) {
                    // Is thrown if 404 or server down
                    Toast.makeText(context, getString(R.string.error_server),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }
                } else {
                    // Some other problem.
                    Toast.makeText(context, getString(R.string.error_network),
                            Toast.LENGTH_LONG).show();
                    // We return to the last fragment
                    if (getFragmentManager().getBackStackEntryCount() != 0) {
                        getFragmentManager().popBackStack();
                    }

                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        helper =  new SetupHelper(this);
        urlmanager = new UrlManager(this);

        // We introduces the SharedPreferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


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
        if(!apiKey.matches("8a4a2c54d582048c31aa85baaeb3f8") && apiKey != ""){
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
                // If this option is selected we want to setup an anonymous Account. Therefore We
                // Swap to the loading fragment and start signUpAnonymously()

                SetupFinalStepLoading anon = new SetupFinalStepLoading();

                // We send two pieces of information to anon: 1) statustext, 2) statusspinner
                Bundle bundle = new Bundle();
                bundle.putString("statuspercent", getString(R.string.setup_creating_uuid));

                // Add bundle to  anon
                anon.setArguments(bundle);

                // Ready for transaction
                transaction.replace(R.id.fragment_container, anon);
                transaction.addToBackStack("");
                transaction.commit();

                // Fragment changed, now we start the async Volley call.
                signUpAnonymously();

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
        // The best way of stepping back is to emulate pressing the back-button
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));

    }

    public void backToStep2DefaultFragment(View view){
        // The best way of stepping back is to emulate pressing the back-button
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    public void signUpAnonymously(){
        // First of all we create anonymous account data
        helper.createAnonymousAccountData();
        // Also the user decided for 1n.pm - hence we set the server String
        helper.setServerURL("https://1n.pm/");
        // Start of the async Volley request, which invokes the Listeners of this class
        helper.signUp(signupListener, signupErrorListener);

    }

    public void signInOnenpm(View view){
        // We set the type to signin
        helper.setType(1);
        // We set the 1n.pm server url
        helper.setServerURL("https://1n.pm/");

        EditText username = (EditText) this.findViewById(R.id.usernameEditText);
        EditText password = (EditText) this.findViewById(R.id.passwordEditText);

        // We set the username
        helper.setUsername(username.getText().toString());

        // We set the password
        helper.setPassword(password.getText().toString());

        // Preparing Fragment Transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SetupFinalStepLoading finalStep = new SetupFinalStepLoading();

        // We also need to add some arguments to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("statustext", getString(R.string.setup_signing_in, helper.getUsername()));
        finalStep.setArguments(bundle);

        // Ready for transaction
        transaction.replace(R.id.fragment_container, finalStep);
        transaction.addToBackStack("");
        transaction.commit();

        // Now we try to extract the API-Key
        helper.queryNewApiKey(getAPIListener, getAPIErrorListener);

    }


    public void testCustomAPIKey(View view){

        // Now we check the custom input. Therefore We
        // Swap to the loading fragment and save the values to helper

        // Preparing Fragment Transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SetupFinalStepLoading finalStep = new SetupFinalStepLoading();

        // We also need to add some arguments to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("statustext", getString(R.string.setup_testing_api));
        finalStep.setArguments(bundle);

        // Ready for transaction
        transaction.replace(R.id.fragment_container, finalStep);
        transaction.addToBackStack("");
        transaction.commit();

        // If the user decides to add a custom server we first save the data to helper
        // Afterwards we start the async task of testing the credentials.
        EditText urlEdit = (EditText) this.findViewById(R.id.urlEditText);
        EditText apiEdit = (EditText) this.findViewById(R.id.apiEditText);

        // The url should already be valid, because this has been validated before
        String url = urlmanager.guessUrl(urlEdit.getText().toString());

        helper.setServerURL(url);
        helper.setApiKey(apiEdit.getText().toString());
        // We set the type to custom
        helper.setType(2);

        Log.d("SET", url);
        Log.d("SET", apiEdit.getText().toString());

        helper.testAPI(testAPIListener,testAPIErrorListener);


    }

    public void saveAPIKey(int type){
        // Finally we can save the APIKey and other information in sharedprefs. Since we only save
        // confidential information like the password with anonymous accounts we call specific
        // voids for each option
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("api_key", helper.getApiKey());

        // We're dealing with a 1n.pm user, hence we save the the username
        if(type < 2)
            editor.putString("username", helper.getUsername());

        // Only the anonymous random generated password is being stored
        // unsafely on the device
        if(type==0)
            editor.putString("password", helper.getPassword());

        editor.putString("url", helper.getServerURL());

        // Here we set the bit which tells the MainActivity whether to start SetupActivity or not
        editor.putBoolean("first_start", true);
        editor.commit();
    }

    public void updateLoadingText(String text){
        SetupFinalStepLoading finalFragment = (SetupFinalStepLoading) getFragmentManager().findFragmentById(R.id.fragment_container);
        if(finalFragment != null){
            finalFragment.updateStatus(text);
        }
    }

    public void updateLoadingStatus(int n){
        SetupFinalStepLoading finalFragment = (SetupFinalStepLoading) getFragmentManager().findFragmentById(R.id.fragment_container);
        if(finalFragment != null){
            finalFragment.updateStatusPercent(n);
        }
    }

    public void enableFinishButton(){
        SetupFinalStepLoading finalFragment = (SetupFinalStepLoading) getFragmentManager().findFragmentById(R.id.fragment_container);
        finalFragment.enableFinishButton();

    }

    public void finishButtonPressed(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
