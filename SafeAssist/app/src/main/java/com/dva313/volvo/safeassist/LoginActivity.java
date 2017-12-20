package com.dva313.volvo.safeassist;

/**
 * Created by Rickard on 2017-11-23.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class LoginActivity extends AppCompatActivity {
    private EditText mUsername, mPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        //check if worker is already inlogged
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        Boolean is_inlogged = preferences.getBoolean("is_inlogged", false);
        if(is_inlogged){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            //to kill the first page activity
            finish();
        }

        //get each edittext component to later save the text string inside them into vars
        mUsername = findViewById(R.id.editTextUsername);
        mPassword = findViewById(R.id.editTextPassword);


    }

    void quit() {
        finish();
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_quit) {
            quit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void submit_user_result(View v) {

        //get the edittext/field data a string
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "One or more field are empty.", Toast.LENGTH_SHORT).show();
        }else{

            final String user_id = username+"_"+password;

            //sending in the request to php as a POST
            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

                @Override//the response from php is received here
                public void onResponse(String response) {

                    //if the worker was inserted newly or it already existed in the DB
                    if(response.contains("Inserting successful")){
                        //save the data in SharedPreferences so we later retrieve them for use
                        SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
                        editor.putString("username", username);
                        editor.putString("unittype", "handheld");
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        //to kill the first page activity
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                    params.put("action", "login");
                    params.put("worker_id", username);
                    params.put("password", password);
                    params.put("key", Constants.AUTH_KEY);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(postRequest);

        }
    }

}
