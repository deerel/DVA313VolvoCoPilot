package com.dva313.volvo.safeassist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Alarm Fetching Service
 *
 * <P>Handles server requests for alarm data.
 *
 * @author Rickard
 * @version 1.0
 * @since   2017-12-08
 */
class Alarm {

    private RequestQueue mRequestQueue = null;
    private Context mContext = null;
    private int mReturnValue = 0;

    
    Alarm(RequestQueue requestQueue, Context context) {
        mRequestQueue = requestQueue;
        mContext = context;
    }

    /**
     * Returns the current alarm level for the given user.
     *
     * @param mWorkerId The id of the logged in worker
     * @param caller    Reference to the object to reply to on server response
     * @return          Integer, the alarm level received from the server
     */
    int fetchAlarm(final String mWorkerId, final ServerComService.Callback caller) {

        if(mWorkerId == null) {
            Log.e("AlarmServie", "Worker id must be set in Alarm Service.");
            return mReturnValue;
        }

        SharedPreferences preferences = mContext.getSharedPreferences("workers_data", MODE_PRIVATE);
        String unittype = preferences.getString("unittype", null);

        if(unittype == "copilot"){

            /* Setting up a resuest to a specific URL. */
            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

                /* Is called when the servers reply is received  */
                @Override
                public void onResponse(String response) {
                    try {
                        mReturnValue = Integer.parseInt(response.toString());
                        caller.callback(mReturnValue, null);
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
                    params.put("action", "copilotAlarm");
                    params.put("worker_id", mWorkerId);
                    params.put("key", Constants.AUTH_KEY);
                    return params;
                }
            };

        /* Add the server request to a request queue to be sent */
            mRequestQueue.add(postRequest);

        }else{
            /* Setting up a resuest to a specific URL. */
            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

                /* Is called when the servers reply is received  */
                @Override
                public void onResponse(String response) {
                    try {
                        mReturnValue = Integer.parseInt(response.toString());
                        caller.callback(mReturnValue, null);
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
                    params.put("action", "workerAlarm");
                    params.put("worker_id", mWorkerId);
                    params.put("key", Constants.AUTH_KEY);
                    return params;
                }
            };

        /* Add the server request to a request queue to be sent */
            mRequestQueue.add(postRequest);
        }

        return mReturnValue;
    }

    /**
     * Cancel all server communication and stop listening for reply.
     */
    public void cancel() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

}
