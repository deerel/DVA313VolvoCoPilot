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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class StartScreen extends AppCompatActivity {
    public TextView good_day_message, status, mDistance;

    public String mUsername;

    /* For Alarm Service */
    public int mAlertLevel;
    boolean mIsBind = false;
    Messenger mMessenger;
    Message mAlarmReply;
    boolean mIsForeground = true;

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();

        setContentView(R.layout.activity_start_screen);
        good_day_message = (TextView) findViewById(R.id.good_day_message_TW);
        status = (TextView) findViewById(R.id.status_TW);
        mDistance = (TextView) findViewById(R.id.textDistance);
        mDistance.setText("");
        mAlertLevel = 0;
        //get the logged in users data
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        String firstname = preferences.getString("first_name", null);
        String lastname = preferences.getString("last_name", null);
        mUsername = preferences.getString("username", null);
        //status.setText("OK");
        //status.setBackgroundColor(Color.parseColor("#0FFF07"));
        setStatus();
        good_day_message.setText("Logged in as " + mUsername);


        /* Foreground service*/
        Intent startIntent = new Intent(this, AlarmService.class);
        bindService(startIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);

    }


    public void logout(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
        editor.putBoolean("is_inlogged", false);
        editor.apply();

        // Shut down foreground service
        Intent stopIntent = new Intent(this, AlarmService.class);
        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(stopIntent);

        unbindService(serviceConnection);
        mIsBind = false;
        //mMessenger = null;
        mIsForeground = false;

        //start the first page
        Intent intent = new Intent(StartScreen.this, FirstPage.class);
        startActivity(intent);
        finish();
    }

    private void setStatus() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        status.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDarkText, null));

        switch (mAlertLevel) {
            case Constants.ALARM_NO_RESPONSE:
                status.setText("Caution!\nLost signal.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert1, null));
                break;
            case Constants.ALARM_ALARM_LEVEL_0:
                //v.vibrate(100);
                status.setText("You are outside a working area.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert0, null));
                break;
            case Constants.ALARM_ALARM_LEVEL_1:
                //v.vibrate(100);
                status.setText("Caution!\nYou are inside a working area.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert1, null));
                break;
            case Constants.ALARM_ALARM_LEVEL_2:
                //v.vibrate(500);
                status.setText("Alert!\nLook out for vehicles!");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert2, null));
                break;
            case Constants.ALARM_ALARM_LEVEL_3:
                //v.vibrate(1000);
                status.setText("Alert!\nVehicles too close!");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert2, null));
                break;
            case Constants.ALARM_NOTIFICATION:
                //v.vibrate(100);
                status.setText("Alert!\nLook out for vehicles!");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert0, null));
                break;
            default:
                //v.vibrate(100);
                status.setText("Caution!\nSystem cannot detect your position.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert1, null));
                break;
        }
        Log.i("StartScreen", "Alert Level: " + mAlertLevel);
        if (!mIsForeground && (mAlertLevel == Constants.ALARM_ALARM_LEVEL_2 || mAlertLevel == Constants.ALARM_ALARM_LEVEL_3)) {
            Log.i("StartScreen", "Alert Level: " + mAlertLevel);
            mIsForeground = true;
            Intent intent = new Intent(getApplicationContext(), StartScreen.class);
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
        super.onDestroy();
        if (mIsBind)
            unbindService(serviceConnection);
        mIsBind = false;
        mIsForeground = false;
        //mMessenger = null;

    }

    /* To bind to Alarm Service to be able to communicate between processes */
    ServiceConnection serviceConnection = new ServiceConnection() {
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

    /* The receiver of alarms from AlarmService */
    class ResponseHandler extends Handler {
        int message;

        @Override
        public void handleMessage(Message msg) {

//            switch (msg.what) {
//                case Constants.ALARM_NOTIFICATION:
//                    message = msg.getData().getInt("Response_message");
//                    break;
//                case Constants.ALARM_ALARM_LEVEL_0:
//                    message = msg.getData().getInt("Response_message");
//                    break;
//                case Constants.ALARM_ALARM_LEVEL_1:
//                    message = msg.getData().getInt("Response_message");
//                    break;
//                case Constants.ALARM_ALARM_LEVEL_2:
//                    message = msg.getData().getInt("Response_message");
//                    break;
//                case Constants.ALARM_ALARM_LEVEL_3:
//                    message = msg.getData().getInt("Response_message");
//                    break;
//                case Constants.ALARM_NO_RESPONSE:
//                    message = msg.getData().getInt("Response_message");
//                    break;
//                default:
//                    message = Constants.ALARM_ERROR;
//            }
            message = msg.getData().getInt("Response_message");
            Log.i("StartScree", "message: " + message + " mAlertLevel: " + mAlertLevel);
            mAlertLevel = message;
            acknowledgeAlarmService();
            setStatus();
            super.handleMessage(msg);

        }

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

    /* Send acknowledgement of received alarm to AlarmService */
    private void acknowledgeAlarmService() {

        Message msg;
        Bundle bundle = new Bundle();
        bundle.putString("Response_message", "1000");
        msg = Message.obtain(null, Constants.ALARM_ACKNOWLEDGE);
        msg.setData(bundle);
        msg.replyTo = new Messenger(new ResponseHandler());
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* Permissions check */
    public void checkPermissions() {
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
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
