package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.Shop;

public class ShopCartAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Shop> lsvShop;
    private static LayoutInflater inflater = null;
    private AQuery aq;
    private static final String TAG = "SHOP ADAPTER";
    private ShopCartListener listenner;

    public interface ShopCartListener {
        public void deleteItem(int position);

        public void showDetailOrder(int position);

    }

    public ShopCartAdapter(Activity mcontext, ArrayList<Shop> arrOffer,
                           ShopCartListener listenner) {
        context = mcontext;
        lsvShop = arrOffer;
        inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listenner = listenner;
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
            convertView = inflater
                    .inflate(R.layout.row_list_shop_my_menu, null);
            holder.layoutRowShopCart = (LinearLayout) convertView
                    .findViewById(R.id.layoutRowShopCart);
            holder.imgShop = (ImageView) convertView.findViewById(R.id.imgShop);
            holder.btnDelete = (ImageView) convertView
                    .findViewById(R.id.btnDelete);
            holder.progress = (ProgressBar) convertView
                    .findViewById(R.id.progess);
            holder.lblShopName = (TextView) convertView
                    .findViewById(R.id.lblShopName);
            holder.lblFoodNumber = (TextView) convertView
                    .findViewById(R.id.lblFoodNumber);
            holder.lblVAT = (TextView) convertView.findViewById(R.id.lblVAT);
            holder.lblShipping = (TextView) convertView
                    .findViewById(R.id.lblShipping);
            holder.lblTotalPrice = (TextView) convertView
                    .findViewById(R.id.lblTotalPrice);

            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }

        final Shop o = lsvShop.get(position);
        if (o != null) {

            aq.id(holder.lblShopName).text(o.getShopName());

            aq.id(holder.lblFoodNumber)
                    .text(o.getNumberItems() + " "
                            + context.getString(R.string.item));
            aq.id(holder.lblVAT).text(
                    context.getString(R.string.vat) + " " + o.getShopVAT()
                            + "%");
            holder.lblTotalPrice.setText("$" + o.getTotalPrice());
            aq.id(holder.lblShipping).text(
                    context.getString(R.string.ship) + " "
                            + context.getText(R.string.currency)
                            + o.getcurrentShipping());
            Debug.e(o.getImage());
            aq.id(holder.imgShop)
                    .progress(holder.progress)
                    .image(o.getImage(), true, true, 0,
                            R.drawable.no_image_available_horizontal,
                            new BitmapAjaxCallback() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {

                                    Drawable d = new BitmapDrawable(context
                                            .getResources(), bm);
                                    holder.imgShop.setBackgroundDrawable(d);
                                }
                            });

            holder.btnDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listenner.deleteItem(position);
                }
            });

            holder.layoutRowShopCart.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listenner.showDetailOrder(position);
                }
            });

        }
        return convertView;
    }

    static class Hoder {
        ImageView imgShop, btnDelete;
        ProgressBar progress;
        TextView lblShopName, lblFoodNumber, lblVAT, lblShipping,
                lblTotalPrice;
        LinearLayout layoutRowShopCart;

    }

}
