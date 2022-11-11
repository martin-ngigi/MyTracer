package com.example.mytracer.interfaces;

import com.example.mytracer.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApi {
    /**
    LINKS:
     API WITH JWT: https://github.com/martin-ngigi/MyTracer-JWT-Django-API
     API WITHOUT JWT: https://github.com/martin-ngigi/MyTracer-Django-API
     */

    //full jwt register url
    //full register url without jwt

    @POST("signup/") //with jwt
    //@POST("users/") without jwt

        //on below line we are creating a method to post our data.
    Call<UserModel> createPost(@Body UserModel userRegisterModel);
}
