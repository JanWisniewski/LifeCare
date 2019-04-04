package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lifecare.main.Fragments.ContactsFragment;
import com.lifecare.main.Fragments.DiseasesFragment;
import com.lifecare.main.Fragments.DoctorsFragment;
import com.lifecare.main.Fragments.DrugsFragment;
import com.lifecare.main.R;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            int intentFragment = getIntent().getExtras().getInt("fragmentName");
            switch (intentFragment) {
                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new ContactsFragment()).commit();
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DrugsFragment()).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DiseasesFragment()).commit();
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DoctorsFragment()).commit();
                    break;
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new ContactsFragment()).commit();
        } else if (id == R.id.nav_drugs) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DrugsFragment()).commit();
        } else if (id == R.id.nav_doctors) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DoctorsFragment()).commit();
        } else if (id == R.id.nav_diseases) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, new DiseasesFragment()).commit();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Toast.makeText(getApplicationContext(), "Wylogowano pomyślnie", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
