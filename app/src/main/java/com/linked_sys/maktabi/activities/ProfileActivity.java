package com.linked_sys.maktabi.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.linked_sys.maktabi.R;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

}
