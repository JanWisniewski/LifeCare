package com.lifecare.main.Fragments.Blocked;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.lifecare.main.Activities.Main;
import com.lifecare.main.App;
import com.lifecare.main.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragmentBlocked extends Fragment {

    public SettingsFragmentBlocked() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle(R.string.settings);
        loadLocale();

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySettings", MODE_PRIVATE);

        final CheckBox checkBox = view.findViewById(R.id.notificationCheckBox);

        checkBox.setChecked(App.NOTIFICATION_DISPLAYED);

        final Spinner spinner = view.findViewById(R.id.languageSpinner);
        String[] languagesArray = getResources().getStringArray(R.array.languagesArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_item, languagesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Locale current = getResources().getConfiguration().locale;
        switch (current.toLanguageTag()) {
            case "en":
                spinner.setSelection(0);
                break;
            case "es":
                spinner.setSelection(1);
                break;
            case "pl":
                spinner.setSelection(2);
                break;
        }

        final Button saveSettingsBtn = view.findViewById(R.id.saveSettingsBtn);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                saveSettingsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        App.NOTIFICATION_DISPLAYED = checkBox.isChecked();

                        if (i == 0) {
                            setLocale("en");
                            getActivity().recreate();
                            spinner.setSelection(0);
                            Toast.makeText(getContext(), R.string.savedSettings, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), Main.class));
                        }
                        if (i == 1) {
                            setLocale("es");
                            getActivity().recreate();
                            spinner.setSelection(1);
                            Toast.makeText(getContext(), R.string.savedSettings, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), Main.class));
                        }
                        if (i == 2) {
                            setLocale("pl");
                            getActivity().recreate();
                            Toast.makeText(getContext(), R.string.savedSettings, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), Main.class));
                            spinner.setSelection(2);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (App.NOTIFICATION_DISPLAYED) {
            App.notificationDisplay();
        } else {
            App.notificationNotDisplay();
        }
        return view;
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(configuration, getActivity().getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My language", language);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        String language = sharedPreferences.getString("My language", "");
        setLocale(language);
    }
}
