package com.hcpt.multirestaurants.adapter;

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

@SuppressWarnings("serial")
public class SpinnerSimpleAdapter extends ArrayAdapter<String> {

	Activity act;
	private String[] list;
	private LayoutInflater inflate;
	private int selectRow = -1;

	public SpinnerSimpleAdapter(Activity context, int resource, String[] objects) {
		super(context, resource, objects);
		act = context;
		list = objects;
		inflate = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = inflate.inflate(R.layout.layout_text_spinner, parent, false);
		TextView make = (TextView) row.findViewById(R.id.lblTextContent);
		String o = list[position];
		make.setText(o);
		selectRow = position;
		return row;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.layout_text_row_spinner,
					null);
			holder.lblTextcontent = (TextView) convertView
					.findViewById(R.id.lblTextContent);
			holder.layoutRowSpinner = (RelativeLayout) convertView
					.findViewById(R.id.layoutRowSpinner);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String o = list[position];

		if (o != null) {
			if (selectRow == position) {
				holder.layoutRowSpinner.setBackgroundColor(act.getResources()
						.getColor(R.color.cl_tabmain));
				holder.lblTextcontent.setTextColor(Color.WHITE);

			} else {
				holder.lblTextcontent.setTextColor(Color.BLACK);
				holder.layoutRowSpinner.setBackgroundColor(Color.WHITE);
			}
			holder.lblTextcontent.setText(o);
		}
		return convertView;
	}

	class ViewHolder {
		TextView lblTextcontent;
		RelativeLayout layoutRowSpinner;
	}

}
