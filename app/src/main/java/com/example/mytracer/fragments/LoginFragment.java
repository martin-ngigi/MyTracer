package com.example.mytracer.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytracer.Constants;
import com.example.mytracer.MyLocation2;
import com.example.mytracer.R;
import com.example.mytracer.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;


public class LoginFragment extends Fragment {

    private Button btn_login;
    private EditText et_email, et_password, et_id;
    private String email, password, id;
    private AlertDialog dialog;

    public LoginFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        btn_login = view.findViewById(R.id.btn_login);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        et_id = view.findViewById(R.id.et_id);

        dialog = new SpotsDialog
                .Builder()
                .setCancelable(false)
                .setContext(getActivity())
                .setTheme(R.style.DialogCustomTheme)
                .build();

        btn_login.setOnClickListener(e ->{
            //startActivity(new Intent(getContext(), HomeActivity.class));
            validateData();
        });
        return view;
    }

    private void validateData() {
        //get data from ui

        email = et_email.getText().toString();
        password = et_password.getText().toString();
        id = et_id.getText().toString();


        if (TextUtils.isEmpty(email)){
            et_email.setError("Email field is empty");
            et_email.setFocusable(true);
            return;//dont proceed
        }
        if (TextUtils.isEmpty(password)) {
            et_password.setError("Password field is empty");
            et_password.setFocusable(true);
            return;//dont proceed
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Email format is wrong. Enter Correct email.");
            et_email.setFocusable(true);
            return;
        }

        getDataLoginDataFromVolley();
    }

    private void getDataLoginDataFromVolley() {
        dialog.setMessage("Login in progress...");
        dialog.show();

        String baseUrl = Constants.baseUrl;
        String url = baseUrl+"/accounts/users/"+id+"/";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //post data
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //hide dialog
                dialog.dismiss();

                //Toast.makeText(getActivity(), "Response is :"+response.toString(), Toast.LENGTH_LONG).show();
                try {
                    //parsing the response to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);


                    //extract data from json object response
                    int id1 = respObj.getInt("id");
                    String fname1 = respObj.getString("first_name");
                    String lname1 = respObj.getString("last_name");
                    String email1 = respObj.getString("email");
                    String password1 = respObj.getString("password");
                    String phone1 = respObj.getString("phone");
                    String backup_phone1 = respObj.getString("backup_phone");
                    int userLocationID = respObj.getInt("userLocationID");

                    //String message = respObj.getString("email");
                    //Toast.makeText(getActivity(), "Response Message ID: "+id, Toast.LENGTH_LONG).show();

                    loginCheck(id1, fname1, lname1, email1, password1, phone1, backup_phone1, userLocationID);


                }
                catch (JSONException e){

                    Log.e("VolleyError", "onErrorResponse: "+e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "1. Error: An error occurred.\nHint: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //hide dialog
                dialog.dismiss();
                //Handle errors
                //check for the response data in the VolleyError and parse it your self.
                onErrorResponse2(error);
                Log.e("VolleyError", "2. onErrorResponse: "+error.getMessage());
                Toast.makeText(getActivity(), "2. Error: Failed to get response. \nHint: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //make a json request
        queue.add(request);
    }

    private void loginCheck(int id1, String fname1, String lname1, String email1, String password1, String phone1, String backup_phone1, int userLocationID) {
        if (email.equals(email1) && password.equals(password1)){
            //save data to constants
            Constants.id = id1;
            Constants.f_name= fname1;
            Constants.l_name = lname1;
            Constants.email = email1;
            Constants.phone = phone1;
            Constants.backup_phone = backup_phone1;
            Constants.password = password1;
            Constants.userLocationID = userLocationID;

            //proceed to homepage
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        }
        else {
            Toast.makeText(getActivity(), "Wrong Login credentials. Please Try again", Toast.LENGTH_SHORT).show();
        }
    }

    //check for the response data in the VolleyError and parse it your self.
    // import com.android.volley.toolbox.HttpHeaderParser;
    public void onErrorResponse2(VolleyError error) {

        // As of f605da3 the following should work
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                // Now you can use any deserializer to make sense of data
                JSONObject obj = new JSONObject(res);
                Toast.makeText(getContext(), "Object is : "+obj.toString(), Toast.LENGTH_LONG).show();
                Log.i("RegisterActivity", "Object is "+obj);
            } catch (UnsupportedEncodingException e1) {
                // Couldn't properly decode data to string
                e1.printStackTrace();
                Log.e("RegisterActivity", "e1 onErrorResponse2:\nCouldn't properly decode data to string\n "+e1.getMessage());
            } catch (JSONException e2) {
                // returned data is not JSONObject?
                Log.e("RegisterActivity", "e2 onErrorResponse2: \nreturned data is not JSONObject?\n"+e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

}