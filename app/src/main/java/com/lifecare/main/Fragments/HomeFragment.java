package com.lifecare.main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lifecare.main.Activities.Main;
import com.lifecare.main.R;

public class HomeFragment extends Fragment {


    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getActivity().setTitle(R.string.home);

        Button userBtn = view.findViewById(R.id.userBtn);
        Button contactsBtn = view.findViewById(R.id.contactBtn);
        Button diseaseBtn = view.findViewById(R.id.diseaseBtn);
        Button doctorBtn = view.findViewById(R.id.doctorBtn);
        Button drugBtn = view.findViewById(R.id.drugsBtn);
        Button settingsBtn = view.findViewById(R.id.settingsBtn);

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "users");
                startActivity(intent);
            }
        });

        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "contacts");
                startActivity(intent);
            }
        });

        diseaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "diseases");
                startActivity(intent);
            }
        });

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "doctors");
                startActivity(intent);
            }
        });

        drugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "drugs");
                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "settings");
                startActivity(intent);
            }
        });

        return view;
    }
}
