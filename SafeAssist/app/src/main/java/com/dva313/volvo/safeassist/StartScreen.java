package com.dva313.volvo.safeassist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class StartScreen extends AppCompatActivity {
    public static final String mBroadcastGpsAction = "com.dva313.volvo.string";
    private IntentFilter mIntentFilter;
    public TextView good_day_message, status, mDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastGpsAction);

        setContentView(R.layout.activity_start_screen);
        good_day_message = (TextView)findViewById(R.id.good_day_message_TW);
        status = (TextView)findViewById(R.id.status_TW);
        mDistance = (TextView)findViewById(R.id.textDistance);
        mDistance.setText("0.0m");
        //get the logged in users data
        SharedPreferences preferences = getSharedPreferences("workers_data", MODE_PRIVATE);
        String firstname = preferences.getString("first_name", null);
        String lastname = preferences.getString("last_name", null);

        status.setText("OK");
        status.setBackgroundColor(Color.parseColor("#0FFF07"));
        good_day_message.setText("Good day "+firstname+" "+lastname);

        //Toast.makeText(getApplicationContext(), "shit", Toast.LENGTH_SHORT).show();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BC", "Broadcast received!");
            // Get extra data included in the Intent
            String dist = intent.getStringExtra("Status");
            mDistance.setText(dist);
            Log.i("VALUE", dist);
//            String message = intent.getStringExtra("Status");
//            Bundle b = intent.getBundleExtra("Location");
//            lastKnownLoc = (Location) b.getParcelable("Location");
//            if (lastKnownLoc != null) {
//                tvLatitude.setText(String.valueOf(lastKnownLoc.getLatitude()));
//                tvLongitude
//                        .setText(String.valueOf(lastKnownLoc.getLongitude()));
//                tvAccuracy.setText(String.valueOf(lastKnownLoc.getAccuracy()));
//                tvTimestamp.setText((new Date(lastKnownLoc.getTime())
//                        .toString()));
//                tvProvider.setText(lastKnownLoc.getProvider());
//            }
//            tvStatus.setText(message);
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };
}
