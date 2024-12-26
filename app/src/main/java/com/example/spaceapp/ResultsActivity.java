package com.example.spaceapp;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ResultsActivity extends AppCompatActivity {
    TextView scoreText, correctText, incorrectText, accuracyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

         scoreText = findViewById(R.id.score_text);
         correctText = findViewById(R.id.correct_text);
         incorrectText = findViewById(R.id.incorrect_text);
         accuracyText = findViewById(R.id.accuracy_text);

        // Get data from intent
        int score = getIntent().getIntExtra("score", 0);
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrectAnswers", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        // Set values
        scoreText.setText("Score: " + score);
        correctText.setText("Correct Answers: " + correctAnswers);
        incorrectText.setText("Incorrect Answers: " + incorrectAnswers);
        accuracyText.setText("Accuracy: " + (correctAnswers * 100 / totalQuestions) + "%");
    }
}