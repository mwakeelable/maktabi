package com.linked_sys.maktabi.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.adapters.BalanceAdapter;
import com.linked_sys.maktabi.core.AppController;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.models.CaptainBalance;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BalanceActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BalanceAdapter.BalanceAdapterListener, SearchView.OnQueryTextListener {
    TextView txtTotalBalance;
    public ArrayList<CaptainBalance> balanceList = new ArrayList<>();
    private RecyclerView recyclerView;
    public BalanceAdapter mAdapter;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTotalBalance = (TextView) findViewById(R.id.totalBalanceTxt);
        txtTotalBalance.setText(CacheHelper.getInstance().captainData.get(session.KEY_BALANCE_TOTAL));
        placeholder = (LinearLayout) findViewById(R.id.no_data_placeholder);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mAdapter = new BalanceAdapter(this, balanceList, this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getBalance();
                    }
                }
        );
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
                            loadMoreBalance();

                        }
                    }
                }
            }
        });
    }

    private void getBalance() {
        String balanceUrl = ApiEndPoints.GET_CAPTAIN_BALANCE
                + "?CaptainID=" + CacheHelper.getInstance().captainData.get(session.KEY_CAPTAIN_ID)
                + "&Skip=" + skip
                + "&length=" + limit;
        ApiHelper api = new ApiHelper(this, balanceUrl, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                balanceList.clear();
                JSONObject obj = (JSONObject) response;
                try {
                    JSONArray balanceArray = obj.optJSONArray("BalancesList");
                    if (balanceArray.length() > 0) {
                        placeholder.setVisibility(View.GONE);
                        for (int i = 0; i < balanceArray.length(); i++) {
                            JSONObject balanceObj = balanceArray.optJSONObject(i);
                            CaptainBalance captainBalance = new CaptainBalance();
                            captainBalance.setBalanceID(balanceObj.optInt("ID"));
                            captainBalance.setCaptainID(balanceObj.optInt("CaptainID"));
                            captainBalance.setAmount(balanceObj.optInt("Amount"));
                            captainBalance.setNotes(balanceObj.getString("Notes"));
                            captainBalance.setCreatedDate(balanceObj.optString("CreatedDate"));
                            balanceList.add(captainBalance);
                        }
                        recyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        if (balanceArray.length() < 10)
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

    private void loadMoreBalance() {
        String balanceUrl = ApiEndPoints.GET_CAPTAIN_BALANCE
                + "?CaptainID=" + CacheHelper.getInstance().captainData.get(session.KEY_CAPTAIN_ID)
                + "&Skip=" + skip
                + "&length=" + limit;
        ApiHelper api = new ApiHelper(this, balanceUrl, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                balanceList.clear();
                JSONObject obj = (JSONObject) response;
                try {
                    JSONArray balanceArray = obj.optJSONArray("BalancesList");
                    if (balanceArray.length() > 0) {
                        placeholder.setVisibility(View.GONE);
                        for (int i = 0; i < balanceArray.length(); i++) {
                            JSONObject balanceObj = balanceArray.optJSONObject(i);
                            CaptainBalance captainBalance = new CaptainBalance();
                            captainBalance.setBalanceID(balanceObj.optInt("ID"));
                            captainBalance.setCaptainID(balanceObj.optInt("CaptainID"));
                            captainBalance.setAmount(balanceObj.optInt("Amount"));
                            captainBalance.setNotes(balanceObj.getString("Notes"));
                            captainBalance.setCreatedDate(balanceObj.optString("CreatedDate"));
                            balanceList.add(captainBalance);
                        }
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        if (balanceArray.length() < 10)
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
        return R.layout.activity_balance;
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
    public void onBalanceRowClicked(int position) {

    }

    @Override
    public void onRefresh() {
        limit = 10;
        skip = 0;
        getBalance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
