package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.app.Activity;
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
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.Menu;

public class FoodOfShopOrderAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<Menu> lsvShop;
	private static LayoutInflater inflater = null;
	private AQuery aq;

	public FoodOfShopOrderAdapter(Activity mcontext, ArrayList<Menu> arrOffer) {
		context = mcontext;
		lsvShop = arrOffer;
		inflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsvShop.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Hoder holder;
		if (convertView == null) {
			holder = new Hoder();
			convertView = inflater.inflate(
					R.layout.row_list_food_of_shop_order, null);
			holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.progess);
			holder.lblFoodName = (TextView) convertView
					.findViewById(R.id.lblFoodName);
			holder.lblFoodNumber = (TextView) convertView
					.findViewById(R.id.lblFoodNumber);
			holder.lblTotal = (TextView) convertView
					.findViewById(R.id.lblTotal);
			holder.lblPrice = (TextView) convertView
					.findViewById(R.id.lblPrice);
			holder.tvTopping=convertView.findViewById(R.id.tvTopping);

			convertView.setTag(holder);

		} else {
			holder = (Hoder) convertView.getTag();
		}
		final Menu menu = lsvShop.get(position);
		if (menu != null) {

			aq.id(holder.lblFoodName).text(menu.getName());

			aq.id(holder.lblFoodNumber).text(
					String.format("%02d", menu.getOrderNumber()));
			aq.id(holder.lblPrice).text(
					context.getText(R.string.currency) + "" + menu.getPrice());
			aq.id(holder.lblTotal).text(
					context.getText(R.string.currency) + " "
							+ String.format("%.1f", menu.getTotalPrice()));
			//aq.id(holder.tvTopping).text(menu.getRelishArrayList().get(0).getRelishName());
			aq.id(holder.imgFood)
					.progress(holder.progress)
					.image(menu.getImage(), true, true, 0,
							R.drawable.no_image_available_horizontal,
							new BitmapAjaxCallback() {
								@SuppressWarnings("deprecation")
								@Override
								public void callback(String url, ImageView iv,
										Bitmap bm, AjaxStatus status) {

									Drawable d = new BitmapDrawable(context
											.getResources(), bm);
									holder.imgFood.setBackgroundDrawable(d);
								}
							});

		}
		return convertView;
	}

	static class Hoder {
		ImageView imgFood;
		ProgressBar progress;
		TextView lblFoodName, lblPrice, lblFoodNumber, lblTotal,tvTopping;

	}

}
