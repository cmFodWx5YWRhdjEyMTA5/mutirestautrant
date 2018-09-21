package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.Offer;

public class ListOfferAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Offer> lsvOffer;
	private static LayoutInflater inflater = null;
	private AQuery aq;

	public ListOfferAdapter(Context mcontext, ArrayList<Offer> arrOffer) {
		context = mcontext;
		lsvOffer = arrOffer;
		inflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsvOffer.size();
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
			convertView = inflater.inflate(R.layout.row_list_offer, null);
			holder.imgOffer = (ImageView) convertView
					.findViewById(R.id.imgOffer);
			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.progess);
			convertView.setTag(holder);

		} else {
			holder = (Hoder) convertView.getTag();
		}
		final Offer o = lsvOffer.get(position);
		if (o != null) {
			aq.id(holder.imgOffer).progress(holder.progress).image(o.getImage(), true, true, 0,
				    R.drawable.no_image_available_horizontal,
				    new BitmapAjaxCallback() {
				     @SuppressLint("NewApi")
					@Override
				     public void callback(String url, ImageView iv, Bitmap bm,
				       AjaxStatus status) {
				    	 
				    	 Drawable d = new BitmapDrawable(context.getResources(),bm);
				    	 holder.imgOffer.setBackgroundDrawable(d);
				     }
				    });
		}
		return convertView;
	}

	static class Hoder {
		ImageView imgOffer;
		ProgressBar progress;
	}
}
