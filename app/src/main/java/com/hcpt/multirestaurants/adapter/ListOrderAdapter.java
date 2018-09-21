package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.OrderGroup;

public class ListOrderAdapter extends ArrayAdapter<OrderGroup> {
	private static String NEW = "NEW";
	private static String DONE = "DONE";
	private static String PROCESSING = "PROCESSING";
	private Activity mContext;
	private AQuery aq;
	private ArrayList<OrderGroup> list;
	LayoutInflater inflater = null;

	public ListOrderAdapter(Activity context, int resource,
			ArrayList<OrderGroup> list) {
		super(context, resource);
		mContext = context;
		this.list = list;
		aq = new AQuery(context);
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_list_history, null);
			holder = new Holder();
			holder.imageContinue = (ImageView) convertView
					.findViewById(R.id.imgContinue);
			holder.lblStatus = (TextView) convertView
					.findViewById(R.id.lblStatusOrder);
			holder.lblStt = (TextView) convertView.findViewById(R.id.lblSTT);
			holder.lblTime = (TextView) convertView
					.findViewById(R.id.lblOrderTime);
			holder.lblTotal = (TextView) convertView
					.findViewById(R.id.lblTotalH);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		OrderGroup oder = list.get(position);

		if (oder != null) {
			if (oder.getStatus().equalsIgnoreCase(NEW)) {
				holder.lblStatus.setTextColor(mContext.getResources().getColor(
						R.color.neworder));
				aq.id(holder.lblStatus).text("New");
			} else if (oder.getStatus().equalsIgnoreCase(PROCESSING)) {
				holder.lblStatus.setTextColor(mContext.getResources().getColor(
						R.color.processorder));
				aq.id(holder.lblStatus).text("Processing");
			} else {
				holder.lblStatus.setTextColor(mContext.getResources().getColor(
						R.color.closeorder));
				aq.id(holder.lblStatus).text("Closed");
			}

			aq.id(holder.lblStt).text("Order #" + oder.getId());
			aq.id(holder.lblTime).text(oder.getDatetime());
			aq.id(holder.lblTotal).text("$" + oder.getPrice());
		}
		return convertView;
	}

	static class Holder {
		TextView lblStt, lblStatus, lblTotal, lblTime;
		ImageView imageContinue;
	}

}
