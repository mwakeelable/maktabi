package com.linked_sys.maktabi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linked_sys.maktabi.R;

import java.util.HashMap;
import java.util.List;


public class FAQAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;

    public FAQAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData){
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_item, null);
        }
        TextView lblListBody = (TextView) convertView.findViewById(R.id.answerTxt);
        lblListBody.setTypeface(null, Typeface.BOLD);
        lblListBody.setText(childText);
        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.container);
        WebView webView = new WebView(_context);
        webView.setVerticalScrollBarEnabled(false);
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        container.removeAllViewsInLayout();
        container.addView(webView);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_list_header,
                    null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.questionTxt);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView indicatorImg = (ImageView) convertView.findViewById(R.id.indicatorImg);

        if (isExpanded) {
            indicatorImg.setImageResource(R.drawable.ic_help_question_expand);
        } else {
            indicatorImg.setImageResource(R.drawable.ic_help_question_collapse);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
