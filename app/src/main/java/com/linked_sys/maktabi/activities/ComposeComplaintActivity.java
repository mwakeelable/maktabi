package com.linked_sys.maktabi.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.core.AppController;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComposeComplaintActivity extends BaseActivity {
    EditText compTitleTXT, compBodyTXT;
    LinearLayout btnComposeComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        compTitleTXT = (EditText) findViewById(R.id.txt_subject);
        compBodyTXT = (EditText) findViewById(R.id.txt_body);
        btnComposeComp = (LinearLayout) findViewById(R.id.btnComposeComplaint);
        btnComposeComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeComplaint();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_compose_complaint;
    }

    private void composeComplaint() {
        String title = compTitleTXT.getEditableText().toString();
        String body = compBodyTXT.getEditableText().toString();
        String officeId = CacheHelper.getInstance().userData.get(session.KEY_OFFICE_ID);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("Title", title);
        map.put("Body", body);
        map.put("UserID", String.valueOf(CacheHelper.getInstance().userData.get(session.KEY_USER_ID)));
        map.put("OfficeId", officeId);
        ApiHelper api = new ApiHelper(this, ApiEndPoints.POST_COMPLAINT, Request.Method.POST, map, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                Log.d(AppController.TAG, response.toString());
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.d(AppController.TAG, "FAILED");
            }
        });
        api.executePostRequest(true);
    }

}
