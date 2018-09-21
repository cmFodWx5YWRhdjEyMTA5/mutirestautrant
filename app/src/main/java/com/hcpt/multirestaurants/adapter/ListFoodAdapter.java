package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.MapDetailActivity;
import com.hcpt.multirestaurants.activity.ShopDetailActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.util.ImageUtil;

public class ListFoodAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Menu> arrFood;
    private static LayoutInflater inflater = null;
    private AQuery aq;
    public Boolean showShop;

    public ListFoodAdapter(Activity mcontext, ArrayList<Menu> arr) {
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
            convertView = inflater.inflate(R.layout.row_list_food, null);
            holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progess);
            holder.lblFoodName = (TextView) convertView
                    .findViewById(R.id.lblFoodName);
            holder.lblPrice = (TextView) convertView
                    .findViewById(R.id.lblPrice);
            holder.lblDescription = (TextView) convertView
                    .findViewById(R.id.lblDescription);
            holder.lblDiscountPrice = (TextView) convertView
                    .findViewById(R.id.lblDiscountPrice);
            holder.lblRatingNumber = (TextView) convertView
                    .findViewById(R.id.lblRatingNumber);
            holder.rtbRating = (RatingBar) convertView
                    .findViewById(R.id.rtbRating);
            holder.btnfindWay = (ImageView) convertView.findViewById(R.id.btnfindWay);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.address.setSelected(true);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
            holder.btnMap = (LinearLayout) convertView.findViewById(R.id.btnMap);
            holder.btnCall = (ImageView) convertView.findViewById(R.id.btnCall);
            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }
        final Menu o = arrFood.get(position);
        if (o != null) {

            if(!showShop){
                holder.btnCall.setVisibility(View.GONE);
                holder.btnMap.setVisibility(View.GONE);
            }else{
                holder.btnCall.setVisibility(View.VISIBLE);
                holder.btnMap.setVisibility(View.VISIBLE);
            }

            holder.rtbRating.setRating((o.getRateValue() / 2));
            holder.lblRatingNumber.setText("(" + o.getRateNumber() + ")");

            holder.lblPrice.setText(context.getString(R.string.currency)
                    + String.format("%.1f", o.getPrice()));
            holder.lblFoodName.setText(o.getName());
            holder.lblDiscountPrice.setText(context
                    .getString(R.string.currency)
                    + String.format("%.1f", o.getCurrentPrice()));
            holder.lblDiscountPrice.setTextColor(Color.RED);
            holder.lblPrice.setTextColor(Color.GRAY);
            holder.lblPrice.setPaintFlags(holder.lblPrice.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.address.setText(o.getShop_address());
            holder.phoneNumber.setText(o.getShop_phone());

            if (o.getPercentDiscount() > 0) {
                holder.lblPrice.setVisibility(View.VISIBLE);
            } else {
                holder.lblPrice.setVisibility(View.GONE);
            }

            String des = o.getDescription();
            if (des.length() > 40) {
                des = des.substring(0, 39) + " ...";
            }
            aq.id(holder.lblDescription).text(o.getCategory());
            aq.id(holder.imgFood)
                    .progress(holder.progress)
                    .image(o.getImage(), true, true, 0,
                            R.drawable.no_image_available_horizontal,
                            new BitmapAjaxCallback() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {

                                    if (bm != null) {
                                        Drawable d = new BitmapDrawable(context
                                                .getResources(), ImageUtil
                                                .getResizedBitmap(bm, 200, 200));
                                        iv.setBackgroundDrawable(d);
                                    }
                                }
                            });
            if (!showShop) {
                holder.btnfindWay.setVisibility(View.GONE);
            } else {
                holder.btnfindWay.setVisibility(View.VISIBLE);
            }
            holder.btnfindWay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Bundle b = new Bundle();
                    b.putInt(GlobalValue.KEY_SHOP_ID, o.getShopId());
                    Intent i = new Intent(context, ShopDetailActivity.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });

            holder.btnCall.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + o.getShop_phone()));
                    context.startActivity(callIntent);
                }
            });

            holder.btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalValue.KEY_SHOP_ID, o.getShopId());
                    Intent intent = new Intent(context,MapDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
                }
            });
        }
        return convertView;
    }

    class Hoder {
        ImageView imgFood;
        ProgressBar progress;
        TextView lblFoodName, lblDescription, lblPrice, lblDiscountPrice,
                lblRatingNumber,address, phoneNumber;
        RatingBar rtbRating;
        ImageView btnfindWay,btnCall;
        LinearLayout btnMap;

    }

}
