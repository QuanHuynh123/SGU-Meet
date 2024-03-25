package com.example.meet.configApi;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  ApiConfig {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}