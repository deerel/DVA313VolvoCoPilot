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
    String mAlertUrl = "http://volvo.xdo.se/test_gps/get_alert.php";
    //verification key for the php file

    final String mAlertKey = "pu4gl3y1";
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

            // For testing
//            double lat2 = 59.632856;
//            double lon2 = 16.565902;
//            double dist = distance(mLatitude, lat2, mLongitude, lon2, 0.0, 0.0);
//            sendMessage(String.valueOf(dist));
            // Stor testing

            Toast.makeText(getApplicationContext(), "onLocationChanged: " + location.getLatitude(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            Log.i("UPDATE URL", url);
            //sending in the request to php as a POST
            StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override//the response from php is received here
                public void onResponse(String response) {
                    getAlert();
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
                    Log.e("UPDATE", error.getMessage());
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


    // https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final double R = 6371.392896;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);

    }

    // Send an Intent with an action named "custom-event-name". The Intent
    // sent should
    // be received by the ReceiverActivity.
    private void sendMessage(String message) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("gps-distance");
        // You can also include some extra data.
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void getAlert() {

        //sending in the request to php as a POST
        StringRequest postRequest = new StringRequest(Request.Method.POST, mAlertUrl, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
                Log.i("GET ALERT", "GOT RESPONSE");
                Log.i("GET ALERT", response.toString());
                Log.i("CURRENT USER", user_id);
                sendMessage(response.toString());
                //if the worker was inserted newly or it already existed in the DB
//                if (!response.contains("Inserting gps location successful")) {
//                    Toast.makeText(getApplicationContext(), "Could not update the location to the Database", Toast.LENGTH_SHORT).show();
//
//
//                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not get alert information.", Toast.LENGTH_LONG).show();
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
                params.put("lat", String.valueOf(mLatitude));
                params.put("lon", String.valueOf(mLongitude));
                params.put("key", mAlertKey);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);

    }
}