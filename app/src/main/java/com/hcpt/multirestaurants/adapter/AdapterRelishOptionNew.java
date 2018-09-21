package com.hcpt.multirestaurants.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.RelishOption;

import java.util.ArrayList;


public class AdapterRelishOptionNew extends BaseAdapter {
    private Context context;
    private ArrayList<RelishOption> optionArrayList;

    public AdapterRelishOptionNew(Context context, ArrayList<RelishOption> optionArrayList) {
        this.context = context;
        this.optionArrayList = optionArrayList;
    }

    @Override
    public int getCount() {
        return optionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(optionArrayList.get(position).getName());
        return view;
    }
}
