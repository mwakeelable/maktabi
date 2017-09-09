package com.linked_sys.maktabi.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.etiya.etiyabadgetablib.EtiyaBadgeTab;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.activities.MainActivity;
import com.linked_sys.maktabi.adapters.CircleTransform;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.network.ApiEndPoints;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    MainActivity activity;
    private ViewPager viewPager;
    BalanceFragment FRAGMENT_BALANCE;
    ComplaintsFragment FRAGMENT_COMPLAINTS;
    private TextView txt_name;
    private ImageView img_user;
    private SwipeRefreshLayout refreshLayout;
    private EtiyaBadgeTab etiyaBadgeTab;
    private String[] titles = new String[2];
    private MainAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new MainAdapter(getChildFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        defineControls(view);
        applyHeader();
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
        etiyaBadgeTab.setupWithViewPager(viewPager);
        setupTabs();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (FRAGMENT_BALANCE != null) {
            if (FRAGMENT_BALANCE.balance_progress != null && FRAGMENT_BALANCE.txt_balance != null) {
                FRAGMENT_BALANCE.balance_progress.setProgress(CacheHelper.captainBalance);
                FRAGMENT_BALANCE.txt_balance.setText(String.valueOf(CacheHelper.captainBalance));
            }
        }

        if (FRAGMENT_COMPLAINTS != null) {
            if (FRAGMENT_COMPLAINTS.comp_progress != null && FRAGMENT_COMPLAINTS.txt_comp != null) {
                FRAGMENT_COMPLAINTS.comp_progress.setProgress(CacheHelper.captainComplaints);
                FRAGMENT_COMPLAINTS.txt_comp.setText(String.valueOf(CacheHelper.captainComplaints));
            }
        }
    }

    @Override
    public void onRefresh() {
        if (FRAGMENT_BALANCE.balance_progress != null && FRAGMENT_BALANCE.txt_balance != null) {
            FRAGMENT_BALANCE.balance_progress.setProgress(CacheHelper.captainBalance);
            FRAGMENT_BALANCE.txt_balance.setText(String.valueOf(CacheHelper.captainBalance));
            refreshLayout.setRefreshing(false);
        } else {
            refreshLayout.setRefreshing(false);
        }
        if (FRAGMENT_COMPLAINTS.txt_comp != null && FRAGMENT_COMPLAINTS.comp_progress != null) {
            FRAGMENT_COMPLAINTS.comp_progress.setProgress(CacheHelper.captainComplaints);
            FRAGMENT_COMPLAINTS.txt_comp.setText(String.valueOf(CacheHelper.captainComplaints));
            refreshLayout.setRefreshing(false);
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    private void applyHeader() {
        txt_name.setText(CacheHelper.getInstance().userData.get(activity.session.KEY_NAME1));
        if (!CacheHelper.getInstance().userData.get(activity.session.KEY_IMAGE).equals("null")) {
            Glide
                    .with(activity)
                    .load(ApiEndPoints.BASE_URL + CacheHelper.getInstance().userData.get(activity.session.KEY_IMAGE))
                    .asBitmap()
                    .transform(new CircleTransform(activity))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            img_user.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);

                        }
                    });

        }
    }

    private void defineControls(View view) {
        FRAGMENT_BALANCE = new BalanceFragment();
        FRAGMENT_COMPLAINTS = new ComplaintsFragment();
        txt_name = (TextView) view.findViewById(R.id.txt_username);
        img_user = (ImageView) view.findViewById(R.id.profile_image);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_main);
        refreshLayout.setOnRefreshListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        etiyaBadgeTab = (EtiyaBadgeTab) view.findViewById(R.id.etiyaBadgeTabs);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });
    }

    private void setupTabs() {
        etiyaBadgeTab.setSelectedTabIndicatorColor(activity.getResources().getColor(R.color.black));
        etiyaBadgeTab.setSelectedTabIndicatorHeight(5);
        etiyaBadgeTab.setTabMode(TabLayout.GRAVITY_CENTER);
        etiyaBadgeTab.setTabGravity(TabLayout.GRAVITY_CENTER);
        etiyaBadgeTab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        etiyaBadgeTab.selectEtiyaBadgeTab(0)
                .tabTitle("حســابي")
                .tabTitleColor(R.color.White)
                .tabIconColor(R.color.White)
//                .tabBadge(true)
//                .tabBadgeCount(CacheHelper.captainBalance)
                .tabBadgeBgColor(R.color.red_400)
                .tabBadgeTextColor(R.color.White)
                .tabBadgeStroke(1, R.color.White)
                .tabBadgeCornerRadius(10)
                .createEtiyaBadgeTab();

        etiyaBadgeTab.selectEtiyaBadgeTab(1)
                .tabTitle("الشـكاوى")
                .tabTitleColor(R.color.White)
                .tabIconColor(R.color.White)
//                .tabBadge(true)
//                .tabBadgeCount(newTasks)
                .tabBadgeBgColor(R.color.red_400)
                .tabBadgeTextColor(R.color.White)
                .tabBadgeStroke(1, R.color.White)
                .tabBadgeCornerRadius(10)
                .createEtiyaBadgeTab();

//        etiyaBadgeTab.selectEtiyaBadgeTab(2)
//                .tabTitle(activity.getResources().getString(R.string.tab4))
//                .tabTitleColor(R.color.white)
//                .tabIconColor(R.color.white)
//                .tabBadge(true)
//                .tabBadgeCount(absence)
//                .tabBadgeBgColor(R.color.red_400)
//                .tabBadgeTextColor(R.color.white)
//                .tabBadgeStroke(1, R.color.white)
//                .tabBadgeCornerRadius(10)
//                .createEtiyaBadgeTab();
//
//        etiyaBadgeTab.selectEtiyaBadgeTab(3)
//                .tabTitle(activity.getResources().getString(R.string.tab5))
//                .tabTitleColor(R.color.white)
//                .tabIconColor(R.color.white)
//                .tabBadge(true)
//                .tabBadgeCount(behaviour)
//                .tabBadgeBgColor(R.color.red_400)
//                .tabBadgeTextColor(R.color.white)
//                .tabBadgeStroke(1, R.color.white)
//                .tabBadgeCornerRadius(10)
//                .createEtiyaBadgeTab();
//
//        etiyaBadgeTab.selectEtiyaBadgeTab(4)
//                .tabTitle(activity.getResources().getString(R.string.tab6))
//                .tabTitleColor(R.color.white)
//                .tabIconColor(R.color.white)
//                .tabBadge(true)
//                .tabBadgeCount(activities)
//                .tabBadgeBgColor(R.color.red_400)
//                .tabBadgeTextColor(R.color.white)
//                .tabBadgeStroke(1, R.color.white)
//                .tabBadgeCornerRadius(10)
//                .createEtiyaBadgeTab();
    }

    private class MainAdapter extends FragmentStatePagerAdapter {
        MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (FRAGMENT_BALANCE == null)
                    FRAGMENT_BALANCE = new BalanceFragment();
                return FRAGMENT_BALANCE;
            } else {
                if (FRAGMENT_COMPLAINTS == null)
                    FRAGMENT_COMPLAINTS = new ComplaintsFragment();
                return FRAGMENT_COMPLAINTS;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(enable);
        }
    }
}
