package com.example.spaceapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

public class SolarSystemActivity extends AppCompatActivity {

    private RadioGroup options;
    private List<Question> questionsList;
    private int currentQuestionIndex = 0;
    private TextView timerText, questionText;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astronauts);

        timerText = findViewById(R.id.timer_text);
        questionText = findViewById(R.id.question_text);
        options = findViewById(R.id.options_radio_group); // Make sure this ID is in your layout

        loadQuestions();
        displayQuestion();
        startTimer();
    }

    protected void loadQuestions() {
        questionsList = new ArrayList<>();
        questionsList.add(new Question("What is the largest planet in our solar system?",
                new String[]{"Earth", "Jupiter", "Saturn", "Mars"}, 1));
        questionsList.add(new Question("Which planet is known as the \"Red Planet\"?",
                new String[]{"Mercury", "Earth", "Mars", "Venus"}, 2));
        questionsList.add(new Question("What is the main component of the Sun?",
                new String[]{"Liquid Lava", "Hydrogen", "Oxygen", "Water Vapor"}, 1));
        questionsList.add(new Question("Which planet has the most moons?",
                new String[]{"Earth", "Neptune", "Jupiter", "Mars"}, 2));
        questionsList.add(new Question("What is the asteroid belt?",
                new String[]{"A region of space beyond Neptune filled with ice and gas",
                        "The layer of the Sun's atmosphere just above the surface",
                        "A region between Mars and Jupiter filled with rocky bodies",
                        "A theoretical area where Earth's missing twin planet should be"}, 2));
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
}