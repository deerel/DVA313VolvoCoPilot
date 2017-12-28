package com.dva313.volvo.safeassist;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

import se.cpacsystems.common.Position;
import se.cpacsystems.position.PositionManager;

/**
 * CoPilot GPS Location service
 *
 * <P>Reads the device's location and sends updates to remote server.
 *
 * @author Rickard
 * @version 1.0
 * @since   2017-12-15
 */
class CoPilotLocation extends GeoLocation {

    private static final int LOCATION_INTERVAL = 1000; // Minimal duration, in milliseconds, needed to get an update
    private static final float LOCATION_DISTANCE = 0.0f; // Minimal distance, in meters, needed to get an update

    private double mLat, mLon;

    private PositionManager _positionManager;
    private boolean _positionConnected = false;

    /**
     * Constructor
     *
     * @param requestQueue  A Volly request queue for sending http-request.
     * @param context       The context of the caller.
     */
    CoPilotLocation(RequestQueue requestQueue, Context context) {
        super(requestQueue, context);
        _positionManager = new PositionManager(mContext);

        if(_positionConnected == false)
        {
            _positionManager.connect();
            _positionConnected = true;
        }

    }

    /**
     * Should be called when the object is not needed any more.
     */
    @Override
    void onDestroy() {

    }

    /**
     * Send an update of current position to the server.
     *
     * @param identifier    The identifier for the logged in user or vehicle (CoPilot)
     */
    @Override
    void updateLocation(final String identifier) {

        Position newPos = _positionManager.getPosition();
        mLat = newPos.latitude;
        mLon = newPos.longitude;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

            @Override//the response from php is received here
            public void onResponse(String response) {
                if (!response.contains("Inserting gps location successful")) {
                    Toast.makeText(mContext, "Could not update the location to the Database", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Lat: " + mLat + ", Lon: " + mLon, Toast.LENGTH_LONG).show();
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
                params.put("worker_id", identifier);
                params.put("action", "location");
                //maybe not that good to convert the double values to string and reconvert them in the php, but for now it works
                params.put("lat", String.valueOf(mLat));
                params.put("lon", String.valueOf(mLon));
                params.put("key", Constants.AUTH_KEY);
                return params;
            }
        };

        mAlarmRequestQueue.add(postRequest);
    }




}
