package com.dva313.volvo.safeassist;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/**
 * Created by Dara on 2017-12-02.
 * Edited by Jonas on 2017-12-07.  Will put this in the backburner for now.
 * App will use GPSService.java until further notice.
 **/

public class Geolocation {
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder mBuilder;
    private SettingsClient client;
    private Task<LocationSettingsResponse> mTask;

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

    public void createLocationRequest(final Activity start) {
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mBuilder = new LocationSettingsRequest.Builder();
        client = LocationServices.getSettingsClient(start);
        mTask = client.checkLocationSettings(mBuilder.build());

        mTask.addOnSuccessListener(start, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        mTask.addOnFailureListener(start, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(start, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
}
