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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytracer.Constants;
import com.example.mytracer.R;
import com.example.mytracer.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class RegisterFragment extends Fragment {

    private Button btn_register;

    private EditText et_first_name, et_last_name, et_phone, et_phone_backUp, et_email, et_password, et_repassword;

    private String fname, lname, phone, phone_backUp, email, password, repassword;

    private TextView successMessageTv;

    private RelativeLayout registerRlayout;
    private LinearLayout successLLayout;

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
        successMessageTv=view.findViewById(R.id.successMessageTv);
        registerRlayout=view.findViewById(R.id.registerRlayout);
        successLLayout = view.findViewById(R.id.successLLayout);

        //spots dialog
        dialog = new SpotsDialog
                .Builder()
                .setContext(getContext())
                .setTheme(R.style.DialogCustomTheme)
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

        //postDataUsingVolley(fname, lname, phone, phone_backUp, email, password);
       // postData(fname, lname, phone, phone_backUp, email, password);
        postDataUsingVolley2(fname, lname, phone, phone_backUp, email, password);
    }

    private void postDataUsingVolley2(String fname, String lname, String phone, String phone_backUp, String email, String password) {
        dialog.setTitle("Posting data.");
        dialog.setMessage("Please wait...");
        dialog.show();

        String baseUrl = Constants.baseUrl;
        String url = baseUrl+"/accounts/users/";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        //post data
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //hide dialog
                dialog.dismiss();

                //Toast.makeText(getActivity(), "Response is :"+response.toString(), Toast.LENGTH_LONG).show();
                try {
                    //parsing the response to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);


                    //extract data from json object response
                    //String message = respObj.getString("email");
                    int id = respObj.getInt("id");
                    Toast.makeText(getActivity(), "Response Message ID: "+id, Toast.LENGTH_LONG).show();

                    Constants.id = id;

                    //hide register layout
                    registerRlayout.setVisibility(View.GONE);
                    //show Success message
                    successLLayout.setVisibility(View.VISIBLE);
                    successMessageTv.setText("Registration Successful.\n Proceed to Login...\n User ID:"+id+" As your login ID");


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
        }){
            @Override
            protected Map<String, String> getParams() {
                //creating a map for storing our values in key and value pair.
                HashMap<String, String> hashMap = new HashMap<>();
                //passing our key and value pair to our parameters.
                /**
                 {
                 {
                 "email": "martinwainaina@gmail.com",
                 "username": "martinwainaina",
                 "phone": "0797292290",
                 "first_name": "Martin",
                 "last_name": "Wainaina",
                 "password": "12345678",
                 "backup_phone":"0712345678"
                 }
                 * **/


                //hashMap.put("Content-Type", "application/json");
//                hashMap.put("email","martinwainaina@gmail.com");
//                hashMap.put("username","martinwainaina");
//                hashMap.put("phone","0797292290");
//                hashMap.put("first_name","Martin");
//                hashMap.put("last_name","Wainaina");
//                hashMap.put("password","12345678");
//                hashMap.put("backup_phone","0712345678");

                hashMap.put("email", email);
                hashMap.put("username", email);
                hashMap.put("phone",phone);
                hashMap.put("first_name", fname);
                hashMap.put("last_name", lname);
                hashMap.put("username","afgh");
                hashMap.put("password", password);
                hashMap.put("backup_phone", phone_backUp);

                //return params
                return hashMap;
            }
        };

        //make a json request
        queue.add(request);
    }


    private void postDataUsingVolley(String fname, String lname, String phone, String phone_backUp, String email, String password) {
        dialog.setTitle("Posting data.");
        dialog.setMessage("Please wait...");
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String baseUrl = Constants.baseUrl;
        //String url = baseUrl+"/auth/signup/";
        String url = "https://0b1b-41-80-98-66.in.ngrok.io/auth/signup/";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //dismiss
                dialog.dismiss();

                Toast.makeText(getContext(), "My Success Response: "+response.toString(), Toast.LENGTH_LONG).show();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String message = jsonObject.getString("message");
                    String message = jsonObject.toString();
                    //Toast.makeText(getContext(), "Success Response: "+message, Toast.LENGTH_LONG).show();

                    //navigate to Login
                    startActivity(new Intent(getContext(), HomeActivity.class));

                }
                catch (JSONException e){
                    //dismiss
                    dialog.dismiss();
                    Log.e("RegisterFragment", "1. onResponse: Error"+e.getMessage());
                    Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dismiss
                dialog.dismiss();
                //check for the response data in the VolleyError and parse it your self.
                onErrorResponse2(error);
                Log.e("RegisterFragment", "2. onResponse: Error"+error.getMessage());
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
                 *     "password": "12345678",
                 *     "backup_phone":"0712345678"
                 * }
                 */

//                hashMap.put("email",email);
//                hashMap.put("username",username);
//                hashMap.put("phone",phone);
//                hashMap.put("first_name",fname);
//                hashMap.put("last_name",lname);
//                hashMap.put("password",password);
//                hashMap.put("backup_phone",phone_backUp);
                hashMap.put("email","martinwainainaj@gmail.com");
                hashMap.put("username","martinwainainaj");
                hashMap.put("phone","0797292290");
                hashMap.put("first_name","Martin");
                hashMap.put("last_name","Wainaina");
                hashMap.put("password","12345678");
                hashMap.put("backup_phone","0712345678");


                return hashMap;
            }
        };

        queue.add(request);



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
                        Toast.makeText(getContext(), "Response: "+response.toString(), Toast.LENGTH_LONG).show();

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