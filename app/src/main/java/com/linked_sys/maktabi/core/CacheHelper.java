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

    //UserData
    public HashMap<String, String> userData = new HashMap<>();
    public HashMap<String, String> captainData = new HashMap<>();
}
