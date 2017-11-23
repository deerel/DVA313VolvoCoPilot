package com.dva313.volvo.safeassist;

/**
 * Created by Rickard on 2017-11-23.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class GPSService extends Service {
    private static final String TAG = "GPS Message: ";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private double mLatitude, mLongitude;
    //path to the php file
    String url = "http://volvo.xdo.se/test_gps/update_gps_location.php";
    //verification key for the php file
    final String key = "iu5gli54";
    String user_id, vehicle_id;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        //everytime on a location update, meaning if the lat and long changes, this function will be called
        @Override
        public void onLocationChanged(final Location location) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            distance();
            Toast.makeText(getApplicationContext(), "onLocationChanged: " + location.getLatitude(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            //sending in the request to php as a POST
            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override//the response from php is received here
                public void onResponse(String response) {
                    Log.i("RESPONSE", "GOT RESPONSE");
                    Log.i("RESPONSE", response.toString());
                    //if the worker was inserted newly or it already existed in the DB
                    if (!response.contains("Inserting gps location successful")) {
                        Toast.makeText(getApplicationContext(), "Could not update the location to the Database", Toast.LENGTH_SHORT).show();


                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("worker_id", user_id);
                    params.put("vehicle_id", vehicle_id);
                    //maybe not that good to convert the double values to string and reconvert them in the php, but for now it works
                    params.put("lat", String.valueOf(location.getLatitude()));
                    params.put("lon", String.valueOf(location.getLongitude()));
                    params.put("key", key);
                    return params;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(postRequest);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();

        //get the logged in users data
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        user_id = preferences.getString("user_id", null);
        vehicle_id = preferences.getString("vehicle_id", null);

        try {
            //permissions ACCESS_FINE_LOCATION & ACCESS_COARSE_LOCATIO are used by this below
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void distance() {
        double lat0 = 59.63122;
        double lon0 = 16.56601;
//        function distance(lat1, lon1, lat2, lon2) {
//            var p = 0.017453292519943295;    // Math.PI / 180
//            var c = Math.cos;
//            var a = 0.5 - c((lat2 - lat1) * p)/2 +
//                    c(lat1 * p) * c(lat2 * p) *
//                            (1 - c((lon2 - lon1) * p))/2;
//
//            return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
//        }
        double p = 0.017453292519943295;
        double a = 0.5 - Math.cos((mLatitude - lat0) * p) / 2 +
                Math.cos(lat0 * p) * Math.cos(mLatitude * p) *
                        (1 - Math.cos((mLongitude - lon0) * p)) / 2;

        a =  12742 * Math.asin(Math.sqrt(a));

//        Intent intent = new Intent("GPSLocationUpdates");
//        intent.putExtra("Status", String.valueOf(a));
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(StartScreen.mBroadcastGpsAction);
        broadcastIntent.putExtra("Data", String.valueOf(a));
        sendBroadcast(broadcastIntent);
    }
}