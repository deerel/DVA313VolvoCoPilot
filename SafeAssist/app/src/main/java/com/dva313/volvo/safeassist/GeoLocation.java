package com.dva313.volvo.safeassist;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Location Super Class
 *
 * <P>Abstract class that shall be extended by location serving classes.
 *
 * @author Rickard
 * @version 1.0
 * @since   2017-12-16
 */
public abstract class GeoLocation {
    protected final RequestQueue mAlarmRequestQueue;
    protected Context mContext = null;

    public GeoLocation(RequestQueue requestQueue, Context context) {
        mAlarmRequestQueue = requestQueue;
        mContext = context;
    }

    abstract void updateLocation(final String identifier);

    abstract void onDestroy();

}
