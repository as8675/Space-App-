package com.example.spaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        // Find each CardView
        CardView cardExploration = findViewById(R.id.card_exploration);
        CardView cardExplore = findViewById(R.id.card_explore);
        CardView cardSolar = findViewById(R.id.card_solar);
        CardView cardAstronaut = findViewById(R.id.card_astronaut);
        logout_btn = findViewById(R.id.logout_button);
        user = mAuth.getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        cardExploration.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, SpaceMissionsActivity.class);
            startActivity(intent);
        });

        cardExplore.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, SpaceExplorationActivity.class);
            startActivity(intent);
        });

        cardSolar.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, SolarSystemActivity.class);
            startActivity(intent);
        });

        cardAstronaut.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, AstronautsActivity.class);
            startActivity(intent);
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}