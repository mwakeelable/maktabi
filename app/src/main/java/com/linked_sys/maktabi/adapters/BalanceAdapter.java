package com.linked_sys.maktabi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.models.CaptainBalance;

import java.util.ArrayList;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<CaptainBalance> balanceList;
    public ArrayList<CaptainBalance> filteredList;
    private BalanceFilter balanceFilter;
    private BalanceAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView balanceTxt, notesTxt, dateTxt;
        RelativeLayout balanceRow;

        MyViewHolder(View view) {
            super(view);
            balanceTxt = (TextView) view.findViewById(R.id.balanceTxt);
            notesTxt = (TextView) view.findViewById(R.id.notesTxt);
            dateTxt = (TextView) view.findViewById(R.id.balanceDateTxt);
            balanceRow = (RelativeLayout) view.findViewById(R.id.balanceRow);
        }
    }

     public BalanceAdapter(Context mContext, ArrayList<CaptainBalance> captainBalanceList, BalanceAdapterListener listener) {
        this.mContext = mContext;
        this.balanceList = captainBalanceList;
        this.filteredList = captainBalanceList;
        this.listener = listener;
        getFilter();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.balance_item, parent, false);
        return new MyViewHolder(itemView);
    }

    private String getDateFormat(String date) {
        String dateFormat = "";
        if (date.charAt(5) != '0')
            dateFormat += date.charAt(5);
        dateFormat += date.charAt(6);
        dateFormat += '/';
        if (date.charAt(8) != '0')
            dateFormat += date.charAt(8);
        dateFormat += date.charAt(9);
        dateFormat += '/';
        dateFormat += date.charAt(2);
        dateFormat += date.charAt(3);
        return dateFormat;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CaptainBalance captainBalance = filteredList.get(position);
        holder.balanceTxt.setText(String.valueOf(captainBalance.getAmount()));
        holder.notesTxt.setText(captainBalance.getNotes());
        String date = getDateFormat(captainBalance.getCreatedDate());
        holder.dateTxt.setText(date);
        applyClickEvents(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(String.valueOf(filteredList.get(position).getBalanceID()));
    }

       @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.balanceRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBalanceRowClicked(position);
            }
        });
    }

    public interface BalanceAdapterListener {
        void onBalanceRowClicked(int position);
    }

    private class BalanceFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<CaptainBalance> tempList = new ArrayList<>();
                // search content in tasks list
                for (CaptainBalance captainBalance : balanceList) {
                    if (captainBalance.getNotes().toLowerCase().contains(constraint.toString().toLowerCase())) {
                             tempList.add(captainBalance);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = balanceList.size();
                filterResults.values = balanceList;
            }
            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         *
         * @param constraint text
         * @param results    filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<CaptainBalance>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if (balanceFilter == null) {
            balanceFilter = new BalanceFilter();
        }
        return balanceFilter;
    }

    }