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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;



/**
 * Step 3 in the Setup Assistant. Shows login options for 1n.pm
 * The Layout & Fragment Class already include the logic for signing
 * up from within the app. Since this would be quite a dirty
 * wrapper we are awaiting some API-Endpoint for the moment.
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


        // Listener for enabling the login button

        final EditText usernameEdit = (EditText) myInflater.findViewById(R.id.usernameEditText);
        final EditText passwordEdit = (EditText) myInflater.findViewById(R.id.passwordEditText);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!usernameEdit.getText().toString().matches("") && !passwordEdit.getText().toString().matches("")){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!usernameEdit.getText().toString().matches("") && !passwordEdit.getText().toString().matches("")){
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return myInflater;




    }

}
