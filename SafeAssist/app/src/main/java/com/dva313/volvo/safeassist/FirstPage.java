package com.dva313.volvo.safeassist;

/**
 * Created by Rickard on 2017-11-23.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FirstPage extends AppCompatActivity {
    EditText mUsername, mPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_page_layout);

        //check if worker is already inlogged
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        Boolean is_inlogged = preferences.getBoolean("is_inlogged", false);
        if(is_inlogged){
            Intent intent = new Intent(FirstPage.this, StartScreen.class);
            startActivity(intent);
            //to kill the frst page activity
            finish();
        }

        //get each edittext component to later save the text string inside them into vars
        mUsername = (EditText)findViewById(R.id.editTextUsername);
        mPassword = (EditText)findViewById(R.id.editTextPassword);

    }

    public void submit_user_result(View v){

        //get the edittext/field data a string
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "One or more field are empty.", Toast.LENGTH_SHORT).show();
        }else{

            final String user_id = username+"_"+password;

            //path to the php file
            String url = "http://volvo.xdo.se/safeassist/login.php";
            //verification key for the php file
            final String key = "32g5hj2g";

            //sending in the request to php as a POST
            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override//the response from php is received here
                public void onResponse(String response) {

                    //if the worker was inserted newly or it already existed in the DB
                    if(response.contains("Worker already exists") || response.contains("Inserting successful")){
                        //save the data in SharedPreferences so we later retrieve them for use
                        SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
                        editor.putString("username", username);

                        editor.apply();

                        Intent intent = new Intent(FirstPage.this, StartScreen.class);
                        startActivity(intent);
                        //to kill the frst page activity
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), "Inserting failed.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Could not insert the data.", Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<>();
                    // the POST parameters:
                    params.put("username", username);
                    params.put("password", password);
                    params.put("key", key);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(postRequest);

        }
    }

}
