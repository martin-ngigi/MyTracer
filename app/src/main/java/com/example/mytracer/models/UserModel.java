package com.example.mytracer.models;

/**
 * {
 *     "email": "martinwainaina@gmail.com",
 *     "username": "martinwainaina",
 *     "phone": "0797292290",
 *     "first_name": "Martin",
 *     "last_name": "Wainaina",
 *     "password": "12345678",
 *     "backup_phone":"0712345678"
 * }
 */
public class UserModel {
    private String email, username, phone, first_name, last_name, password, backup_phone;
    private int id;
    public UserModel() {
    }

    public UserModel(String email, String username, String phone, String first_name, String last_name, String password, String backup_phone) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.backup_phone = backup_phone;
    }

    //so as to get ID from the response
    public UserModel(String email, String username, String phone, String first_name, String last_name, String password, String backup_phone, int id) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.backup_phone = backup_phone;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBackup_phone() {
        return backup_phone;
    }

    public void setBackup_phone(String backup_phone) {
        this.backup_phone = backup_phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
