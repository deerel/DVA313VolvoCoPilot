package com.dva313.volvo.safeassist;

/**
 * Created by Rickard on 2017-12-08.
 */

class Constants {
    static final String SERVICE_URL = "http://volvo.xdo.se/safeassist/appinterface.php";
    static final String AUTH_KEY = "s2K4Jd092Kdc78sdKKCs2423";

    static final int ALARM_ERROR = -1;
    static final int ALARM_INIT = 0;
    static final int ALARM_SET_ALARM_DELAY = 1;
    static final int ALARM_ACKNOWLEDGE = 2;
    static final int ALARM_ALARM_LEVEL_0 = 3;
    static final int ALARM_ALARM_LEVEL_1 = 4;
    static final int ALARM_ALARM_LEVEL_2 = 5;
    static final int ALARM_ALARM_LEVEL_3 = 6;
    static final int ALARM_NOTIFICATION = 7;
    static final int ALARM_NO_RESPONSE = 8;
    static final int ALARM_FINISH = 10;

    interface STATE {
        int INIT = 0;
        int ALARM_ALARM_LEVEL_0 = 100;
        int ALARM_ALARM_LEVEL_1 = 200;
        int ALARM_ALARM_LEVEL_2 = 300;
        int ALARM_ALARM_LEVEL_3 = 400;
        int ALARM_NO_SIGNAL = 1000;
    }

    interface DELAY {
        int INIT = 100;
        int ALARM_ALARM_LEVEL_0 = 5000;
        int ALARM_ALARM_LEVEL_1 = 5000;
        int ALARM_ALARM_LEVEL_2 = 2000;
        int ALARM_ALARM_LEVEL_3 = 1000;
        int ALARM_DEFAULT = 1000;
    }

    interface ACTION {
        String MAIN_ACTION = "com.dva313.volvo.safeassist.action.main";
        String STARTFOREGROUND_ACTION = "com.dva313.volvo.safeassist.action.main.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.dva313.volvo.safeassist.action.main.action.stopforeground";
    }

    interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }


}
