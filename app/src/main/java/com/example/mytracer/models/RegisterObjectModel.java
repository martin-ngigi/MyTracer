package com.example.mytracer.models;

public class RegisterObjectModel {
    private UserRegisterModel registerModel;
    private String message;

    public RegisterObjectModel() {
    }

    public RegisterObjectModel(UserRegisterModel registerModel, String message) {
        this.registerModel = registerModel;
        this.message = message;
    }

    public UserRegisterModel getRegisterModel() {
        return registerModel;
    }

    public void setRegisterModel(UserRegisterModel registerModel) {
        this.registerModel = registerModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
