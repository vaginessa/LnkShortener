package de.hirtenstrasse.michael.lnkshortener;


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
