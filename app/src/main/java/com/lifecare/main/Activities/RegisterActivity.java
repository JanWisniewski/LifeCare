package com.lifecare.main.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lifecare.main.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText password, email;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        final Button registerButton = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String finalPassword = password.getText().toString();
                final String finalEmail = email.getText().toString();

                if (finalPassword.isEmpty() || finalEmail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
                } else {
                    createAccount(finalEmail, finalPassword);
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Utworzono konto", Toast.LENGTH_LONG).show();
                            backToLogin();
                        } else {
                            Toast.makeText(getApplicationContext(), "Nieudana próba utworzenia konta" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void backToLogin() {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }
}
