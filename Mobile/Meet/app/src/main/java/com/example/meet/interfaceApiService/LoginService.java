package com.example.meet.interfaceApiService;

import com.example.meet.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/sessionLogin")
    Call<String> createSessionCookie(@Body LoginRequest loginRequest);

    @POST("/profile")
    Call<String> verifySessionCookie();
}
