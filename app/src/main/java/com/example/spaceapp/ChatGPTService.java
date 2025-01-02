package com.example.spaceapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChatGPTService {
    @POST("v1/chat/completions")
    Call<ChatGPTResponse> getQuestions(
            @Header("Authorization") String authorization,
            @Body JsonObject body
    );
}
