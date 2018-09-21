package com.hcpt.multirestaurants.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.MyMenuActivity;
import com.hcpt.multirestaurants.activity.MyMenuActivity.MenuListener;
import com.hcpt.multirestaurants.config.Constant;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Relish;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;

public class MyMenuAdapter extends BaseAdapter implements AdapterOptionTopping.OnCheckListerner, AdapterOptionTopping.OnSetDataListerner {
    private MyMenuActivity context;
    private ArrayList<Menu> arrMenu;
    private MenuListener listener;
    private static LayoutInflater inflater = null;
    private AQuery aq;
    private Dialog dialogListToppings;
    private Dialog mDialog;
    private AdapterOptionTopping adapterOptionTopping;
    private ArrayList<Relish> relishArrayList;

    public MyMenuAdapter(Activity mcontext, ArrayList<Menu> arrMenu, MenuListener listener) {
        context = (MyMenuActivity) mcontext;
        this.arrMenu = arrMenu;
        this.listener = listener;
        inflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aq = new AQuery(context);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrMenu.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        final Hoder holder;
        if (convertView == null) {
            holder = new Hoder();
            convertView = inflater.inflate(R.layout.item_product_cart_order, null);
            holder.imgFood = convertView.findViewById(R.id.imgFood);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvTotalprice = convertView.findViewById(R.id.tvTotalPrice);
            holder.tvNumberItem = convertView.findViewById(R.id.tvItemNumber);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            holder.tvTopping = convertView.findViewById(R.id.tvTopping);
            holder.tvCategory = convertView.findViewById(R.id.tvCategory);
            holder.btnAddTopping = convertView.findViewById(R.id.btnAddTopping);
            holder.btnAddIntroduction = convertView.findViewById(R.id.btnAddIntroduction);
            convertView.setTag(holder);

        } else {
            holder = (Hoder) convertView.getTag();
        }
        final Menu menu = arrMenu.get(position);
        if (menu != null) {
            Debug.e(String.valueOf(menu.getTotalPrices()));
            double price = menu.getTotalPrices();
            aq.id(holder.tvTotalprice).text("$" + String.format("%.1f", price));
            aq.id(holder.tvName).text(menu.getName());


            Glide.with(context).load(menu.getImage()).placeholder(R.drawable.no_image_available).into(holder.imgFood);
            if (menu.getOrderNumber() == 1) {
                aq.id(holder.tvNumberItem).text(
                        String.format("%02d", menu.getOrderNumber()) + " item");
            } else {
                aq.id(holder.tvNumberItem).text(
                        String.format("%02d", menu.getOrderNumber()) + " items");
            }

            aq.id(holder.tvTotalprice).text(
                    "$" + String.format("%.1f", menu.getOrderNumber() * price));

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.deleteItem(position);
                }
            });


            StringBuilder listTopping = new StringBuilder();
            for (int i = 0; i < menu.getRelishArrayList().size(); i++) {
                if (menu.getRelishArrayList().get(i).getRelishOptionChoise() != null && !menu.getRelishArrayList().get(i).getRelishOptionChoise().getName().equals(Constant.NONE)) {
                    price = price + menu.getRelishArrayList().get(i).getRelishPrice();
                    listTopping.append(", ").append(menu.getRelishArrayList().get(i).getRelishName() + ":\t" + menu.getRelishArrayList().get(i).getRelishOptionChoise().getName());
                }
            }
            if (!listTopping.equals("")) {
                holder.tvTopping.setText(listTopping.toString());
            } else {
                holder.tvTopping.setVisibility(View.GONE);

            }

            holder.tvCategory.setText(menu.getCategoryProduct());
            holder.btnAddIntroduction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInstructionDialog(arrMenu.get(position), position);
                }
            });
            holder.btnAddTopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogListTopping(arrMenu.get(position), position);
                }
            });
            holder.tvNumberItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showListQuantities(arrMenu.get(position), position);
                    //
                }
            });
        }
        return convertView;
    }

    @Override
    public void onCheckChange(boolean isChecked, int position) {
        relishArrayList.get(position).setChecker(isChecked);
    }

    @Override
    public void onCheckChange(Relish relish, int position, int p) {
        relishArrayList.set(position, relish);
    }

    static class Hoder {
        ImageView imgFood;
        Button btnDelete;
        TextView tvName, tvTopping, tvTotalprice, tvCategory, tvNumberItem;
        TextView btnAddIntroduction, btnAddTopping;

    }


    private void showListQuantities(final Menu food, final int p) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setquantity_new);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 4 * (displaymetrics.widthPixels / 5);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCancelable(false);

        ListView lsvCook = (ListView) dialog.findViewById(R.id.lsvCookMethod);
        Integer[] numberlist = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(context,
                android.R.layout.simple_list_item_single_choice, numberlist);
        lsvCook.setAdapter(adapter);
        lsvCook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = 1 + position + "";
                food.setOrderNumber(Integer.parseInt(a));
                updateContent();
                dialog.dismiss();
                updateDataShop(food, p);
            }
        });

        dialog.show();

    }

    //add note
    private void showInstructionDialog(final Menu food, final int p) {
        mDialog = new Dialog(context, R.style.AppTheme_OrderDetailsDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_instruction);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = 4 * (displaymetrics.widthPixels / 5);
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        mDialog.setCancelable(false);

        // Get view
        final EditText edtInsTruction = (EditText) mDialog.findViewById(R.id.edtInsTructions);
        TextView btnCancel = (TextView) mDialog.findViewById(R.id.btnCancel);
        TextView btnSave = (TextView) mDialog.findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (edtInsTruction.getText().toString().length() > 0) {
                    food.setAddnote(edtInsTruction.getText().toString());
                }
                updateContent();
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void updateContent() {
        notifyDataSetChanged();
    }

    private void updateDataShop(Menu food, int position) {
        food.setOrderNumber(food.getOrderNumber());
        food.setRelishArrayList(food.getRelishArrayList());
        food.setAddnote(food.getAddnote());
        listener.addQuantity(position, food.getOrderNumber());
        listener.updateNote(position, food.getAddnote());
        listener.updateShop(position, food.getRelishArrayList());
    }

    private void showDialogListTopping(final Menu food, final int p) {
        //p la position of food

        dialogListToppings = new Dialog(context, R.style.AppTheme_OrderDetailsDialog);
        dialogListToppings.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListToppings.setContentView(R.layout.dialog_option);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogListToppings.getWindow().getAttributes());
        lp.width = 19 * (displaymetrics.widthPixels / 20);
        dialogListToppings.getWindow().setAttributes(lp);
        dialogListToppings.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogListToppings.setCancelable(false);
        RecyclerView lvToppings = dialogListToppings.findViewById(R.id.rclToppings);
        TextView btnCancle = dialogListToppings.findViewById(R.id.btnCancel);
        TextView btnSave = dialogListToppings.findViewById(R.id.btnSave);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvToppings.setLayoutManager(layoutManager);
        relishArrayList = food.getRelishArrayList();
        adapterOptionTopping = new AdapterOptionTopping(context, relishArrayList, this, this);
        lvToppings.setAdapter(adapterOptionTopping);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListToppings.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContent();
                dialogListToppings.dismiss();
                updateDataShop(food, p);
            }
        });
        dialogListToppings.show();
    }


}
