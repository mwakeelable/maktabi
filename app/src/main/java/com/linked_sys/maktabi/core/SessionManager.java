package com.linked_sys.maktabi.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.linked_sys.maktabi.activities.SignInActivity;

import java.util.HashMap;

public class SessionManager {
    public SharedPreferences pref;
    public SharedPreferences settings_pref;
    public SharedPreferences.Editor editor;
    public Context mContext;
    public int PRIVATE_MODE = 0;
    public final String PREF_NAME = AppController.TAG;
    public final String IS_LOGIN = "IsLoggedIn";
    public final String KEY_EMAIL = "email";
    public final String KEY_TOKEN = "TOKEN";
    public final String KEY_PASSWORD = "password";
    //USER KEYS
    public final String KEY_USER_ID = "userID";
    public final String KEY_NAME1 = "Name1";
    public final String KEY_NAME2 = "Name2";
    public final String KEY_NAME3 = "Name3";
    public final String KEY_NAME4 = "Name4";
    public final String KEY_IMAGE = "Image";
    public final String KEY_USER_TYPE = "UserType";
    public final String KEY_OFFICE_ID = "OfficeID";
    //CAPTAIN KEYS
    public final String KEY_CAPTAIN_ID = "captainID";
    public final String KEY_NAME = "Name";
    public final String KEY_UBER_NAME = "UberName";
    public final String KEY_CAREEM_NAME = "CareemName";
    public final String KEY_UBER_MOBILE = "UberMobile";
    public final String KEY_CAREEM_MOBILE = "CareemMobile";
    public final String KEY_NID = "NID";
    public final String KEY_CARD_NO = "CardNo";
    public final String KEY_CAREEM_ID = "CareemID";
    public final String KEY_BALANCE_TOTAL = "TotalBalance";


    public SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        settings_pref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void createLoginSession(String email, String password, String token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public void setUserData(String id, String name1, String name2, String name3, String name4, String image, String userType, String officeID) {
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_NAME1, name1);
        editor.putString(KEY_NAME2, name2);
        editor.putString(KEY_NAME3, name3);
        editor.putString(KEY_NAME4, name4);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_USER_TYPE, userType);
        editor.putString(KEY_OFFICE_ID, officeID);
        editor.commit();

    }

    public void setCaptainData(String id, String name, String uberName, String careemName, String uberMobile, String careemMobile, String nID, String cardNo, int careemID, String image) {
        editor.putString(KEY_CAPTAIN_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_UBER_NAME, uberName);
        editor.putString(KEY_CAREEM_NAME, careemName);
        editor.putString(KEY_UBER_MOBILE, uberMobile);
        editor.putString(KEY_CAREEM_MOBILE, careemMobile);
        editor.putString(KEY_NID, nID);
        editor.putString(KEY_CARD_NO, cardNo);
        editor.putInt(KEY_CAREEM_ID, careemID);
        editor.commit();

    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_NAME1, pref.getString(KEY_NAME1, null));
        user.put(KEY_NAME2, pref.getString(KEY_NAME2, null));
        user.put(KEY_NAME3, pref.getString(KEY_NAME3, null));
        user.put(KEY_NAME4, pref.getString(KEY_NAME4, null));
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_USER_TYPE, pref.getString(KEY_USER_TYPE, null));
        user.put(KEY_OFFICE_ID, pref.getString(KEY_OFFICE_ID, null));
        return user;
    }

    public HashMap<String, String> getCaptainDetails() {
        HashMap<String, String> captain = new HashMap<String, String>();
        captain.put(KEY_CAPTAIN_ID, pref.getString(KEY_CAPTAIN_ID, null));
        captain.put(KEY_NAME, pref.getString(KEY_NAME, null));
        captain.put(KEY_UBER_NAME, pref.getString(KEY_UBER_NAME, null));
        captain.put(KEY_CAREEM_NAME, pref.getString(KEY_CAREEM_NAME, null));
        captain.put(KEY_UBER_MOBILE, pref.getString(KEY_UBER_MOBILE, null));
        captain.put(KEY_CAREEM_MOBILE, pref.getString(KEY_CAREEM_MOBILE, null));
        captain.put(KEY_NID, pref.getString(KEY_NID, null));
        captain.put(KEY_CARD_NO, pref.getString(KEY_CARD_NO, null));
        captain.put(KEY_CAREEM_ID, String.valueOf(pref.getInt(KEY_CAREEM_ID, 0)));
        return captain;
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Intent i = new Intent(mContext, SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(mContext, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUserToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    public void setUserType(String userType) {
        editor.putString(KEY_USER_TYPE, userType);
        editor.commit();
    }

    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, "");
    }
}
