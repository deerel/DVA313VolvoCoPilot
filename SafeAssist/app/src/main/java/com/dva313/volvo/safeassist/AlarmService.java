package com.dva313.volvo.safeassist;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rickard on 2017-12-07.
 */

public class AlarmService extends Service {

    int mSleepTime = 5000;
    String mWorkerId = null;
    int mReturnValue = Constants.ALARM_NO_RESPONSE;

    Messenger messenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Message MSG;
            String message;
            Bundle bundle = new Bundle();

            switch (msg.what) {
                case Constants.ALARM_SET_ALARM_DELAY:
                    String delay = msg.getData().getString("Response_message");
                    if( msg.getData().containsKey("Worker_id")) {
                        mWorkerId = msg.getData().getString("Worker_id");
                    }
                    mSleepTime = Integer.parseInt(delay);
                    break;
                case Constants.ALARM_ACKNOWLEDGE:
                    /* Sender is ready for a new message */
                    break;
                default:
                    super.handleMessage(msg);
            }

            // Get Alarm Level from server and respond to sender
            int alarm = fetchAlarm();
            Log.i("AlarmService", "Alarm code: " + alarm);
            switch (alarm) {
                case Constants.ALARM_NOTIFICATION:
                    message = "Just a notification";
                    MSG = Message.obtain(null, Constants.ALARM_NOTIFICATION);
                    bundle.putInt("Response_message", Constants.ALARM_NOTIFICATION);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALARM_ALARM_LEVEL_0:
                    message = "No Alarm";
                    MSG = Message.obtain(null, Constants.ALARM_ALARM_LEVEL_0);
                    bundle.putInt("Response_message", Constants.ALARM_ALARM_LEVEL_0);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALARM_ALARM_LEVEL_1:
                    message = "Alarm Level 1";
                    MSG = Message.obtain(null, Constants.ALARM_ALARM_LEVEL_1);
                    bundle.putInt("Response_message", Constants.ALARM_ALARM_LEVEL_1);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALARM_ALARM_LEVEL_2:
                    message = "No Alarm";
                    MSG = Message.obtain(null, Constants.ALARM_ALARM_LEVEL_2);
                    bundle.putInt("Response_message", Constants.ALARM_ALARM_LEVEL_2);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    message = "No response!";
                    MSG = Message.obtain(null, Constants.ALARM_NO_RESPONSE);
                    bundle.putInt("Response_message", Constants.ALARM_NO_RESPONSE);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }
    }

    private int fetchAlarm() {


        if(mWorkerId == null) {
            Log.e("AlarmServie", "Worker id must be set in Alarm Service.");
            return mReturnValue;
        }
        // Sleep to not spam and drain battery
        try
        {
            Thread.sleep(mSleepTime);
        }
        catch (Exception e)
        {
            Log.e("AlarmServie: ", e.getMessage());
            return mReturnValue;
        }

        //sending in the request to php as a POST
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
                Log.i("GET ALERT", "GOT RESPONSE");
                Log.i("GET ALERT", response.toString());
                Log.i("CURRENT USER", mWorkerId);
                //sendMessage(response.toString());
                try {
                    int alarmResponse = Integer.parseInt(response.toString());
                    mReturnValue = alarmResponse;
                } catch (Exception e) {
                    Log.e("AlarmServie: ", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mReturnValue = Constants.ALARM_NO_RESPONSE;
                Toast.makeText(getApplicationContext(), "Could not get alert information.", Toast.LENGTH_LONG).show();
            }
        }
        ) {
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

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
        return mReturnValue;
    }
}
