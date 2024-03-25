package com.example.meet.interfaceApiService;

import com.example.meet.enumCustom.RegistrationStatus;
import com.example.meet.model.AccountUserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("/register")
    Call<RegistrationStatus> registerAccount(@Body AccountUserModel accountUser);

    @POST("/register/update")
    Call<RegistrationStatus> updateAccount(@Body AccountUserModel accountUser);
}
