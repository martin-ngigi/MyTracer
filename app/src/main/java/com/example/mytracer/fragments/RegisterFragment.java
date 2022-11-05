package com.example.mytracer.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytracer.Constants;
import com.example.mytracer.R;
import com.example.mytracer.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class RegisterFragment extends Fragment {

    private Button btn_register;

    private EditText et_first_name, et_last_name, et_phone, et_phone_backUp, et_email, et_password, et_repassword;

    private String fname, lname, phone, phone_backUp, email, password, repassword;

    private AlertDialog dialog;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        et_first_name = view.findViewById(R.id.et_first_name);
        et_last_name = view.findViewById(R.id.et_last_name);
        et_phone = view.findViewById(R.id.et_phone);
        et_phone_backUp = view.findViewById(R.id.et_phone_backUp);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        et_repassword = view.findViewById(R.id.et_repassword);
        btn_register =view.findViewById(R.id.btn_register);

        //spots dialog
        dialog = new SpotsDialog
                .Builder()
                .setContext(getContext())
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();


        btn_register.setOnClickListener( e-> {
            validateData();
        });

        return view;
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

        postDataUsingVolley(fname, lname, phone, phone_backUp, email, password);
       // postData(fname, lname, phone, phone_backUp, email, password);
    }

    private void postDataUsingVolley(String fname, String lname, String phone, String phone_backUp, String email, String password) {
        dialog.setTitle("Posting data.");
        dialog.setMessage("Please wait...");
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String baseUrl = Constants.baseUrl;
        String url = baseUrl+"/auth/signup/";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //dismiss
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.toString();
                    Toast.makeText(getContext(), "Response: "+message, Toast.LENGTH_SHORT).show();

                    //navigate to Login
                    startActivity(new Intent(getContext(), HomeActivity.class));

                }
                catch (JSONException e){
                    //dismiss
                    dialog.dismiss();
                    Log.e("RegisterFragment", "onResponse: Error"+e.getMessage());
                    Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dismiss
                dialog.dismiss();
                Log.e("RegisterFragment", "onResponse: Error"+error.getMessage());
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap <String, String> hashMap = new HashMap<>();
                String username = email.replace("@gmail.com", "");

                /**
                 * {
                 *     "email": "martinwainaina001@gmail.com",
                 *     "username": "martinwainaina001",
                 *     "phone": "0797292290",
                 *     "first_name": "Martin",
                 *     "last_name": "Wainaina",
                 *     "date_of_birth": "1999-11-23",
                 *     "password": "12345678",
                 *     "backup_phone":"0712345678"
                 * }
                 */

                hashMap.put("email",email);
                hashMap.put("username",username);
                hashMap.put("phone",phone);
                hashMap.put("first_name",fname);
                hashMap.put("last_name",lname);
                hashMap.put("date_of_birth","");
                hashMap.put("password",password);
                hashMap.put("backup_phone",phone_backUp);


                return hashMap;
            }
        };

        queue.add(request);



    }


    // Post Request For JSONObject
    public void postData(String fname, String lname, String phone, String phone_backUp, String email, String password) {

        dialog.setTitle("Posting data.");
        dialog.setMessage("Please wait...");
        dialog.show();

        String username = this.email.replace("@gmail.com", "");

        String baseUrl = Constants.baseUrl;
        String url = baseUrl+"/auth/signup/";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("username",username);
            object.put("phone", phone);
            object.put("first_name", fname);
            object.put("last_name", lname);
            object.put("date_of_birth","");
            object.put("password", password);
            object.put("backup_phone", phone_backUp);
            //object.put("is_active", true);
        } catch (JSONException e) {
            e.printStackTrace();
            //dismiss
            dialog.dismiss();
            Log.e("RegisterFragment", "onResponse: Error"+e.getMessage());
            Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Response: "+response.toString(), Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dismiss
                dialog.dismiss();
                Log.e("RegisterFragment", "onResponse: Error"+error.getMessage());
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}