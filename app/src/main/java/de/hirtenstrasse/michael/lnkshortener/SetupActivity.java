package de.hirtenstrasse.michael.lnkshortener;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class SetupActivity extends AppCompatActivity {

    private SetupHelper helper;
    private SharedPreferences sharedPref;
    private UrlManager urlmanager;

    public Response.Listener<String> signupListener = new Response.Listener<String>()
    {
        @Override
        public void onResponse(String response){
            // Now everything should be fine, new User should be set-up.
            // Next we extract the API-Key before we can test it.
            // The API-Key is extracted by logging the user into the /admin site of serverUrl
            helper.queryNewApiKey(getAPIListener,getAPIErrorListener);
        }
    };

    public Response.ErrorListener signupErrorListener = new Response.ErrorListener(){

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: Add Logic. Basically go one step back and show error message / toast
        }
    };


    public Response.Listener<String> testAPIListener = new Response.Listener<String>()
    {
        // TODO: Add Logic.
        @Override
        public void onResponse(String response){
            // Here we should also check for the right redirect
            Log.d("SUCC", response);
        }
    };

    public Response.ErrorListener testAPIErrorListener = new Response.ErrorListener(){

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: Add Logic.
            Log.d("ERR",error.toString());
            // If the statusCode is 404 probably the URL is wrong, if 302 maybe https instead of http,
            // if 401 probably the API-Key is wrong.
            Log.d("STATUS", "Error Code:" + error.networkResponse.statusCode);

        }
    };

    public Response.Listener<String> getAPIListener = new Response.Listener<String>()
    {
        // TODO: Add Logic.
        @Override
        public void onResponse(String response){
            // The response is the plain HTML of the /admin page. We need to extract the API-Key
            // from the HTML, therefore we pass it to renderApiKey. After that we test the API-Key.
            String apiKey = helper.renderApiKey(response);
            helper.setApiKey(apiKey);
            saveAnonymousAPIKey();
            //TODO: Next Statement needs to be removed, testing purpose only.
            updateLoadingText(apiKey);
            updateLoadingStatus(100);

        }
    };

    public Response.ErrorListener getAPIErrorListener = new Response.ErrorListener(){

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: Add Logic.

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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
        helper.setServerURL("https://1n.pm");
        // Start of the async Volley request, which invokes the Listeners of this class
        helper.signUp(signupListener, signupErrorListener);
    }


    public void testCustomAPIKey(View view){

        // Now we check the custom input. Therefore We
        // Swap to the loading fragment and save the values to helper

        // Preparing Fragment Transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        SetupFinalStepLoading anon = new SetupFinalStepLoading();
        transaction.replace(R.id.fragment_container, anon);
        transaction.addToBackStack("");
        transaction.commit();

        // If the user decides to add a custom server we first save the data to helper
        // Afterwards we start the async task of testing the credentials.
        EditText urlEdit = (EditText) this.findViewById(R.id.urlEditText);
        EditText apiEdit = (EditText) this.findViewById(R.id.apiEditText);

        String url = urlmanager.guessUrl(urlEdit.getText().toString());

        helper.setServerURL(url);
        helper.setApiKey(apiEdit.getText().toString());

        Log.d("SET", url);
        Log.d("SET", apiEdit.getText().toString());

        helper.testAPI(testAPIListener,testAPIErrorListener);


    }

    public void testAnonymousAPIKey(){
        // TODO: Add logic
    }

    public void saveAnonymousAPIKey(){
        // Finally we can save the APIKey and other information in sharedprefs. Since we only save
        // confidential information like the password with anonymous accounts we call specific
        // voids for each option
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("api_key", helper.getApiKey());
        editor.putString("username", helper.getUsername());

        // Only the anonymous random generated password is being stored
        // unsafely on the device
        editor.putString("password", helper.getPassword());
        editor.putString("url", helper.getServerURL());

        // Here we set the bit which tells the MainActivity whether to start SetupActivity or not
        editor.putBoolean("first_start", true);
        editor.commit();
        Log.d("KEY", helper.getApiKey());
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



}
