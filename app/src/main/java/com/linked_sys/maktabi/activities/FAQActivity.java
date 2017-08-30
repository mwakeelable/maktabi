package com.linked_sys.maktabi.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.adapters.FAQAdapter;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQActivity extends BaseActivity {
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpandableListView expandableList;
    private int lastExpandedPosition = -1;
    FAQAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expandableList = (ExpandableListView) findViewById(R.id.list);
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        adapter = new FAQAdapter(this, listDataHeader, listDataChild);
        expandableList.setAdapter(adapter);
        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        getFAQ();
    }


    private void getFAQ() {
        String url = ApiEndPoints.GET_FAQ + "";
        ApiHelper api = new ApiHelper(this, url, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                JSONObject res = (JSONObject) response;
                try {
                    JSONArray faqObj = res.optJSONArray("GetFAQList");
                    for (int i = 0; i < faqObj.length(); i++) {
                        JSONObject jsonObject = faqObj.getJSONObject(i);
                        listDataHeader.add(jsonObject.optString("Subject"));
                        List<String> answer = new ArrayList<String>();
                        answer.add(jsonObject.optString("Body"));
                        listDataChild.put(listDataHeader.get(i), answer);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(VolleyError error) {

            }
        });
        api.executeRequest(true, false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_faq;
    }

}
