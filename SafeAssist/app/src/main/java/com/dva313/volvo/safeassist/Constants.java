package com.dva313.volvo.safeassist;

/**
 * Created by Rickard on 2017-12-08.
 */

public class Constants {
    public static final String SERVICE_URL = "http://volvo.xdo.se/safeassist/appinterface.php";
    public static final String AUTH_KEY = "s2K4Jd092Kdc78sdKKCs2423";

    public static final int ALARM_ERROR = -1;
    public static final int ALARM_SET_ALARM_DELAY = 1;
    public static final int ALARM_ACKNOWLEDGE = 2;
    public static final int ALARM_ALARM_LEVEL_0 = 3;
    public static final int ALARM_ALARM_LEVEL_1 = 4;
    public static final int ALARM_ALARM_LEVEL_2 = 5;
    public static final int ALARM_ALARM_LEVEL_3 = 6;
    public static final int ALARM_NOTIFICATION = 7;
    public static final int ALARM_NO_RESPONSE = 8;

    public interface ACTION {
        public static String MAIN_ACTION = "com.dva313.volvo.safeassist.action.main";
        public static String STARTFOREGROUND_ACTION = "com.dva313.volvo.safeassist.action.main.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.dva313.volvo.safeassist.action.main.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }


}
