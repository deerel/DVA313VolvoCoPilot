package com.dva313.volvo.safeassist;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

public class GeoLocation {

    private Location mLocation = null;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000; // Minimal duration, in milliseconds, needed to get an update
    private static final float LOCATION_DISTANCE = 0.0f; // Minimal distance, in meters, needed to get an update
    private RequestQueue mAlarmRequestQueue;
    private Context mContext = null;
    //String mWorkerId = null;


    public GeoLocation(RequestQueue requestQueue, Context context) {
        mAlarmRequestQueue = requestQueue;
        mContext = context;
        //mWorkerId = workerId;

        initializeLocationManager();

    }

    public void onDestroy() {
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

    public void updateLocation(final String workerId) {

        if(mLocation == null) return;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
                if (!response.contains("Inserting gps location successful")) {
                    Toast.makeText(mContext, "Could not update the location to the Database", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
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
                params.put("worker_id", workerId);
                params.put("action", "location");
                //maybe not that good to convert the double values to string and reconvert them in the php, but for now it works
                params.put("lat", String.valueOf(mLocation.getLatitude()));
                params.put("lon", String.valueOf(mLocation.getLongitude()));
                params.put("key", Constants.AUTH_KEY);
                return params;
            }
        };

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
            //gpstest();
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
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i("Location", "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d("Location", "gps provider does not exist " + ex.getMessage());
            }

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.i("Location", "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d("Location", "network provider does not exist, " + ex.getMessage());
            }
        }
    }

    public void setLocationUpdateInterval(int minTime, int minDistance) {

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, minTime, minDistance,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i("Location", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("Location", "gps provider does not exist " + ex.getMessage());
        }

    }


//    /* Test function for testing update interval on app in background*/
//    private void gpstest() {
//
//        Log.i("GPS TEST", "Updating...");
//        if(mLocation == null) return;
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {
//
//            @Override//the response from php is received here
//            public void onResponse(String response) {
//                if (!response.contains("Inserting gps location successful")) {
//                    Toast.makeText(mContext, "Could not update the location to the Database", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
//                } else {
//                    Log.i("GPS TEST", response.toString());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //error.printStackTrace();
//                Log.i("GPS TEST", error.getMessage());
//                //Toast.makeText(getApplicationContext(), "Could not insert the data.", Toast.LENGTH_SHORT).show();
//            }
//        }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                // the POST parameters:
//                params.put("worker_id", mWorkerId);
//                params.put("action", "gpstest");
//                //maybe not that good to convert the double values to string and reconvert them in the php, but for now it works
//                params.put("lat", String.valueOf(mLocation.getLatitude()));
//                params.put("lon", String.valueOf(mLocation.getLongitude()));
//                params.put("key", Constants.AUTH_KEY);
//                return params;
//            }
//        };
//
//        mAlarmRequestQueue.add(postRequest);
//    }

}
