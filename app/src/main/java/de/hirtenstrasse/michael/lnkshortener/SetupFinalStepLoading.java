package de.hirtenstrasse.michael.lnkshortener;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SetupFinalStepLoading extends Fragment {

    TextView statusText;
    ProgressBar spinner;

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
        View myInflater = inflater.inflate(R.layout.fragment_setup_step3_anonymous, container, false);

        //  Assign variables for use in voids
        spinner = (ProgressBar) myInflater.findViewById(R.id.statusSpinner);
        statusText = (TextView) myInflater.findViewById(R.id.textStatus);

        spinner.setMax(100);
        spinner.setProgress(10);

        return myInflater;
    }

    public void updateStatus(String text){


        statusText.setText(text);
    }

    public void updateStatusPercent(int status){

        if(status > 0 && status <= 100){

            spinner.setProgress(status);

        } else {
            // TODO: Add error-logic
        }

    }

}
