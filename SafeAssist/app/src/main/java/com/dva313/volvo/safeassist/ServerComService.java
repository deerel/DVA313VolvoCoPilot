package com.dva313.volvo.safeassist;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by Rickard on 2017-12-07.
 */

public class ServerComService extends Service {

    private static final String LOG_TAG = "ServerComService";

    private int mSleepTime = 500;
    private String mWorkerId = null;
    private UnitType mUnitType = null;
    private RequestQueue mAlarmRequestQueue;
    AlarmUpdateThread mAlarmUpdateThread = null;


    private final Messenger messenger = new Messenger(new IncomingHandler());
    static Message mMessageReceived = null;

    private GeoLocation mGeoLocation = null;
    private Alarm mAlarm = null;

    @Override
    public void onCreate() {
        super.onCreate();


        /* Create a queue for http-requests for communication with remote server */
        // Instantiate the cache
        Cache mAlarmCache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network mAlarmNetwork = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mAlarmRequestQueue = new RequestQueue(mAlarmCache, mAlarmNetwork);
        // Start the queue
        mAlarmRequestQueue.start();
    }

    /* Is called when an activity is finishing (someone called finish() on it, or because
    *  the system is temporarily destroying this instance of the activity to save space.*/
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /* Starting up the service and creates a sticky notification in the status bar*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* Setup for status bar notification, necessary for foreground service*/
        if (intent.getAction(). equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_safeassist);

            Notification notification = new NotificationCompat.Builder(this, "fgservice")
                    .setContentTitle("Volvo SafeAssist")
                    .setTicker("Volvo SafeAssist")
                    .setContentText("Running")
                    .setSmallIcon(R.drawable.ic_safeassist)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true).build();

            // Staring the foreground service
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            // Fetching worker id to identify user on the remote server
            mWorkerId = intent.getStringExtra("workerId");
            mUnitType = (UnitType) intent.getSerializableExtra("unittype");
            // Starting location tracking
            if(mUnitType == UnitType.HANDHELD)
                mGeoLocation = new HandheldLocation(mAlarmRequestQueue, getApplicationContext());
            else if(mUnitType == UnitType.COPILOT)
                mGeoLocation = new CoPilotLocation(mAlarmRequestQueue, getApplicationContext());
            else
            {
                // Unit not identified
                Log.i("ServerComService", "Unit could not be identified");
            }
            mAlarm = new Alarm(mAlarmRequestQueue, getApplicationContext());

        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            mGeoLocation.onDestroy();
            mGeoLocation = null;
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    /* Bind messenger to MainActivity to be able to send and receive messages */
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler implements Callback {

        Message incomingMessage;
        int alarm = 0;

        @Override
        public void handleMessage(Message msg) {
            incomingMessage = msg;

            switch (msg.what) {
                case Constants.ALARM_SET_ALARM_DELAY:
                    String delay = msg.getData().getString("Response_message");
                    if( msg.getData().containsKey("Worker_id")) {
                        mWorkerId = msg.getData().getString("Worker_id");
                    }
                    mSleepTime = Integer.parseInt(delay);
                    break;
                case Constants.ALARM_ACKNOWLEDGE:
                    /* Sender is ready for a new message */
                    break;
                default:
                    super.handleMessage(msg);
            }

            mAlarmUpdateThread = new AlarmUpdateThread(this, mSleepTime);
            mAlarmUpdateThread.run();

        }

        /* Waiting for location to be sent to server and alarm to be received */
        @Override
        public void callback(int alarm) {
            Message replyMessage;
            Bundle replyBundle = new Bundle();
            replyMessage = Message.obtain(null, alarm);
            replyBundle.putInt("Response_message", alarm);
            replyMessage.setData(replyBundle);

            try {
                incomingMessage.replyTo.send(replyMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }


    /* Interface for the alarm thread to communicate with this class (ServerComService) */
    interface Callback {
        void callback(int alarm);
    }

    /* Class that runs a separate thread to update location and fetching alarm status */
    class AlarmUpdateThread implements Runnable {

        final Callback mCallback;
        final int mDelay;

        public AlarmUpdateThread(Callback c, int delay) {
            this.mCallback = c;
            this.mDelay = delay;
        }

        public void run() {
            if(mGeoLocation != null) {
                mGeoLocation.updateLocation(mWorkerId);
            }
            int alarm = mAlarm.fetchAlarm(mWorkerId);
            this.mCallback.callback(alarm); // callback
            // Sleep to not spam and drain battery
            try
            {
                Thread.sleep(mDelay);
            }
            catch (Exception e)
            {
                Log.e("AlarmUpdateThread: ", e.getMessage());
            }
        }
    }

}
