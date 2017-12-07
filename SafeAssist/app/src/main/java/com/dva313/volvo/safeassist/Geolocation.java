package com.dva313.volvo.safeassist;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Created by Dara on 2017-12-02.
 * Edited by Jonas on 2017-12-07.
 */

public class Geolocation {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    public Geolocation(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
        this.mLocationRequest = new LocationRequest();
    }

    public void getLocation(Activity start) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(start);

        if ( ContextCompat.checkSelfPermission( start, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            int REQUEST_LOCATION = 1;
            ActivityCompat.requestPermissions( start, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, REQUEST_LOCATION);
        }
        mFusedLocationClient.getLastLocation();
    }

    public void createLocationRequest() {
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
