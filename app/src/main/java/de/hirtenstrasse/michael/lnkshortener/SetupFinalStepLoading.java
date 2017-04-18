package de.hirtenstrasse.michael.lnkshortener;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SetupFinalStepLoading extends Fragment {


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
        
        return myInflater;
    }

}
