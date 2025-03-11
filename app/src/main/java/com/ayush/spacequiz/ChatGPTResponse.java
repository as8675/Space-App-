package com.ayush.spacequiz;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChatGPTResponse {
    @SerializedName("choices")
    private List<Choice> choices;

    public static class Choice {
        @SerializedName("message")
        private Message message;
    }

    public static class Message {
        @SerializedName("content")
        private String content;
    }

    public String getContent() {
        if (choices != null && !choices.isEmpty() && choices.get(0).message != null) {
            String content = choices.get(0).message.content;
            return content.trim();
        }
        System.err.println("Error: ChatGPT response is empty or malformed!");
        return null;
    }
}
