package com.linked_sys.maktabi.core;

import java.util.HashMap;

public class CacheHelper {
    private static CacheHelper mInstance = null;

    public static CacheHelper getInstance() {
        if (mInstance == null)
            mInstance = new CacheHelper();
        return mInstance;
    }

    //USER TOKEN
    public String token = "";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SHARED_PREF = "ah_firebase";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    //UserData
    public HashMap<String, String> userData = new HashMap<>();
    public HashMap<String, String> captainData = new HashMap<>();
}
