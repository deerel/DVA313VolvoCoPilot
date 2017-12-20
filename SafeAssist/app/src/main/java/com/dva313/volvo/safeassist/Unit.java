package com.dva313.volvo.safeassist;

import android.content.Context;
import android.os.Handler;
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
 * Unit information
 *
 * <P>Stores information of the unit (device) tha app is executed on.
 *
 * @author Dara
 * @version 1.0
 * @since   2017-12-02
 */
class Unit {
    private static final Object lock = new Object();
    private static Unit instance = null;
    private Double mRadius;
    private String mIdentifier;
    private boolean mIsLoggedIn = false;
    private UnitType mUnitType;
    //abstract void setRadius(Double radius);
    //abstract Double getRadius();
    //abstract void setIdentifier(String identifier);
    //abstract String getIdentifier();
    protected Unit(UnitType unitType) {
        mUnitType = unitType;
    }

    String getIdentifier() {
        return mIdentifier;
    }

    UnitType getUnitType() {
        return mUnitType;
    }


}

