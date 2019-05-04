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

import static com.lifecare.main.Activities.MainActivity.DISABLED_INPUTS;

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

        final Bundle args = getArguments();

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
                intent.putExtra(DISABLED_INPUTS, args.getString("disabledInputs"));
                startActivity(intent);
            }
        });

        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "contacts");
                intent.putExtra(DISABLED_INPUTS, args.getString("disabledInputs"));
                startActivity(intent);
            }
        });

        diseaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "diseases");
                intent.putExtra(DISABLED_INPUTS, args.getString("disabledInputs"));
                startActivity(intent);
            }
        });

        doctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "doctors");
                intent.putExtra(DISABLED_INPUTS, args.getString("disabledInputs"));
                startActivity(intent);
            }
        });

        drugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "drugs");
                intent.putExtra(DISABLED_INPUTS, args.getString("disabledInputs"));
                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Main.class);
                intent.putExtra("fragmentName", "settings");
                intent.putExtra(DISABLED_INPUTS, args.getString("disabledInputs"));
                startActivity(intent);
            }
        });

        return view;
    }
}
