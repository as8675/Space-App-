package com.ayush.spacequiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ayush.spacequiz.BuildConfig;
import com.ayush.spacequiz.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstronautsActivity extends AppCompatActivity {
    private static final String TAG = "AstronautsActivity";
    private static final String API_KEY = BuildConfig.API_KEY;
    private RadioGroup options;
    private TextView timerText, questionText, progressText;
    private ProgressBar progressBar, progressSpinner;
    private CountDownTimer timer;
    private List<Question> questionsList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private List<Question> previousQuestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astronauts);

        // Initialize views
        timerText = findViewById(R.id.timer_text);
        questionText = findViewById(R.id.question_text);
        options = findViewById(R.id.options_radio_group);
        progressBar = findViewById(R.id.progress_bar);
        progressSpinner = findViewById(R.id.progressSpinner);
        progressText = findViewById(R.id.progress_text);

        // Reset all state
        resetQuizState();

        // Load questions dynamically
        loadQuestionsFromChatGPT();
    }

    private void resetQuizState() {
        previousQuestions.clear(); // Clear previously asked questions
        questionsList = new ArrayList<>();
        currentQuestionIndex = 0;
        score = 0;
        correctAnswers = 0;
        incorrectAnswers = 0;
    }

    private void loadQuestionsFromChatGPT() {
        showLoadingIndicator();

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        JsonArray messages = new JsonArray();
        Log.d("API_KEY_DEBUG", "API Key: " + BuildConfig.API_KEY);

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content",
                "Generate 5 multiple choice questions about " + getRandomTopic() + ". " +
                        "Ensure the questions are creative and unique compared to the following: " +
                        getPastQuestionsSummary(previousQuestions) + "Seed: " + System.currentTimeMillis() + ". " +
                        "Format as a JSON array with each question having: " +
                        "question (string), options (array of 4 strings), and correctAnswer (integer 0-3).");
        messages.add(message);

        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 1.1);

        ChatGPTService service = ChatGPTClient.getClient();
        Call<ChatGPTResponse> call = service.getQuestions(API_KEY, requestBody);

        call.enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(Call<ChatGPTResponse> call, Response<ChatGPTResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String content = response.body().getContent();

                        questionsList = QuestionParser.parseQuestionsFromJSON(content);

                        if (questionsList != null && !questionsList.isEmpty()) {
                            runOnUiThread(() -> {
                                    hideLoadingIndicator();
                                    setupQuizAfterQuestionsLoaded();
                            });
                        } else {
                            handleError("Parsed questions are empty. Check the response format.");
                        }
                    } catch (Exception e) {
                        handleError("Error parsing response: " + e.getMessage());
                    }
                } else {
                    handleError("API response error: " + response.code() + "API KEY: "+ API_KEY);
                }
            }

            @Override
            public void onFailure(Call<ChatGPTResponse> call, Throwable t) {
                hideLoadingIndicator();
                handleError("Network error: " + t.getMessage());
            }
        });
    }

    private String getRandomTopic() {
        String[] topics = {
                "famous astronauts and their achievements",
                "first missions by astronauts",
                "life of astronauts in space",
                "training and selection of astronauts",
                "women astronauts and their contributions"
        };
        int randomIndex = new java.util.Random().nextInt(topics.length);
        return topics[randomIndex];
    }

    private String getPastQuestionsSummary(List<Question> pastQuestions) {
        StringBuilder summary = new StringBuilder();
        for (Question question : pastQuestions) {
            summary.append("Question: ").append(question.getQuestionText()).append(" | ");
        }
        return summary.toString();
    }

    private void setupQuizAfterQuestionsLoaded() {
        if (questionsList != null && !questionsList.isEmpty()) {
            progressBar.setMax(questionsList.size());
            displayQuestion();
            startTimer();
        } else {
            Toast.makeText(this, "No questions available.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionsList.size()) {
            Question currentQuestion = questionsList.get(currentQuestionIndex);
            questionText.setText(currentQuestion.getQuestionText());
            previousQuestions.add(currentQuestion);

            options.removeAllViews();
            for (int i = 0; i < currentQuestion.getOptions().length; i++) {
                RadioButton radioButton = new RadioButton(this);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                );
                radioButton.setLayoutParams(params);
                radioButton.setBackground(getResources().getDrawable(R.drawable.radio_button_selector));
                radioButton.setPadding(30, 30, 30, 30);
                radioButton.setTextSize(16);
                radioButton.setText(currentQuestion.getOptions()[i]);
                radioButton.setId(i);

                radioButton.setOnClickListener(v -> {
                    int selectedId = options.getCheckedRadioButtonId();
                    int correctAnswer = currentQuestion.getCorrectAnswerIndex();
                    applyAnimation(radioButton);

                    resetOptionsColors();
                    if (selectedId == correctAnswer) {
                        radioButton.setBackground(getResources().getDrawable(R.drawable.button_correct));
                        radioButton.setTextColor(getResources().getColor(android.R.color.white));
                        score += 10;
                        correctAnswers++;
                        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                    } else {
                        radioButton.setBackground(getResources().getDrawable(R.drawable.button_wrong));
                        radioButton.setTextColor(getResources().getColor(android.R.color.white));

                        RadioButton correctButton = options.findViewById(correctAnswer);
                        if (correctButton != null) {
                            correctButton.setBackground(getResources().getDrawable(R.drawable.button_correct));
                            correctButton.setTextColor(getResources().getColor(android.R.color.white));
                            applyAnimation(correctButton);
                        }
                        incorrectAnswers++;
                    }

                    nextQuestion(); // Move to the next question
                });

                options.addView(radioButton);
            }

            updateProgress();
            startTimer(); // Start the timer for the current question
        } else {
            displayFinalScore(); // End the quiz
        }
    }


    private void nextQuestion() {
        if (timer != null) timer.cancel(); // Cancel the current question's timer

        currentQuestionIndex++;
        if (currentQuestionIndex <= questionsList.size()) {
            new Handler().postDelayed(this::displayQuestion, 1500); // Delay before showing the next question
        } else {
            displayFinalScore(); // End the quiz
        }
    }

    private void updateProgress() {
        progressBar.setProgress(currentQuestionIndex + 1);
        progressText.setText("Question " + (currentQuestionIndex + 1) + " of " + questionsList.size());
    }

    private void displayFinalScore() {
        if (timer != null) timer.cancel();

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        intent.putExtra("totalQuestions", questionsList.size());
        intent.putExtra("activityClass", AstronautsActivity.class.getSimpleName());
        startActivity(intent);
        finish();
    }

    private void showLoadingIndicator() {
        progressSpinner.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        questionText.setText("Loading questions...");
    }

    private void hideLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
        progressSpinner.setVisibility(View.GONE);
    }

    private void handleError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();

        if (questionsList == null || questionsList.isEmpty()) {
            loadBackupQuestions();
            setupQuizAfterQuestionsLoaded();
        }
    }

    private void loadBackupQuestions() {
        questionsList = new ArrayList<>();
        questionsList.add(new Question("What is the largest planet?", new String[]{"Earth", "Mars", "Jupiter", "Venus"}, 2));
        questionsList.add(new Question("Who was the first man on the Moon?", new String[]{"Neil Armstrong", "Buzz Aldrin", "Yuri Gagarin", "Alan Shepard"}, 0));
    }

    private void startTimer() {
        if (timer != null) timer.cancel(); // Cancel any existing timer

        timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(30), 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                incorrectAnswers++; // Treat as incorrect if time runs out
                nextQuestion();
            }
        }.start();
    }

    private void resetOptionsColors() {
        for (int i = 0; i < options.getChildCount(); i++) {
            RadioButton rb = (RadioButton) options.getChildAt(i);
            rb.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    private void applyAnimation(View view) {
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        view.startAnimation(fadeIn);
    }
}