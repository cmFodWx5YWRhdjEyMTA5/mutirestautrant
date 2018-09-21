package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.Order;

public class DetailHistoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Order> arrFood;
	private static LayoutInflater inflater = null;
	private AQuery aq;

	public DetailHistoryAdapter(Context mcontext, ArrayList<Order> arr) {
		context = mcontext;
		arrFood = arr;
		inflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrFood.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Hoder holder;
		if (convertView == null) {
			holder = new Hoder();
			convertView = inflater.inflate(R.layout.row_detail_history, null);
			holder.imgFood = (ImageView) convertView
					.findViewById(R.id.imgDetailHistory);
			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.progess);
			holder.lblFoodName = (TextView) convertView
					.findViewById(R.id.lblFoodNameDetailHistory);
			holder.lblPrice = (TextView) convertView
					.findViewById(R.id.lblPrice);

			holder.lblDiscount = (TextView) convertView
					.findViewById(R.id.lblPromotion);
			holder.lblNumber = (TextView) convertView
					.findViewById(R.id.lblNumber);
			convertView.setTag(holder);

		} else {
			holder = (Hoder) convertView.getTag();
		}
		final Order o = arrFood.get(position);
		if (o != null) {

			aq.id(holder.lblPrice).text("$ " + o.getPrice());
			aq.id(holder.lblDiscount).text(o.getPromotion() + " %");
			aq.id(holder.lblFoodName).text(o.getName());
			aq.id(holder.lblNumber).text(o.getNumber());

			Glide.with(context).load(o.getImage()).placeholder(R.drawable.no_image_available_horizontal).into(holder.imgFood);
		}
		return convertView;
	}

	static class Hoder {
		ImageView imgFood;
		ProgressBar progress;
		TextView lblFoodName, lblPrice, lblDiscount, lblNumber;

	}

}
