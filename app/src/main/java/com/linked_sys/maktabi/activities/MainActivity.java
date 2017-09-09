package com.linked_sys.maktabi.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.iid.FirebaseInstanceId;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.adapters.CircleTransform;
import com.linked_sys.maktabi.core.AppController;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.fragments.MainFragment;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    DrawerLayout mDrawerLayout;
    CoordinatorLayout mCoordinatorLayout;
    ActionBar mActionBar;
    Toolbar mToolbar;
    MainFragment FRAGMENT_MAIN;
    public FrameLayout mFrameLayout;
    public DrawerView drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.containerView);
        drawer = (DrawerView) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        FRAGMENT_MAIN = new MainFragment();
        drawMainFragment();
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mDrawerLayout.addDrawerListener(drawerToggle);
        mDrawerLayout.closeDrawer(drawer);
        drawCaptainMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.main_activity;
    }

    private void drawCaptainMenu() {
        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.profileNav))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        openActivity(ProfileActivity.class);
                    }
                })
        );
        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.balanceNav))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        openActivity(BalanceActivity.class);
                    }
                })
        );
        drawer.addDivider();

        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.complaintNav))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        openActivity(ComplaintsActivity.class);
                    }
                })
        );
        drawer.addDivider();

        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.faqNav))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        openActivity(FAQActivity.class);
                    }
                })
        );
        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.officeNotifications))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "تحت التطوير", Toast.LENGTH_SHORT).show();
                    }
                })
        );
        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.granteeNotifications))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "تحت التطوير", Toast.LENGTH_SHORT).show();
                    }
                })
        );
//        drawer.addItem(new DrawerItem()
//                .setTextPrimary(getString(R.string.generalInquery))
//                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
//                    @Override
//                    public void onClick(DrawerItem drawerItem, long l, int i) {
//                        mDrawerLayout.closeDrawer(GravityCompat.START);
//                        openActivity(SettingsActivity.class);
//                    }
//                })
//        );
        drawer.addDivider();

        drawer.addItem(new DrawerItem()
                .setTextPrimary(getString(R.string.signOutNav))
                .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int i) {
                        logout();
                    }
                })
        );

        if (CacheHelper.getInstance().captainData.get(session.KEY_IMAGE).equals("null")) {
            drawer.addProfile(new DrawerProfile()
                    .setId(Long.parseLong(CacheHelper.getInstance().userData.get(session.KEY_USER_ID)))
                    .setBackground(ContextCompat.getDrawable(MainActivity.this, R.color.colorPrimary))
                    .setName(CacheHelper.getInstance().captainData.get(session.KEY_NAME))
                    .setAvatar(ContextCompat.getDrawable(this, R.drawable.avatar_placeholder))
            );
        } else {
            Glide
                    .with(this)
                    .load(ApiEndPoints.BASE_URL + CacheHelper.getInstance().captainData.get(session.KEY_IMAGE))
                    .asBitmap()
                    .transform(new CircleTransform(this))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            drawer.addProfile(new DrawerProfile()
                                    .setId(Long.parseLong(CacheHelper.getInstance().userData.get(session.KEY_USER_ID)))
                                    .setBackground(ContextCompat.getDrawable(MainActivity.this, R.color.colorPrimary))
                                    .setName(CacheHelper.getInstance().captainData.get(session.KEY_NAME))
                                    .setAvatar(ContextCompat.getDrawable(MainActivity.this, R.drawable.avatar_placeholder))
                            );
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            userAvatar = resource;
                            drawer.addProfile(new DrawerProfile()
                                    .setId(Long.parseLong(CacheHelper.getInstance().userData.get(session.KEY_USER_ID)))
                                    .setBackground(ContextCompat.getDrawable(MainActivity.this, R.color.colorPrimary))
                                    .setName(CacheHelper.getInstance().captainData.get(session.KEY_NAME))
                                    .setAvatar(new BitmapDrawable(getResources(), userAvatar))
                            );
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            drawer.addProfile(new DrawerProfile()
                                    .setId(Long.parseLong(CacheHelper.getInstance().userData.get(session.KEY_USER_ID)))
                                    .setBackground(ContextCompat.getDrawable(MainActivity.this, R.color.colorPrimary))
                                    .setName(CacheHelper.getInstance().captainData.get(session.KEY_NAME))
                                    .setAvatar(ContextCompat.getDrawable(MainActivity.this, R.drawable.avatar_placeholder))
                            );
                        }
                    });
        }
    }

    private void removeFBToken() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("UserID", CacheHelper.getInstance().userData.get(session.KEY_USER_ID));
        map.put("Token", FirebaseInstanceId.getInstance().getToken());
        ApiHelper api = new ApiHelper(this, ApiEndPoints.REMOVE_FB_TOKEN, Request.Method.POST, map, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                CacheHelper.captainBalance = 0;
                session.logoutUser();
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.d(AppController.TAG, error.getMessage());
            }
        });
        api.executePostRequest(true);
    }

    private void logout() {
        new MaterialDialog.Builder(MainActivity.this)
                .title(getResources().getString(R.string.logout_title))
                .content(getResources().getString(R.string.logout_msg))
                .positiveText(getResources().getString(R.string.logout_positive_btn))
                .negativeText(getResources().getString(R.string.logout_negative_btn))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        removeFBToken();
                    }
                })
                .show();
    }

    private void drawMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mFrameLayout.getChildCount() == 0) {
            transaction.replace(R.id.containerView, FRAGMENT_MAIN);
        } else {
            transaction.add(R.id.containerView, FRAGMENT_MAIN);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
