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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/*
** First Step of the Setup Assitant. Shows three options: Use 1n.pm, use custom server or
*  keep settings (if != standard of older versions)
 */
public class SetupStep1Fragment extends Fragment {

    public SetupStep1Fragment() {
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
        View myInflater = inflater.inflate(R.layout.fragment_setup_step1, container, false);


        RadioButton keepRadioButton = (RadioButton) myInflater.findViewById(R.id.keepRadio);

        if(getArguments().getBoolean("oldData")){
            keepRadioButton.setEnabled(true);
        }

        final Button next = (Button) myInflater.findViewById(R.id.nextButton);

        RadioGroup radioGroup = (RadioGroup) myInflater.findViewById(R.id.firstSelectionRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.keepRadio){
                    next.setText(getString(R.string.setup_finish));
                } else {
                    next.setText(getString(R.string.setup_next));
                }
            }
        });

        return myInflater;
    }


}
