package com.hcpt.multirestaurants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.config.Constant;
import com.hcpt.multirestaurants.object.Relish;
import com.hcpt.multirestaurants.object.RelishOption;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterOptionTopping extends RecyclerView.Adapter<AdapterOptionTopping.ViewHolde> {
    private Context context;
    private ArrayList<Relish> relishArrayList;
    private OnCheckListerner onCheckListerner;
    private OnSetDataListerner onSetDataListerner;


    public AdapterOptionTopping(Context context, ArrayList<Relish> relishArrayList, OnCheckListerner onCheckListerner, OnSetDataListerner onSetDataListerner) {
        this.context = context;
        this.relishArrayList = relishArrayList;
        this.onCheckListerner = onCheckListerner;
        this.onSetDataListerner = onSetDataListerner;

    }


    @Override
    public ViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_topping, parent, false);
        return new ViewHolde(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolde holder, final int position) {
        final Relish relish = relishArrayList.get(position);
        AdapterRelishOptionNew adapterRelishOptionNew = new AdapterRelishOptionNew(context, relish.getOptionArrayList());
        holder.spnType.setAdapter(adapterRelishOptionNew);
        holder.tvTotal.setText("$" + relishArrayList.get(position).getRelishPrice());
        holder.tvRelish.setText(relishArrayList.get(position).getRelishName());
        if (relish.getRelishOptionChoise() != null && !relish.getRelishOptionChoise().equals(Constant.NONE)) {
            int pos = relish.getOptionArrayList().indexOf(relish.getRelishOptionChoise());
            holder.spnType.setSelection(pos);
        }
        holder.spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                relish.setRelishOptionChoise(relish.getOptionArrayList().get(p));
                onSetDataListerner.onCheckChange(relish, position, p);
                Debug.e(relish.getOptionArrayList().get(p).getName() + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.cbRelish.setChecked(relishArrayList.get(position).getChecker());
        holder.cbRelish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckListerner.onCheckChange(isChecked, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return relishArrayList.size();
    }

    public class ViewHolde extends RecyclerView.ViewHolder {
        private TextView tvTotal;
        private CheckBox cbRelish;
        private TextView tvRelish;
        private Spinner spnType;

        public ViewHolde(View itemView) {
            super(itemView);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvRelish = itemView.findViewById(R.id.tvRelish);
            cbRelish = itemView.findViewById(R.id.cbRelish);
            spnType = itemView.findViewById(R.id.spnType);
        }
    }


    public void setClearOption() {
        for (Relish relishOption : relishArrayList) {
            relishOption.setChecker(false);
        }
        notifyDataSetChanged();
    }

    public interface OnCheckListerner {
        void onCheckChange(boolean isChecked, int position);
    }

    public interface OnSetDataListerner {
        void onCheckChange(Relish relish, int position, int p);
    }

}
