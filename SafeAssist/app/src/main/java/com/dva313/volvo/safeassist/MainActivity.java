package com.dva313.volvo.safeassist;

import android.Manifest;

import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;

import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * MainActivity
 *
 * <P>Contains the main features of the application, i.e. displaying alerts and starting
 * service for location and alarm update.
 *
 * @author Dara
 * @version 1.0
 * @since   2017-12-08
 */
public class MainActivity extends AppCompatActivity {
    private TextView mTextWelcome, mTextStatus, mDebug;
    private String mUsername;
    private int mApplicationState = Constants.STATE.INIT;
    private Unit mUnit = null;

    /* For Alarm Service */
    private int mAlertLevel;
    private boolean mIsBind = false;
    private Messenger mMessenger;
    private boolean mIsForeground = true;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();

        setContentView(R.layout.main_layout);
        mTextWelcome = findViewById(R.id.textViewWelcome);
        mTextStatus = findViewById(R.id.textViewStatus);
        mDebug = findViewById(R.id.textViewDebugInfo);
        mDebug.setText("");
        mAlertLevel = 0;


        //get the logged in users data
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        String firstname = preferences.getString("first_name", null);
        String lastname = preferences.getString("last_name", null);
        mUsername = preferences.getString("username", null);

        String unittype = preferences.getString("unittype", null);
        //Log.i("MainActivity", "Unittype: " + unittype);
        if(unittype.equals("handheld"))
            mUnit = new Unit(UnitType.HANDHELD);
        else if(unittype.equals("copilot"))
            mUnit = new Unit(UnitType.COPILOT);
        else {
            Log.i("MainActivity", "No unittype in prefs.");
        }


        setStatus();
        mTextWelcome.setText("Logged in as " + mUsername);


        /* Foreground service*/
        Intent startIntent = new Intent(this, ServerComService.class);
        bindService(startIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startIntent.putExtra("workerId", mUsername);
        startIntent.putExtra("unittype", mUnit.getUnitType());
        startService(startIntent);

    }

    /* Custom Action bar start */
    void appToBackground() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        if (id == R.id.action_home) {
            appToBackground();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /* Custom Action bar end */

    public void logout() {


        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        //String username = preferences.getString("username", null);
        String unittype = preferences.getString("unittype", null);


        if(unittype == "copilot"){
            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.SERVICE_URL, new Response.Listener<String>() {

                @Override//the response from php is received here
                public void onResponse(String response) {

                    if(response.contains("Removing successful")){
                        // Shut down foreground service
                        finishAlarmService();
                        Intent stopIntent = new Intent(getApplicationContext(), ServerComService.class);
                        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                        startService(stopIntent);

                        SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
                        editor.putBoolean("is_inlogged", false);
                        editor.apply();

                        unbindService(serviceConnection);
                        mIsBind = false;
                        //mMessenger = null;
                        mIsForeground = false;

                        //start the first page
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), "Could not remove the association.", Toast.LENGTH_LONG).show();
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
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<>();
                    // the POST parameters:
                    params.put("action", "removeAssoc");
                    params.put("worker_id", mUsername);
                    //params.put("password", password);
                    params.put("key", Constants.AUTH_KEY);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(postRequest);

        }else{
            // Shut down foreground service
            finishAlarmService();
            Intent stopIntent = new Intent(this, ServerComService.class);
            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);

            SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
            editor.putBoolean("is_inlogged", false);
            editor.apply();

            unbindService(serviceConnection);
            mIsBind = false;
            //mMessenger = null;
            mIsForeground = false;

            //start the first page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setStatus() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mTextStatus.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDarkText, null));

        switch (mAlertLevel) {
            case Constants.ALARM_NO_RESPONSE:
                mTextStatus.setText(R.string.no_signal);
                mTextStatus.setBackgroundResource(R.drawable.alert_box_1);
                break;
            case Constants.ALARM_ALARM_LEVEL_0:
                if(mApplicationState != Constants.STATE.ALARM_ALARM_LEVEL_0) {
                    mApplicationState = Constants.STATE.ALARM_ALARM_LEVEL_0;
                    //v.vibrate(100);
                    mTextStatus.setText(R.string.alert_0);
                    mTextStatus.setBackgroundResource(R.drawable.alert_box_0);
                }
                break;
            case Constants.ALARM_ALARM_LEVEL_1:
                if(mApplicationState != Constants.STATE.ALARM_ALARM_LEVEL_1) {
                    mApplicationState = Constants.STATE.ALARM_ALARM_LEVEL_1;
                    //v.vibrate(100);
                    mTextStatus.setText(R.string.alert_1);
                    mTextStatus.setBackgroundResource(R.drawable.alert_box_1);
                }
                break;
            case Constants.ALARM_ALARM_LEVEL_2:
                if(mApplicationState != Constants.STATE.ALARM_ALARM_LEVEL_2) {
                    mApplicationState = Constants.STATE.ALARM_ALARM_LEVEL_2;
                    //v.vibrate(500);
                    mTextStatus.setText(R.string.alert_2);
                    mTextStatus.setBackgroundResource(R.drawable.alert_box_2);
                }
                break;
            case Constants.ALARM_ALARM_LEVEL_3:
                if(mApplicationState != Constants.STATE.ALARM_ALARM_LEVEL_3) {
                    mApplicationState = Constants.STATE.ALARM_ALARM_LEVEL_3;
                    //v.vibrate(1000);
                    mTextStatus.setText(R.string.alert_3);
                    mTextStatus.setBackgroundResource(R.drawable.alert_box_3);
                }
                break;
            case Constants.ALARM_NOTIFICATION:
                //v.vibrate(100);
                mTextStatus.setText("");
                mTextStatus.setBackgroundResource(R.drawable.alert_box_0);
                break;
            case Constants.ALARM_INIT:
                //v.vibrate(100);
                mTextStatus.setText(R.string.initializing);
                mTextStatus.setBackgroundResource(R.drawable.alert_box_0);
                break;
            default:
                if(mApplicationState != Constants.STATE.ALARM_NO_SIGNAL) {
                    mApplicationState = Constants.STATE.ALARM_NO_SIGNAL;
                    //v.vibrate(100);
                    mTextStatus.setText(R.string.no_signal);
                    mTextStatus.setBackgroundResource(R.drawable.alert_box_1);
                }
                break;
        }

        /* Move app to foreground if alarm level is high */
        if (!mIsForeground && (mAlertLevel == Constants.ALARM_ALARM_LEVEL_2 || mAlertLevel == Constants.ALARM_ALARM_LEVEL_3)) {
            Log.i("MainActivity", "Alert Level: " + mAlertLevel);
            mIsForeground = true;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }


    }

    @Override
    protected void onPause() {
        mIsForeground = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        mIsForeground = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
//        unbindService(serviceConnection);
//        mIsBind = false;
//        mMessenger = null;
        mIsForeground = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //super.onDestroy();
        if (mIsBind)
            unbindService(serviceConnection);
        mIsBind = false;
        mIsForeground = false;
        //mMessenger = null;
        super.onDestroy();

    }

    /* To bind to Alarm Service to be able to communicate between processes */
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMessenger = new Messenger(iBinder);
            mIsBind = true;
            initAlarmService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //mMessenger = null;
            //mIsBind = false;
        }
    };

