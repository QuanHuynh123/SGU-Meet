package com.example.meet.interfaceApiService;

import com.example.meet.enumCustom.RegistrationStatus;
import com.example.meet.model.AccountUserModel;
import com.example.meet.model.LoginRequest;
import com.example.meet.model.UserModel;

import java.util.List;

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

    @GET("/getFriend")
    Call<List<UserModel>> getFriend(@Query("idFriend") List<String> idFriend);

    @POST("/addFriend")
    Call<Boolean> addFriend(@Query("userId1") String userId1, @Query("userId2") String userId2);

    @POST("/requestAddFriend")
    Call<Boolean> requestAddFriend(@Query("userId1") String userId1, @Query("userId2") String userId2);
}
