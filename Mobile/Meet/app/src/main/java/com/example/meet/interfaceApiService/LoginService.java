package com.example.meet.interfaceApiService;

import com.example.meet.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/sessionLogin")
    Call<LoginRequest> createSessionCookie(@Body LoginRequest loginRequest);

    @POST("/verifyCookie")
    Call<Boolean> verifySessionCookie(@Header("cookie") String sessionCookieValue);

    @POST("/sessionLogout")
    Call<Boolean> clearSessionCookieAndRevoke(@Header("cookie") String sessionCookieValue);
}
