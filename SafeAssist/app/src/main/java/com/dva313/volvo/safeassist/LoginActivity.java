package com.dva313.volvo.safeassist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

/**
 * LoginActivity
 *
 * <P>Application starting point. Handles user login.
 *
 * @author Dara
 * @version 1.0
 * @since   2017-12-08
 */
public class LoginActivity extends AppCompatActivity {
    private EditText mUsername, mPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        //to get the workers data and see if they are logged in, and also insert the type of buildConfig type
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        Boolean isInlogged = preferences.getBoolean("is_inlogged", false);

        /* User should login every time, until that is changed the following code is disabled */
        if(isInlogged){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            //to kill the first page activity
            finish();
        }

        SharedPreferences.Editor editor = preferences.edit();

        if(BuildConfig.BUILD_TYPE.contains("copilot")){
            editor.putString("unittype", "copilot");
            editor.apply();
            //Toast.makeText(getApplicationContext(), "copilot", Toast.LENGTH_LONG).show();

        }else if(BuildConfig.BUILD_TYPE.contains("handheld")){
            editor.putString("unittype", "handheld");
            editor.apply();
            //Toast.makeText(getApplicationContext(), "handheld", Toast.LENGTH_LONG).show();
        }else {
            editor.putString("unittype", "unknown");
            editor.apply();
            Toast.makeText(getApplicationContext(), "ERROR: Could not get the application type.", Toast.LENGTH_LONG).show();
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

            SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
            String unittype = preferences.getString("unittype", null);

            if(unittype == "copilot"){

                //sending in the request to php as a POST
                StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

                    @Override//the response from php is received here
                    public void onResponse(String response) {

                        //if the worker was inserted newly or it already existed in the DB
                        if(response.contains("Inserting successful")){
                            //save the data in SharedPreferences so we later retrieve them for use
                            SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
                            editor.putString("username", username);
                            //editor.putString("unittype", "copilot");
                            editor.putBoolean("is_inlogged", true);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //to kill the first page activity
                            finish();

                        }else if(response.contains("Could not insert the new worker")){
                            Toast.makeText(getApplicationContext(), "Could not insert the new worker", Toast.LENGTH_SHORT).show();
                        }else if(response.contains("No worker with that username was found")){
                            Toast.makeText(getApplicationContext(), "No worker with that username was found", Toast.LENGTH_SHORT).show();
                        }else{

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
                        params.put("action", "copilotLogin");
                        params.put("worker_id", username);
                        params.put("password", password);
                        params.put("key", Constants.AUTH_KEY);
                        return params;
                    }
                };

                Volley.newRequestQueue(this).add(postRequest);


            }else{

                //sending in the request to php as a POST
                StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

                    @Override//the response from php is received here
                    public void onResponse(String response) {

                        //if the worker was inserted newly or it already existed in the DB
                        if(response.contains("Inserting successful")){
                            //save the data in SharedPreferences so we later retrieve them for use
                            SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
                            editor.putString("username", username);
                            //editor.putString("unittype", "copilot");
                            editor.putBoolean("is_inlogged", true);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //to kill the first page activity
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            //Log.i("LOGIN ERROR", response);
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
                        params.put("action", "workerLogin");
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

}
