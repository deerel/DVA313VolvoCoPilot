package com.dva313.volvo.safeassist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class StartScreen extends AppCompatActivity {
    public TextView good_day_message, status, mDistance;
    public String mAlertLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_screen);
        good_day_message = (TextView)findViewById(R.id.good_day_message_TW);
        status = (TextView)findViewById(R.id.status_TW);
        mDistance = (TextView)findViewById(R.id.textDistance);
        mDistance.setText("");
        mAlertLevel = "0";
        //get the logged in users data
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        String firstname = preferences.getString("first_name", null);
        String lastname = preferences.getString("last_name", null);

        //status.setText("OK");
        //status.setBackgroundColor(Color.parseColor("#0FFF07"));
        setStatus();
        good_day_message.setText("Logged in as "+firstname+" "+lastname);

        startService(new Intent(this, GPSService.class));
    }



    public void logout(View v){
        SharedPreferences.Editor editor = getSharedPreferences("workers_data", MODE_PRIVATE).edit();
        editor.putBoolean("is_inlogged", false);
        editor.apply();

        //start the first page
        Intent intent = new Intent(StartScreen.this, FirstPage.class);
        startActivity(intent);
        finish();
    }

    private void setStatus() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        status.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDarkText, null));

        switch (mAlertLevel) {
            case "0" :
                status.setText("Not inside a working area.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert0, null));
                break;
            case "1" :
                v.vibrate(100);
                status.setText("Caution!\nYou are inside a working area.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert1, null));
                break;
            case "2" :
                v.vibrate(1000);
                status.setText("Alert!\nLook out for vehicles!");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert2, null));
                break;
            default:
                v.vibrate(100);
                status.setText("Caution!\nSystem cannot detect your position.");
                status.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAlert1, null));
                break;
        }
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("gps-distance"));
        super.onResume();
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            mAlertLevel = message.split(",")[0];
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            setStatus();
            mDistance.setText(message.split(",")[1]);
        }
    };



}
