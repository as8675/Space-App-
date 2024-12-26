package com.example.spaceapp;

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

public class SpaceMissionsActivity extends AppCompatActivity {

    private RadioGroup options;
    private List<Question> questionsList;
    private int currentQuestionIndex = 0;
    private TextView timerText, questionText, progressText;
    private ProgressBar progressBar;
    private CountDownTimer timer;

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
        questionsList.add(new Question("Which was the first successful manned mission to the Moon?",
                new String[]{"Apollo 11", "Apollo 13", "Gemini 3", "Mercury-Atlas 6"}, 0));
        questionsList.add(new Question("What is the primary goal of the Mars Rover missions?",
                new String[]{"To search for water on Mars", "To bring Martian soil back to Earth", "To colonize Mars", "To study the Sun"}, 0));
        questionsList.add(new Question("Which space telescope has provided astronomers with many deep-space images since its launch in 1990?",
                new String[]{"Chandra X-ray Observatory", "Spitzer Space Telescope", "Hubble Space Telescope", "James Webb Space Telescope"}, 2));
        questionsList.add(new Question("What was the main purpose of the Voyager missions?",
                new String[]{"To explore the Moon", "To explore and study the outer planets of our solar system", "To study the Sun", "To search for exoplanets"}, 1));
        questionsList.add(new Question("Which country launched the first artificial satellite, Sputnik 1?",
                new String[]{"United States", "Soviet Union", "China", "France"}, 1));
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
        currentQuestionIndex++;
        if (currentQuestionIndex < questionsList.size()) {
            new Handler().postDelayed(this::displayQuestion, 2000);
//            displayQuestion();
        } else {
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_SHORT).show();
            finish();
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

}