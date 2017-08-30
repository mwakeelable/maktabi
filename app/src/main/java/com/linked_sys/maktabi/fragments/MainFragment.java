package com.linked_sys.maktabi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.activities.BalanceActivity;
import com.linked_sys.maktabi.activities.ComplaintsActivity;
import com.linked_sys.maktabi.activities.MainActivity;
import com.linked_sys.maktabi.core.CacheHelper;

public class MainFragment extends Fragment {
    MainActivity activity;
    TextView txtBalance, txtComplaints, txtOfficeNotifications;
    LinearLayout btnBalance, btnComplaints, btnOfficeNotifications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtBalance = (TextView) view.findViewById(R.id.txtBalanceCount);
        txtComplaints = (TextView) view.findViewById(R.id.txtCompCount);
        txtOfficeNotifications = (TextView) view.findViewById(R.id.txtOfficeNotifyCount);
        btnBalance = (LinearLayout) view.findViewById(R.id.btnBalance);
        btnComplaints = (LinearLayout) view.findViewById(R.id.btnComplaints);
        btnOfficeNotifications = (LinearLayout) view.findViewById(R.id.btnOfficeNotify);

        txtBalance.setText(CacheHelper.getInstance().captainData.get(activity.session.KEY_BALANCE_TOTAL));
        txtComplaints.setText(CacheHelper.getInstance().captainData.get("compCount"));
        txtOfficeNotifications.setText(CacheHelper.getInstance().captainData.get("OfficeNotify"));

        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openActivity(BalanceActivity.class);
            }
        });

        btnComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openActivity(ComplaintsActivity.class);
            }
        });

        btnOfficeNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        txtComplaints.setText(CacheHelper.getInstance().captainData.get("compCount"));
    }
}
