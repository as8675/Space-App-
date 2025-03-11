package com.ayush.spacequiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.ayush.spacequiz.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    TextView loginText;
    EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextName;
    ImageView buttonReg;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginText = findViewById(R.id.signup);
        editTextEmail = findViewById(R.id.editText_Email);
        editTextPassword = findViewById(R.id.editText_Password);
        editTextConfirmPassword = findViewById(R.id.editText_ConfirmPassword);
        editTextName = findViewById(R.id.editText_Name);
        buttonReg = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();

        buttonReg.setOnClickListener(v -> {
            String email, password, confirmPassword, username;
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            confirmPassword = editTextConfirmPassword.getText().toString().trim();
            username = editTextName.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignUp.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("rememberMe", true);
            editor.apply();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Registration successful, go to Login screen
                            Toast.makeText(SignUp.this, username + " registered!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Registration failed
                            Toast.makeText(SignUp.this, "Authentication failed: " +
                                            (task.getException() != null ? task.getException().getMessage() : ""),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });
    }
}
