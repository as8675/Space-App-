package com.example.spaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AstronautsActivity extends AppCompatActivity {
    private RadioGroup options;
    private List<Question> questionsList;
    private int currentQuestionIndex = 0;
    private TextView timerText, questionText, progressText;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private int score = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astronauts);

        timerText = findViewById(R.id.timer_text);
        questionText = findViewById(R.id.question_text);
        options = findViewById(R.id.options_radio_group);
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);

        loadQuestions();

        if (progressBar != null)
            progressBar.setMax(questionsList.size());

        displayQuestion();
        startTimer();
    }

    protected void loadQuestions() {
        questionsList = new ArrayList<>();

        // Add sample questions
        questionsList.add(new Question("What is the largest planet in our solar system?",
                new String[]{"Earth", "Mars", "Jupiter", "Venus"}, 2));
        questionsList.add(new Question("Who was the first man on the Moon?",
                new String[]{"Neil Armstrong", "Buzz Aldrin", "Yuri Gagarin", "Alan Shepard"}, 0));
        questionsList.add(new Question("What is the closest star to Earth?",
                new String[]{"Sirius", "Alpha Centauri", "Proxima Centauri", "The Sun"}, 3));
    }

    private void startTimer() {
        long duration = TimeUnit.SECONDS.toMillis(30);  // 30 seconds

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timerText.setText(text);
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                nextQuestion();
            }
        };

        timer.start();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionsList.size()) {
            Question currentQuestion = questionsList.get(currentQuestionIndex);
            questionText.setText(currentQuestion.getQuestionText());

            options.removeAllViews();
            for (int i = 0; i < currentQuestion.getOptions().length; i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(currentQuestion.getOptions()[i]);
                radioButton.setId(i);

                // Set on click listener for each radio button
                radioButton.setOnClickListener(v -> {
                    int selectedId = options.getCheckedRadioButtonId();
                    int correctAnswer = currentQuestion.getCorrectAnswerIndex();

                    // Reset colors for all options
                    resetOptionsColors();

                    if (selectedId == correctAnswer) {
                        radioButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                    } else {
                        radioButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        RadioButton correctButton = options.findViewById(correctAnswer);
                        if (correctButton != null) {
                            correctButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        }
                    }

                    // Optionally move to the next question
                    nextQuestion();
                });

                options.addView(radioButton);
            }
            updateProgress();
            startTimer();  // Start the timer as soon as the question is displayed
        } else {
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void resetOptionsColors() {
        for (int i = 0; i < options.getChildCount(); i++) {
            RadioButton rb = (RadioButton) options.getChildAt(i);
            rb.setTextColor(getResources().getColor(android.R.color.black)); // Set to default text color
        }
    }

    private void nextQuestion() {
        int selectedOption = options.getCheckedRadioButtonId();
        if (selectedOption != -1)
        {
            Question currentQuestion = questionsList.get(currentQuestionIndex);
            if (selectedOption == currentQuestion.getCorrectAnswerIndex()) {
                score += 10;
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }
        } else {
            // If no option is selected, treat as incorrect
            incorrectAnswers++;
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questionsList.size()) {
            new Handler().postDelayed(this::displayQuestion, 2000);
        } else {
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_SHORT).show();
            displayFinalScore();
        }
    }

    private void updateProgress() {
        if (progressBar != null) {
            progressBar.setProgress(currentQuestionIndex + 1);
        }
        if (progressText != null) {
            progressText.setText("Question " + (currentQuestionIndex + 1) + " of " + questionsList.size());
        }
    }

    private void displayFinalScore() {
        // Stop the timer if it's running
        if (timer != null) {
            timer.cancel();
        }

        // Create a summary message
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        intent.putExtra("totalQuestions", questionsList.size());
        startActivity(intent);
        finish();
    }

}
