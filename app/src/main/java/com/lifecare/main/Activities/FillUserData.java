package com.lifecare.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lifecare.main.Models.UserData;
import com.lifecare.main.R;

public class FillUserData extends AppCompatActivity {

    EditText nameET;
    EditText surnameET;
    Spinner spinnerSex;
    String id;

    DatabaseReference dbUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_user_data);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.userData);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nameET = findViewById(R.id.nameET);
        surnameET = findViewById(R.id.surnameET);
        spinnerSex = findViewById(R.id.spinnerSex);
        String[] sexArray = getResources().getStringArray(R.array.sexArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, sexArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);

        dbUserData = FirebaseDatabase.getInstance().getReference("UserData");

        DatabaseReference reference = dbUserData.child(id);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("sexID")) {
                    nameET.setText(dataSnapshot.child("name").getValue(String.class));
                    surnameET.setText(dataSnapshot.child("surname").getValue(String.class));
                    spinnerSex.setSelection(dataSnapshot.child("sexID").getValue(Integer.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            onClickSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickSave() {
        String name = nameET.getText().toString();
        String surname = surnameET.getText().toString();
        Integer sex = spinnerSex.getSelectedItemPosition();

        String id = this.id;

        UserData userData = new UserData(id, name, surname, sex);

        dbUserData.child(id).setValue(userData).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.updatedUserData, Toast.LENGTH_LONG).show();
                    backToList();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backToList() {
        Intent fragment = new Intent(getApplicationContext(), Main.class);
        fragment.putExtra("fragmentName", "");
        startActivity(fragment);
        finish();
    }
}
