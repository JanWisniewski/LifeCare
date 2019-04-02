package com.lifecare.main.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lifecare.main.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.loginButton);
        final TextView goToRegisterLink = findViewById(R.id.goToRegisterLink);

        firebaseAuth = FirebaseAuth.getInstance();

        goToRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String finalEmail = email.getText().toString();
                final String finalPassword = password.getText().toString();

                if (finalEmail.isEmpty() || finalPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
                } else {
                    login(finalEmail, finalPassword);
                }
            }
        });
    }

    private void login(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Zalogowano pomyślnie", Toast.LENGTH_LONG).show();
                            goToMainActivity();
                        } else {
                            Toast.makeText(getApplicationContext(), "Błąd logowania " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void goToMainActivity() {
        Intent mainActivity = new Intent(getApplicationContext(), Main.class);
        startActivity(mainActivity);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            goToMainActivity();
        }
    }
}
