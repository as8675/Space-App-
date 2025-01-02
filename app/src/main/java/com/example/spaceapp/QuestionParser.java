package com.example.spaceapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionParser {
    private static final String TAG = "QuestionParser";

    public static List<Question> parseQuestionsFromJSON(String jsonResponse) {
        List<Question> questions = new ArrayList<>();
        try {
            // First parse the response as a JSON string
            JSONArray questionsArray = new JSONArray(jsonResponse);

            // Iterate through the JSON array
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObj = questionsArray.getJSONObject(i);
                String questionText = questionObj.getString("question");

                // Extract options
                JSONArray optionsArray = questionObj.getJSONArray("options");
                String[] options = new String[optionsArray.length()];
                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }

                // Extract the correct answer index
                int correctAnswer = questionObj.getInt("correctAnswer");

                // Add the parsed question to the list
                questions.add(new Question(questionText, options, correctAnswer));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }
        return questions;
    }
}
