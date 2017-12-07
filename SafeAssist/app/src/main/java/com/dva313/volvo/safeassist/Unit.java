package com.dva313.volvo.safeassist;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dara on 2017-12-02.
 */

public class Unit {
    protected Double mRadius;
    protected String mIdentifier;
    private boolean mIsLoggedIn = false;
    private Context mContext;

    //abstract void setRadius(Double radius);
    //abstract Double getRadius();
    //abstract void setIdentifier(String identifier);
    //abstract String getIdentifier();
    public Unit(Context context) {

        mContext = context.getApplicationContext();
    }

    /* TODO
    *  Continue implementation when web interface is done.
    */
    public boolean login(String username, String password){
        String url = "http://volvo.xdo.se/safeassist/login.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
                if(response.contains("1")) {
                    mIsLoggedIn = true;
                    Toast.makeText(mContext.getApplicationContext(), "Login successful. Welcome!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext.getApplicationContext(), "Could not login." + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext.getApplicationContext(), "Could not connect to server.", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("worker_id", "kalle");
                params.put("vehicle_id", "car");

                return params;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(postRequest);


        return mIsLoggedIn;
    }

}

