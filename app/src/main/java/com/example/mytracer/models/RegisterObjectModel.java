package com.example.mytracer.models;

public class RegisterObjectModel {
    private UserModel registerModel;
    private String message;

    public RegisterObjectModel() {
    }

    public RegisterObjectModel(UserModel registerModel, String message) {
        this.registerModel = registerModel;
        this.message = message;
    }

    public UserModel getRegisterModel() {
        return registerModel;
    }

    public void setRegisterModel(UserModel registerModel) {
        this.registerModel = registerModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
