package com.linked_sys.maktabi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.linked_sys.maktabi.core.AppController;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.core.SessionManager;
import com.linked_sys.maktabi.core.SharedManager;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import org.json.JSONObject;

public abstract class BaseActivity extends AppCompatActivity {
    SessionManager session;
    SharedManager sharedManager;
    Bitmap userAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        session = new SessionManager(this);
        sharedManager = new SharedManager();
        sharedManager.setLanguage(this, "ar");
    }

    protected abstract int getLayoutID();

    public void openActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void restartActivity(Intent intent) {
        finish();
        startActivity(intent);
    }

    public void showToast(String msg, int time) {
        Toast.makeText(this, msg, time).show();
    }

    public void getUserData() {
        if (session.getUserToken() != null)
            CacheHelper.getInstance().token = session.getUserToken();
        ApiHelper api = new ApiHelper(this, ApiEndPoints.GET_USER_DATA, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                CacheHelper.getInstance().userData.clear();
                CacheHelper.getInstance().captainData.clear();
                JSONObject res = (JSONObject) response;
                try {
                    JSONObject userDataObj = res.optJSONObject("UserData");
                    JSONObject captainDataObj = res.optJSONObject("CaptainData");

                    session.setUserType(userDataObj.optString("UserType"));

                    CacheHelper.getInstance().userData.put(session.KEY_USER_ID, String.valueOf(userDataObj.optInt("Id")));
                    CacheHelper.getInstance().userData.put(session.KEY_NAME1, userDataObj.optString("Name1"));
                    CacheHelper.getInstance().userData.put(session.KEY_NAME2, userDataObj.optString("Name2"));
                    CacheHelper.getInstance().userData.put(session.KEY_NAME3, userDataObj.optString("Name3"));
                    CacheHelper.getInstance().userData.put(session.KEY_NAME4, userDataObj.optString("Name4"));
                    CacheHelper.getInstance().userData.put(session.KEY_IMAGE, userDataObj.optString("Image"));
                    CacheHelper.getInstance().userData.put(session.KEY_USER_TYPE, userDataObj.optString("UserType"));
                    CacheHelper.getInstance().userData.put(session.KEY_OFFICE_ID, userDataObj.optString("OfficeID"));

                    CacheHelper.getInstance().captainData.put(session.KEY_CAPTAIN_ID, String.valueOf(captainDataObj.optInt("ID")));
                    CacheHelper.getInstance().captainData.put(session.KEY_NAME, captainDataObj.optString("Name"));
                    CacheHelper.getInstance().captainData.put(session.KEY_UBER_NAME, captainDataObj.optString("UberName"));
                    CacheHelper.getInstance().captainData.put(session.KEY_CAREEM_NAME, captainDataObj.optString("CareemName"));
                    CacheHelper.getInstance().captainData.put(session.KEY_UBER_MOBILE, captainDataObj.optString("UberMobile"));
                    CacheHelper.getInstance().captainData.put(session.KEY_CAREEM_MOBILE, captainDataObj.optString("CareemMobile"));
                    CacheHelper.getInstance().captainData.put(session.KEY_NID, captainDataObj.optString("NID"));
                    CacheHelper.getInstance().captainData.put(session.KEY_CARD_NO, captainDataObj.optString("CardNo"));
                    CacheHelper.getInstance().captainData.put(session.KEY_CAREEM_ID, String.valueOf(captainDataObj.optInt("CareemID")));
                    CacheHelper.getInstance().captainData.put(session.KEY_BALANCE_TOTAL, String.valueOf(res.opt("TotalBalance")));

                    openActivity(MainActivity.class);
                    finish();
                } catch (Exception e) {
                    Log.d(AppController.TAG, e.getMessage().toString());
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.d(AppController.TAG, "Error");
            }
        });
        api.executeRequest(false, false);
    }
}
