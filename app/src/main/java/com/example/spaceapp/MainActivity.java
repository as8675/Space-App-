package com.example.spaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find each CardView
        CardView cardExploration = findViewById(R.id.card_exploration);
        CardView cardExplore = findViewById(R.id.card_explore);
        CardView cardSolar = findViewById(R.id.card_solar);
        CardView cardAstronaut = findViewById(R.id.card_astronaut);

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
    }
}