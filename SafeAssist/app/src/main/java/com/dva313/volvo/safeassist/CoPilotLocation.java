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


    public CoPilotLocation(RequestQueue requestQueue, Context context) {
        super(requestQueue, context);
    }

    @Override
    void updateLocation(String identifier) {

    }

    @Override
    void onDestroy() {

    }
}
