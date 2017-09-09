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
import com.linked_sys.maktabi.activities.MainActivity;

import static com.linked_sys.maktabi.core.CacheHelper.captainBalance;

public class BalanceFragment extends Fragment {
    MainActivity activity;
    TextView txt_balance;
    LinearLayout btn_balance;
    public ProgressBar balance_progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        balance_progress = (ProgressBar) view.findViewById(R.id.balance_count);
        txt_balance = (TextView) view.findViewById(R.id.txt_balance);
        btn_balance = (LinearLayout) view.findViewById(R.id.btn_balance);
        balance_progress.setProgress(captainBalance);
        txt_balance.setText(String.valueOf(captainBalance));
        btn_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openActivity(BalanceActivity.class);
            }
        });
    }
}
