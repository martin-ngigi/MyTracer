package com.example.mytracer.retrofit_examples;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytracer.Constants;
import com.example.mytracer.R;
import com.example.mytracer.interfaces.RegisterApi;
import com.example.mytracer.models.UserRegisterModel;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register;

    private EditText et_first_name, et_last_name, et_phone, et_phone_backUp, et_email, et_password, et_repassword;

    private String fname, lname, phone, phone_backUp, email, password, repassword;

    private TextView successMessageTv, tv_subtitle;

    private RelativeLayout registerRlayout;
    private LinearLayout successLLayout;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_phone = findViewById(R.id.et_phone);
        et_phone_backUp = findViewById(R.id.et_phone_backUp);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_repassword = findViewById(R.id.et_repassword);
        btn_register =findViewById(R.id.btn_register);
        successMessageTv=findViewById(R.id.successMessageTv);
        registerRlayout=findViewById(R.id.registerRlayout);
        successLLayout = findViewById(R.id.successLLayout);
        tv_subtitle = findViewById(R.id.tv_subtitle);

        //spots dialog
        dialog = new SpotsDialog
                .Builder()
                .setContext(this)
                .setTheme(R.style.DialogCustomTheme)
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();


        btn_register.setOnClickListener( e-> {
            validateData();
        });

    }

    private void validateData() {
        //get data from ui
        fname = et_first_name.getText().toString();
        lname = et_last_name.getText().toString();
        phone = et_phone.getText().toString();
        phone_backUp = et_phone_backUp.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        repassword = et_repassword.getText().toString();

        if (TextUtils.isEmpty(fname)){
            et_first_name.setError("First Name field is empty");
            et_first_name.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(lname)){
            et_last_name.setError("Last Name field is empty");
            et_last_name.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(phone)){
            et_phone.setError("Phone field is empty");
            et_phone.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(phone_backUp)){
            et_phone_backUp.setError("BackUp Phone field is empty");
            et_phone_backUp.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(email)){
            et_email.setError("Email field is empty");
            et_email.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(password)){
            et_password.setError("Password field is empty");
            et_password.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(repassword)){
            et_repassword.setError("Retype Password field is empty");
            et_repassword.setFocusable(true);
            return;//dont proceed
        }
        if (phone.length()<10){
            et_phone.setError("Phone Length can not be less than 10 digits");
            et_phone.setFocusable(true);
        }
        if (TextUtils.isEmpty(phone_backUp)){
            et_phone_backUp.setError("BackUp Phone Length can not be less than 10 digits");
            et_phone_backUp.setFocusable(true);
            return;//dont proceed
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Email format is wrong. Enter Correct email.");
            et_email.setFocusable(true);
            return;
        }
        if (password.length()<8){
            et_password.setError("Password cant be less than 8 characters. Enter Password again.");
            et_password.setFocusable(true);
            return;
        }
        if (! password.equals(repassword)){
            et_password.setError("Passwords don't match. Enter Password again.");
            et_password.setFocusable(true);
            return;
        }
        if (phone.equals(phone_backUp)){
            et_phone_backUp.setError("Please use a different back up phone number");
            et_phone_backUp.setFocusable(true);
            return;
        }

        /**GIHUB LINk:
         *              https://github.com/martin-ngigi/MyTracer-JWT-Django-API
         * **/
        //registerUSerWithJWT();
        registerUSerWithJWT();
    }

    private void registerUSerWithJWT() {
        // below line is for displaying our progress bar.
        dialog.show();

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.baseUrl+"/auth/") // with jwt
                //.baseUrl(Constants.baseUrl+"/accounts/") // without jwt
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RegisterApi registerApi = retrofit.create(RegisterApi.class);

        // passing data from our text fields to our modal class.
        //username
        UserRegisterModel registerModel = new UserRegisterModel(email, email, phone, fname, lname, password, phone_backUp);

        // calling a method to create a post and passing our modal class.
        Call<UserRegisterModel> call = registerApi.createPost(registerModel);

        // on below line we are executing our method.
        call.enqueue(new Callback<UserRegisterModel>() {
            @Override
            public void onResponse(Call<UserRegisterModel> call, Response<UserRegisterModel> response) {
                // this method is called when we get response from our api.
                Toast.makeText(RegisterActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                dialog.dismiss();

                // on below line we are setting empty text
                // to our both edit text.
                et_first_name.setText("");
                et_last_name.setText("");
                et_email.setText("");
                et_password.setText("");

                // we are getting response from our body
                // and passing it to our modal class.
                UserRegisterModel responseRegisterFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nfirst_name : " + responseRegisterFromAPI.getFirst_name() + "\n" + "last_name : " + responseRegisterFromAPI.getLast_name();

                // below line we are setting our
                // string to our text view.
                //tv_subtitle.setText(responseString);
                tv_subtitle.setText(responseString);
            }

            @Override
            public void onFailure(Call<UserRegisterModel> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                tv_subtitle.setText("Error found is : " + t.getMessage());
            }
        });
    }

}