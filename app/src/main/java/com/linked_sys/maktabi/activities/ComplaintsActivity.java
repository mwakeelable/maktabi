package com.linked_sys.maktabi.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.adapters.ComplaintsAdapter;
import com.linked_sys.maktabi.core.AppController;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.models.Complaint;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComplaintsActivity extends BaseActivity implements ComplaintsAdapter.ComplaintsAdapterListener, SearchView.OnQueryTextListener {
    public ArrayList<Complaint> complaintList = new ArrayList<>();
    private RecyclerView recyclerView;
    public ComplaintsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    int limit = 10;
    int skip = 0;
    boolean loadMore = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    LinearLayout placeholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(ComposeComplaintActivity.class);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        placeholder = (LinearLayout) findViewById(R.id.no_data_placeholder);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mAdapter = new ComplaintsAdapter(this, complaintList, this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getComplaints();
                    }
                }
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                limit = 10;
                skip = 0;
                getComplaints();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loadMore) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            skip = skip + limit;
                            loadMoreComplaints();

                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ComplaintsActivity.this.finish();
                hideSoftKeyboard(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getComplaints() {
        String balanceUrl = ApiEndPoints.GET_CAPTAIN_COMPLAINTS
                + "?UserID=" + CacheHelper.getInstance().userData.get(session.KEY_USER_ID)
                + "&Skip=" + skip
                + "&length=" + limit;
        ApiHelper api = new ApiHelper(this, balanceUrl, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                complaintList.clear();
                JSONObject obj = (JSONObject) response;
                try {
                    JSONArray compArray = obj.optJSONArray("ComplainsList");
                    if (compArray.length() > 0) {
                        placeholder.setVisibility(View.GONE);
                        for (int i = 0; i < compArray.length(); i++) {
                            JSONObject compObj = compArray.optJSONObject(i);
                            Complaint complaint = new Complaint();
                            complaint.setCompID(compObj.optInt("ID"));
                            complaint.setCompTitle(compObj.optString("Title"));
                            complaint.setCompDate(compObj.optString("PostDate"));
                            complaint.setSolved(compObj.optBoolean("Solved"));
                            complaint.setStatusID(compObj.optInt("StatusID"));
                            complaintList.add(complaint);
                        }
                        recyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        if (compArray.length() < 10)
                            loadMore = false;
                        else
                            loadMore = true;
                    } else {
                        placeholder.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    Log.d(AppController.TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        api.executeRequest(true, false);
    }

    private void loadMoreComplaints() {
        String balanceUrl = ApiEndPoints.GET_CAPTAIN_BALANCE
                + "?CaptainID=" + CacheHelper.getInstance().captainData.get(session.KEY_CAPTAIN_ID)
                + "&Skip=" + skip
                + "&length=" + limit;
        ApiHelper api = new ApiHelper(this, balanceUrl, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                complaintList.clear();
                JSONObject obj = (JSONObject) response;
                try {
                    JSONArray compArray = obj.optJSONArray("ComplainsList");
                    if (compArray.length() > 0) {
                        placeholder.setVisibility(View.GONE);
                        for (int i = 0; i < compArray.length(); i++) {
                            JSONObject compObj = compArray.optJSONObject(i);
                            Complaint complaint = new Complaint();
                            complaint.setCompID(compObj.optInt("ID"));
                            complaint.setCompTitle(compObj.optString("Title"));
                            complaint.setCompDate(compObj.optString("PostDate"));
                            complaint.setSolved(compObj.optBoolean("Solved"));
                            complaint.setStatusID(compObj.optInt("StatusID"));
                            complaintList.add(complaint);
                        }
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        if (compArray.length() < 10)
                            loadMore = false;
                        else
                            loadMore = true;
                    } else {
                        placeholder.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    Log.d(AppController.TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        api.executeRequest(true, false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_complaints;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onCompRowClicked(int position) {
        Intent intent = new Intent(this, CompDetailsActivity.class);
        intent.putExtra("ID", mAdapter.filteredList.get(position).getCompID());
        intent.putExtra("statusID", mAdapter.filteredList.get(position).getStatusID());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getComplaints();
//        mAdapter.notifyDataSetChanged();
    }
}
