package com.example.meet.interfaceApiService;

import com.example.meet.enumCustom.RegistrationStatus;
import com.example.meet.model.AccountUserModel;
import com.example.meet.model.LoginRequest;
import com.example.meet.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ProfileService {

    @GET("/profile")
    Call<UserModel> getProfileUser(@Query("userId") String userId);

    @PUT("/updateProfile")
    Call<RegistrationStatus> updateProfileUser(@Body UserModel userModel);
}
