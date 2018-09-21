package com.hcpt.multirestaurants.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.Category;

import java.util.ArrayList;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> categoryArrayList;
    private FragmentCommunication mCommunicator;

    public ListCategoryAdapter(Context context, ArrayList<Category> categoryArrayList,FragmentCommunication mCommunicator) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.mCommunicator=mCommunicator;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvCategory.setText(categoryArrayList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommunicator.respond(categoryArrayList.get(position).getId(),categoryArrayList.get(position).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
    public interface FragmentCommunication {
        void respond(int menuId,String menuName);
    }
}
