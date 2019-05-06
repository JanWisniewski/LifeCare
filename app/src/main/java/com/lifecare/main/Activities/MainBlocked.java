package com.lifecare.main.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.lifecare.main.Fragments.Blocked.ContactsFragmentBlocked;
import com.lifecare.main.Fragments.Blocked.DiseasesFragmentBlocked;
import com.lifecare.main.Fragments.Blocked.DoctorsFragmentBlocked;
import com.lifecare.main.Fragments.Blocked.DrugsFragmentBlocked;
import com.lifecare.main.Fragments.Blocked.HomeFragmentBlocked;
import com.lifecare.main.Fragments.Blocked.SettingsFragmentBlocked;
import com.lifecare.main.R;

public class MainBlocked extends AppCompatActivity {

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new HomeFragmentBlocked()).commit();

        try {
            String intentFragment = getIntent().getExtras().getString("fragmentName");
            switch (intentFragment) {
                case "contacts":
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new ContactsFragmentBlocked()).commit();
                    break;
                case "drugs":
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DrugsFragmentBlocked()).commit();
                    break;
                case "diseases":
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DiseasesFragmentBlocked()).commit();
                    break;
                case "doctors":
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DoctorsFragmentBlocked()).commit();
                    break;
                case "settings":
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new SettingsFragmentBlocked()).commit();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new HomeFragmentBlocked()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