    /* The receiver of alarms from ServerComService */
    class ResponseHandler extends Handler {
        int message;

        @Override
        public void handleMessage(Message msg) {
            message = msg.getData().getInt("Response_message");
            Boolean sendNewDelay = alarmChanged(message);
            mAlertLevel = message;
            if(sendNewDelay) {
                Log.i("MainActivity", "New Delay to Alarm Service");
                newDelayAlarmService(newDelay());
            } else {
                Log.i("MainActivity", "Ack to Alarm Service");
                acknowledgeAlarmService();
            }
            setStatus();
            super.handleMessage(msg);
        }
    }

    @NonNull
    private Boolean alarmChanged(int newLevel) {
        Log.i("MainActivity", "mAlarmLevel: " + mAlertLevel + " newLevel: " + newLevel);
        return mAlertLevel != newLevel;

    }

    private int newDelay() {
        int delay = 0;

        switch (mAlertLevel) {
            case Constants.ALARM_INIT:
                delay = Constants.DELAY.INIT;
                break;
            case Constants.ALARM_ALARM_LEVEL_0:
                delay = Constants.DELAY.ALARM_ALARM_LEVEL_0;
                break;
            case Constants.ALARM_ALARM_LEVEL_1:
                delay = Constants.DELAY.ALARM_ALARM_LEVEL_1;
                break;
            case Constants.ALARM_ALARM_LEVEL_2:
                delay = Constants.DELAY.ALARM_ALARM_LEVEL_2;
                break;
            case Constants.ALARM_ALARM_LEVEL_3:
                delay = Constants.DELAY.ALARM_ALARM_LEVEL_3;
                break;
            default:
                delay = Constants.DELAY.ALARM_DEFAULT;
                break;
        }
        mDebug.setText("Update interval: " + delay + " ms");
        return delay;
    }

    /* Initiate the Alarm Service communication */
    private void initAlarmService() {

        Message msg;
        Bundle bundle = new Bundle();
        bundle.putString("Response_message", "1000");
        bundle.putString("Worker_id", mUsername);
        msg = Message.obtain(null, Constants.ALARM_SET_ALARM_DELAY);
        msg.setData(bundle);
        msg.replyTo = new Messenger(new ResponseHandler());
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* Send acknowledgement of received alarm to ServerComService */
    private void acknowledgeAlarmService() {
        Message msg;
        //Bundle bundle = new Bundle();
        //bundle.putString("Response_message", "5000");
        msg = Message.obtain(null, Constants.ALARM_ACKNOWLEDGE);
        //msg.setData(bundle);
        msg.replyTo = new Messenger(new ResponseHandler());
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* Send acknowledgement of received alarm to ServerComService */
    private void newDelayAlarmService(int delay) {
        Message msg;
        Bundle bundle = new Bundle();
        bundle.putString("Response_message", ""+delay);
        msg = Message.obtain(null, Constants.ALARM_SET_ALARM_DELAY);
        msg.setData(bundle);
        msg.replyTo = new Messenger(new ResponseHandler());
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void finishAlarmService() {
        Message msg;
        //Bundle bundle = new Bundle();
        //bundle.putString("Response_message", ""+delay);
        msg = Message.obtain(null, Constants.ALARM_FINISH);
        //msg.setData(bundle);
        msg.replyTo = new Messenger(new ResponseHandler());
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* Permissions check */
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.VIBRATE,
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    /* Handle response from permissions check */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    // What to do if we don't get the permissions. Exit app?

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
