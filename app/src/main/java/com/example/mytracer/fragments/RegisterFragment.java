package com.example.mytracer.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mytracer.R;
import com.example.mytracer.activities.HomeActivity;

public class RegisterFragment extends Fragment {

    private Button btn_register;


    public RegisterFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        btn_register =view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener( e-> {
            startActivity(new Intent(getContext(), HomeActivity.class));
        });

        return view;
    }
}