package com.dva313.volvo.safeassist;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Timer;

/**
 * Created by Rickard on 2017-12-07.
 */

public class AlarmService extends Service {
    private final int SET_ALARM_DELAY = 1;
    private final int ACKNOWLEDGE = 2;
    private final int ALARM_LEVEL_0 = 3;
    private final int ALARM_LEVEL_1 = 4;
    private final int ALARM_LEVEL_2 = 5;
    private final int NOTIFICATION = 6;
    private final int NO_RESPONSE = 7;

    int mSleepTime = 5000;

    Messenger messenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Message MSG;
            String message;
            Bundle bundle = new Bundle();

            switch (msg.what) {
                case SET_ALARM_DELAY:
                    String delay = msg.getData().getString("Response_message");
                    mSleepTime = Integer.parseInt(delay);
                    break;
                case ACKNOWLEDGE:
                    /* Sender is ready for a new message */
                    break;
                default:
                    super.handleMessage(msg);
            }

            // Get Alarm Level from server and respond to sender
            switch (fetchAlarm()) {
                case NOTIFICATION:
                    message = "Just a notification";
                    MSG = Message.obtain(null, NOTIFICATION);
                    bundle.putString("Response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ALARM_LEVEL_0:
                    message = "No Alarm";
                    MSG = Message.obtain(null, ALARM_LEVEL_0);
                    bundle.putString("Response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ALARM_LEVEL_1:
                    message = "Alarm Level 1";
                    MSG = Message.obtain(null, ALARM_LEVEL_1);
                    bundle.putString("Response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ALARM_LEVEL_2:
                    message = "No Alarm";
                    MSG = Message.obtain(null, ALARM_LEVEL_2);
                    bundle.putString("Response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    message = "No response!";
                    MSG = Message.obtain(null, NO_RESPONSE);
                    bundle.putString("Response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }
    }

    private int fetchAlarm() {
        // Sleep to not spam and drain battery
        try
        {
            Thread.sleep(mSleepTime);
        }
        catch (Exception ex)
        {
            Log.e("Error: ", ex.getMessage());
        }

        return ALARM_LEVEL_0;
    }
}
