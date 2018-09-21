package com.hcpt.multirestaurants.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.OfferActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Offer;

import java.util.ArrayList;

public class ListOfferAdapterNew extends RecyclerView.Adapter<ListOfferAdapterNew.ViewHolder> {
    private Context context;
    private ArrayList<Offer> offerArrayList;

    public ListOfferAdapterNew(Context context, ArrayList<Offer> offerArrayList) {
        this.context = context;
        this.offerArrayList = offerArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(offerArrayList.get(position).getImage()).placeholder(R.drawable.no_image_available_horizontal).into(holder.imgOffer);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offer offer = offerArrayList.get(position);
                Bundle b = new Bundle();
                b.putInt(GlobalValue.KEY_OFFER_ID, offer.getOfferId());
                b.putInt(GlobalValue.KEY_SHOP_ID, offer.getShopId());
                Intent intent = new Intent(context, OfferActivity.class);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return offerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            imgOffer = itemView.findViewById(R.id.imgOffer);
        }
    }
}
