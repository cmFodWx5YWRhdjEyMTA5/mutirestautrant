package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.City;

@SuppressLint("ViewHolder")
@SuppressWarnings("serial")
public class CityAdapter extends ArrayAdapter<City> {

	Activity act;
	private ArrayList<City> list;
	private LayoutInflater inflate;
	private int selectRow = -1;

	public CityAdapter(Activity context, int resource,
			ArrayList<City> objects) {
		super(context, resource, objects);
		act = context;
		list = objects;
		inflate = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = inflate.inflate(R.layout.layout_text_spinner, parent, false);
		TextView make = (TextView) row.findViewById(R.id.lblTextContent);
		City o = list.get(position);
		make.setText(o.getName());
		selectRow = position;
		return row;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.layout_text_row_spinner, null);
			holder.lblTextcontent = (TextView) convertView
					.findViewById(R.id.lblTextContent);
			holder.layoutRowSpinner = (RelativeLayout) convertView
					.findViewById(R.id.layoutRowSpinner);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		City o = list.get(position);

		if (o != null) {
			if (selectRow == position) {
				holder.layoutRowSpinner.setBackgroundColor(act.getResources().getColor(R.color.cl_tabmain));
				holder.lblTextcontent.setTextColor(Color.WHITE);
				
			} else {
				holder.lblTextcontent.setTextColor(Color.BLACK);
				holder.layoutRowSpinner.setBackgroundColor(Color.WHITE);
			}
			holder.lblTextcontent.setText(o.getName());
		}
		return convertView;
	}

	class ViewHolder {
		TextView lblTextcontent;
		RelativeLayout layoutRowSpinner;
	}

}
