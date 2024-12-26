package com.example.spaceapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
         Button mainMenuButton = findViewById(R.id.main_menu_button);
         Button retakeQuizButton = findViewById(R.id.retake_quiz_button);
         Button shareResultsButton = findViewById(R.id.share_results_button);
         String appName = getString(R.string.app_name);


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

        // Navigate back to the main menu
        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        retakeQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, AstronautsActivity.class); // Change to the specific quiz activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        shareResultsButton.setOnClickListener(v -> {
            String shareText = "I scored " + score + " in the Space Quiz!\n" +
                    "Correct Answers: " + correctAnswers + "\n" +
                    "Incorrect Answers: " + incorrectAnswers + "\n" +
                    "Accuracy: " + (correctAnswers * 100 / totalQuestions) + "%\n" +
                    "Think you can beat my score? Download " + appName + " now!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share your quiz results"));
        });

    }
}