package com.dva313.volvo.safeassist;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by Rickard on 2017-12-07.
 */

public class AlarmService extends Service {

    private static final String LOG_TAG = "ForegroundService";

    int mSleepTime = 5000;
    String mWorkerId = null;
    static int mReturnValue = Constants.ALARM_NO_RESPONSE;
    Location mLocation = null;
    RequestQueue mAlarmRequestQueue;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    Messenger messenger = new Messenger(new IncomingHandler());
    static Message mMessageReceived = null;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeLocationManager();

        try {
            //permissions ACCESS_FINE_LOCATION & ACCESS_COARSE_LOCATIO are used by this below
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i("Location", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("Location", "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i("Location", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("Location", "gps provider does not exist " + ex.getMessage());
        }

        // Instantiate the cache
        Cache mAlarmCache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network mAlarmNetwork = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mAlarmRequestQueue = new RequestQueue(mAlarmCache, mAlarmNetwork);
        // Start the queue
        mAlarmRequestQueue.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("Location", "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            //Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, StartScreen.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_safeassist);

            Notification notification = new NotificationCompat.Builder(this, "fgservice")
                    .setContentTitle("Volvo SafeAssist")
                    .setTicker("Volvo SafeAssist")
                    .setContentText("Running")
                    .setSmallIcon(R.drawable.ic_safeassist)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true).build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
            //timerHandler.postDelayed(timerRunnable, 0);
        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            //Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            //timerHandler.removeCallbacks(timerRunnable);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            mMessageReceived = msg;
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
            //timerHandler.postDelayed(timerRunnable, 0);
            //Log.i("AlarmService", "Alarm code: " + alarm);
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
                    message = "Alarm Level 2";
                    MSG = Message.obtain(null, Constants.ALARM_ALARM_LEVEL_2);
                    bundle.putInt("Response_message", Constants.ALARM_ALARM_LEVEL_2);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALARM_ALARM_LEVEL_3:
                    message = "Alarm Level 3";
                    MSG = Message.obtain(null, Constants.ALARM_ALARM_LEVEL_3);
                    bundle.putInt("Response_message", Constants.ALARM_ALARM_LEVEL_3);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALARM_NO_RESPONSE:
                    message = "No respons";
                    MSG = Message.obtain(null, Constants.ALARM_NO_RESPONSE);
                    bundle.putInt("Response_message", Constants.ALARM_NO_RESPONSE);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    message = "No response!";
                    MSG = Message.obtain(null, Constants.ALARM_ERROR);
                    bundle.putInt("Response_message", Constants.ALARM_ERROR);
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

        // Update GPS location
        updateLocation();

        //sending in the request to php as a POST
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
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

        //Volley.newRequestQueue(getApplicationContext()).add(postRequest);
        mAlarmRequestQueue.add(postRequest);
        return mReturnValue;
    }


    /*
    *   LOCATION
    */

    private void updateLocation() {

        if(mLocation == null) return;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
                if (!response.contains("Inserting gps location successful")) {
                    Toast.makeText(getApplicationContext(), "Could not update the location to the Database", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();

                //Toast.makeText(getApplicationContext(), "Could not insert the data.", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("worker_id", mWorkerId);
                params.put("action", "location");
                //maybe not that good to convert the double values to string and reconvert them in the php, but for now it works
                params.put("lat", String.valueOf(mLocation.getLatitude()));
                params.put("lon", String.valueOf(mLocation.getLongitude()));
                params.put("key", Constants.AUTH_KEY);
                return params;
            }
        };

        //Volley.newRequestQueue(getApplicationContext()).add(postRequest);
        mAlarmRequestQueue.add(postRequest);
    }

    private class LocationListener implements android.location.LocationListener
    {

        public LocationListener(String provider)
        {
            mLocation = new Location(provider);
        }

        //everytime on a location update, meaning if the lat and long changes, this function will be called
        @Override
        public void onLocationChanged(final Location location) {
            mLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            //Log.e("Location", "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            //Log.e("Location", "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            //Log.e("Location", "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


}
