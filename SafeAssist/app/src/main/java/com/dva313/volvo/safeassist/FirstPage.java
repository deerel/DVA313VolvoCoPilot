package com.dva313.volvo.safeassist;

/**
 * Created by Rickard on 2017-11-23.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstPage extends AppCompatActivity {
    EditText firstname_et, lastname_et, vehicle_id_et, birthday_year_et, birthday_month_et, birthday_day_et;


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
        firstname_et = (EditText)findViewById(R.id.firstname_ET);
        lastname_et = (EditText)findViewById(R.id.lastname_ET);
        vehicle_id_et = (EditText)findViewById(R.id.vehicle_id_ET);
        birthday_year_et = (EditText)findViewById(R.id.birthday_year_ET);
        birthday_month_et = (EditText)findViewById(R.id.birthday_month_ET);
        birthday_day_et = (EditText)findViewById(R.id.birthday_day_ET);
    }

    public void submit_user_result(View v){

        //get the edittext/field data a string
        final String firstname = firstname_et.getText().toString();
        final String lastname = lastname_et.getText().toString();
        final String vehicle_id = vehicle_id_et.getText().toString();
        String birth_year = birthday_year_et.getText().toString();
        String birth_month = birthday_month_et.getText().toString();
        String birth_day = birthday_day_et.getText().toString();

        if(firstname.isEmpty() || lastname.isEmpty() || vehicle_id.isEmpty() || birth_day.isEmpty() || birth_month.isEmpty() || birth_year.isEmpty()){
            Toast.makeText(getApplicationContext(), "One or more field are empty.", Toast.LENGTH_SHORT).show();
        }else{
            //set the firstname, lastname and birthday as the unique identifier. a better way is to save personal number if we are allowed to do it
            final String user_id = firstname+"_"+lastname+"-"+birth_year+"/"+birth_month+"/"+birth_day;

            //path to the php file
            String url = "http://volvo.xdo.se/test_gps/insert_worker_data.php";
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
                        editor.putString("first_name", firstname);
                        editor.putString("last_name", lastname);
                        editor.putString("mUserId", user_id);
                        editor.putString("mVehicleId", vehicle_id);
                        editor.putBoolean("is_inlogged", true);
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
                    params.put("worker_id", user_id);
                    params.put("mVehicleId", vehicle_id);
                    params.put("key", key);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(postRequest);

        }
    }

}
