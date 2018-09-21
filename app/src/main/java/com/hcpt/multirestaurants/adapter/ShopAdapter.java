package com.hcpt.multirestaurants.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.MapDetailActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.ImageUtil;

import java.util.ArrayList;

public class ShopAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Shop> lsvShop;
    private static LayoutInflater inflater = null;
    private AQuery aq;
    private static final String TAG = "SHOP ADAPTER";

    public ShopAdapter(Activity mcontext, ArrayList<Shop> arrOffer) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Hoder holder;
        if (convertView == null) {
            holder = new Hoder();
            convertView = inflater.inflate(R.layout.row_list_shop, null);
            holder.imgShop = (ImageView) convertView.findViewById(R.id.imgShop);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progess);
            holder.lblShopName = (TextView) convertView
                    .findViewById(R.id.lblShopName);
            holder.lblOpenhourStatus = (TextView) convertView
                    .findViewById(R.id.lblOpenHourStatus);
            holder.imgFeatured = (ImageView) convertView
                    .findViewById(R.id.imgFeatured);
            holder.btnCall = (ImageView) convertView.findViewById(R.id.btnCall);
            holder.imgVerified = (ImageView) convertView
                    .findViewById(R.id.imgVerified);
            holder.rtbRating = (RatingBar) convertView
                    .findViewById(R.id.rtbRating);
            holder.lblRatingNumber = (TextView) convertView
                    .findViewById(R.id.lblRatingNumber);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.address.setSelected(true);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
            holder.btnMap = (LinearLayout) convertView.findViewById(R.id.btnMap);

            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }
        final Shop o = lsvShop.get(position);
        if (o != null) {

            if (o.isOpen()) {
                holder.lblOpenhourStatus.setVisibility(View.GONE);
            } else {
                aq.id(holder.lblOpenhourStatus).text("CLOSED");
                holder.lblOpenhourStatus.setVisibility(View.VISIBLE);
            }

            aq.id(holder.lblShopName).text(o.getShopName());

            String des = o.getDescription();
            if (des.length() > 40) {
                des = des.substring(0, 39) + " ...";
            }

            if (o.getIsFeatured().equals("1")) {
                holder.imgFeatured.setVisibility(View.VISIBLE);
            } else {
                holder.imgFeatured.setVisibility(View.INVISIBLE);
            }

            if (o.getIsVerified().equals("1")) {
                holder.imgVerified.setVisibility(View.VISIBLE);
            } else {
                holder.imgVerified.setVisibility(View.INVISIBLE);
            }

            holder.address.setText(o.getAddress());
            holder.phoneNumber.setText(o.getPhone());

            aq.id(holder.lblRatingNumber).text("(" + o.getRateNumber() + ")");
            holder.rtbRating.setRating(o.getRateValue() / 2);
            aq.id(holder.imgShop)
                    .progress(holder.progress)
                    .image(o.getImage(), false, true, 0,
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

            // set action 3 buttons :

            holder.btnCall.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Debug.e("Call");
                    // TODO Auto-generated method stub
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + o.getPhone()));
                    context.startActivity(callIntent);
                }
            });

            holder.btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Debug.e("MAP");
                    Bundle bundle = new Bundle();
                    bundle.putInt(GlobalValue.KEY_SHOP_ID, o.getShopId());
                    Intent intent = new Intent(context,MapDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
                }
            });
            Debug.e("sao lai intent");

//            holder.btnCategory.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Bundle b = new Bundle();
//                    b.putInt(GlobalValue.KEY_SHOP_ID, o.getShopId());
//                    b.putString(GlobalValue.KEY_SHOP_NAME, o.getShopName());
//                    Intent i = new Intent(context, ListCategoryActivity.class);
//                    i.putExtras(b);
//                    context.startActivity(i);
//                }
//            });
//
//            holder.btnFindWay.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Bundle b = new Bundle();
//                    b.putInt(GlobalValue.KEY_SHOP_ID, o.getShopId());
//                    Intent i = new Intent(context, MapDetailActivity.class);
//                    i.putExtras(b);
//                    context.startActivity(i);
//                }
//            });

        }
        return convertView;
    }


    static class Hoder {
        ImageView imgShop;
        ProgressBar progress;
        TextView lblShopName, lblOpenhourStatus,
                lblRatingNumber, address, phoneNumber;
        ImageView imgFeatured, btnCall, imgVerified;
        RatingBar rtbRating;
        LinearLayout btnMap;

    }

}
