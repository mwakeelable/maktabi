package com.linked_sys.maktabi.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.activities.BalanceActivity;
import com.linked_sys.maktabi.activities.CompDetailsActivity;
import com.linked_sys.maktabi.activities.ComplaintsActivity;
import com.linked_sys.maktabi.activities.MainActivity;

import static com.linked_sys.maktabi.R.id.btn_balance;
import static com.linked_sys.maktabi.R.id.txt_balance;
import static com.linked_sys.maktabi.core.CacheHelper.captainBalance;
import static com.linked_sys.maktabi.core.CacheHelper.captainComplaints;

public class ComplaintsFragment extends Fragment {
    MainActivity activity;
    TextView txt_comp;
    LinearLayout btn_comp;
    public ProgressBar comp_progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.complaints_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        comp_progress = (ProgressBar) view.findViewById(R.id.comp_count);
        txt_comp = (TextView) view.findViewById(R.id.txt_comp);
        btn_comp = (LinearLayout) view.findViewById(R.id.btn_comp);
        comp_progress.setProgress(captainComplaints);
        txt_comp.setText(String.valueOf(captainComplaints));
        btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openActivity(ComplaintsActivity.class);
            }
        });
    }
}
