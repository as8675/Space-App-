package com.ayush.spacequiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ayush.spacequiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

                // Clear rememberMe flag
                SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("rememberMe");
                editor.apply();

                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}