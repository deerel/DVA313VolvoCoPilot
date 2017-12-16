package com.dva313.volvo.safeassist;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rickard on 2017-12-15.
 */

class Alarm {

    private RequestQueue mRequestQueue = null;
    private Context mContext = null;
    private int mReturnValue = 0;

    Alarm(RequestQueue requestQueue, Context context) {
        mRequestQueue = requestQueue;
        mContext = context;
    }

    int fetchAlarm(final String mWorkerId) {

        if(mWorkerId == null) {
            Log.e("AlarmServie", "Worker id must be set in Alarm Service.");
            return mReturnValue;
        }

        /* Setting up a resuest to a specific URL. */
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

            /* Is called when the servers reply is received  */
            @Override
            public void onResponse(String response) {
                try {
                    mReturnValue = Integer.parseInt(response.toString());
                } catch (Exception e) {
                    Log.e("AlarmServie: ", e.getMessage());
                }

            }

           /* Is called if an error occurs  */
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mReturnValue = Constants.ALARM_NO_RESPONSE;
                Toast.makeText(mContext, "Could not get alert information.", Toast.LENGTH_LONG).show();
            }
        }
        ) {
            /* Set which arguments to send to the server */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("action", "alarm");
                params.put("worker_id", mWorkerId);
                params.put("key", Constants.AUTH_KEY);
                return params;
            }
        };

        /* Add the server request to a request queue to be sent */
        mRequestQueue.add(postRequest);
        return mReturnValue;
    }
}