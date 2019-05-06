package com.lifecare.main.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lifecare.main.Fragments.ContactsFragment;
import com.lifecare.main.Fragments.DiseasesFragment;
import com.lifecare.main.Fragments.DoctorsFragment;
import com.lifecare.main.Fragments.DrugsFragment;
import com.lifecare.main.Fragments.HomeFragment;
import com.lifecare.main.Fragments.SettingsFragment;
import com.lifecare.main.R;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bundle args;

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

        Intent intent = getIntent();

        args = new Bundle();
        args.putString("disabledInputs", intent.getStringExtra(MainActivity.DISABLED_INPUTS));

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        Fragment fragment = new HomeFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, fragment).commit();

        try {
            String intentFragment = getIntent().getExtras().getString("fragmentName");
            switch (intentFragment) {
                case "contacts":
                    fragment = new ContactsFragment();
                    break;
                case "drugs":
                    fragment = new DrugsFragment();
                    break;
                case "diseases":
                    fragment = new DiseasesFragment();
                    break;
                case "doctors":
                    fragment = new DoctorsFragment();
                    break;
                case "settings":
                    fragment = new SettingsFragment();
                    break;
                default:
                    fragment = new HomeFragment();
                    break;
            }
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new HomeFragment();

        if (id == R.id.nav_contacts) {
            fragment = new ContactsFragment();
        } else if (id == R.id.nav_drugs) {
            fragment = new DrugsFragment();
        } else if (id == R.id.nav_doctors) {
            fragment = new DoctorsFragment();
        } else if (id == R.id.nav_diseases) {
            fragment = new DiseasesFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, fragment).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
