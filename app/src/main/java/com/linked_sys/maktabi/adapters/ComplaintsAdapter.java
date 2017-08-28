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
import com.linked_sys.maktabi.models.Complaint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Complaint> complaintList;
    public ArrayList<Complaint> filteredList;
    private ComplaintFilter complaintFilter;
    private ComplaintsAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView compTitle, compDate;
        RelativeLayout compRow;

        MyViewHolder(View view) {
            super(view);
            compTitle = (TextView) view.findViewById(R.id.compTitleTxt);
            compDate = (TextView) view.findViewById(R.id.compDateTxt);
            compRow = (RelativeLayout) view.findViewById(R.id.compRow);
        }
    }

    public ComplaintsAdapter(Context mContext, ArrayList<Complaint> complaintsList, ComplaintsAdapterListener listener) {
        this.mContext = mContext;
        this.complaintList = complaintsList;
        this.filteredList = complaintsList;
        this.listener = listener;
        getFilter();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint_item, parent, false);
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
        Complaint complaint = filteredList.get(position);
        holder.compTitle.setText(String.valueOf(complaint.getCompTitle()));
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = spf.parse(complaint.getCompDate());
            spf = new SimpleDateFormat("dd/MM/yyyy");
            String date = spf.format(newDate);
            holder.compDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        applyClickEvents(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(String.valueOf(filteredList.get(position).getCompID()));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.compRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCompRowClicked(position);
            }
        });
    }

    public interface ComplaintsAdapterListener {
        void onCompRowClicked(int position);
    }

    private class ComplaintFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Complaint> tempList = new ArrayList<>();
                // search content in tasks list
                for (Complaint complaint : complaintList) {
                    if (complaint.getCompTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(complaint);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = complaintList.size();
                filterResults.values = complaintList;
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
            filteredList = (ArrayList<Complaint>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if (complaintFilter == null) {
            complaintFilter = new ComplaintFilter();
        }
        return complaintFilter;
    }
}
