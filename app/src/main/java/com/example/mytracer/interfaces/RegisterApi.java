package com.example.mytracer.interfaces;

import com.example.mytracer.models.RegisterObjectModel;
import com.example.mytracer.models.UserRegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApi {
    @POST("signup/") //with jwt
    //@POST("users/") without jwt

        //on below line we are creating a method to post our data.
    Call<UserRegisterModel> createPost(@Body UserRegisterModel userRegisterModel);
}
