package com.hcpt.multirestaurants.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.FoodDetailActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Menu;

import java.util.ArrayList;

public class ListFoodHomeAdapterNew extends RecyclerView.Adapter<ListFoodHomeAdapterNew.ViewHolder> {
    private Context context;
    private ArrayList<Menu> menuArrayList;

    public ListFoodHomeAdapterNew(Context context, ArrayList<Menu> menuArrayList) {
        this.context = context;
        this.menuArrayList = menuArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_food_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.lblRateNumber.setText(String.format("%.1f", (menuArrayList.get(position).getRateValue() / 2)));
        Glide.with(context).load(menuArrayList.get(position).getImage()).into(holder.imgFood);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(GlobalValue.KEY_FOOD_ID, menuArrayList.get(position).getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
//                Menu menu = arrFoods.get(index);
//                Bundle b = new Bundle();
//                b.putInt(GlobalValue.KEY_FOOD_ID, menu.getId());
//                gotoActivity(self, FoodDetailActivity.class, b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView lblRateNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            lblRateNumber = itemView.findViewById(R.id.lblRateNumber);
        }
    }
}
