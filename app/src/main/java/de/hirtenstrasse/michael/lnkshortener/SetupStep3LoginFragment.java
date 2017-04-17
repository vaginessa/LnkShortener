package de.hirtenstrasse.michael.lnkshortener;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupStep3LoginFragment extends Fragment {


    public SetupStep3LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflater = inflater.inflate(R.layout.fragment_setup_step3_login, container, false);

        // Watch the Login / Signup ToggleButton and show the E-Mail field / reset Password field accordingly.

        // First we define the variables for accessing the UI

        final ToggleButton loginToggle =  (ToggleButton) myInflater.findViewById(R.id.loginToggleButton);
        final EditText emailInput = (EditText) myInflater.findViewById(R.id.emailEditText);
        final TextView emailLabel = (TextView) myInflater.findViewById(R.id.emailLabel);
        final Button resetPasswordButton = (Button) myInflater.findViewById(R.id.resetPasswordButton);
        final Button loginButton = (Button) myInflater.findViewById(R.id.loginButton);
        final TextView titleText = (TextView) myInflater.findViewById(R.id.loginTitleTextView);

        // Now we add the ClickListener on the toggle button
        loginToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if (!loginToggle.isChecked()) {
                    // Default state: Login mask, E-Mail field hidden, Reset Password field shown,
                    // Login button states "Login".
                    emailInput.setVisibility(View.GONE);
                    emailLabel.setVisibility(View.GONE);

                    resetPasswordButton.setVisibility(View.VISIBLE);
                    loginButton.setText(getString(R.string.setup_login));
                    titleText.setText(getString(R.string.setup_login_title));



                } else {
                    // If loginToggle.isChecked() == true it means that the Fragment should be in the
                    // Signup-State. Therefore we need to show the E-Mail field and hide Reset-Password
                    // button as well as rename Login button

                    emailInput.setVisibility(View.VISIBLE);
                    emailLabel.setVisibility(View.VISIBLE);

                    resetPasswordButton.setVisibility(View.INVISIBLE);
                    loginButton.setText(getString(R.string.setup_signup));
                    titleText.setText(getString(R.string.setup_signup_title));

                }

            }
        });


        return myInflater;




    }

}
