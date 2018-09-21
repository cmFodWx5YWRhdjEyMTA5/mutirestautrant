package com.hcpt.multirestaurants.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.ListCategoryActivity;
import com.hcpt.multirestaurants.activity.ListFoodActivity;
import com.hcpt.multirestaurants.activity.OfferActivity;
import com.hcpt.multirestaurants.activity.ShopDetailActivity;
import com.hcpt.multirestaurants.config.Constant;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Relish;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;

import java.util.ArrayList;

import static com.hcpt.multirestaurants.activity.ListCategoryActivity.self;

public class AdapterItemFoodNew extends RecyclerView.Adapter<AdapterItemFoodNew.ViewHolder> implements AdapterOptionTopping.OnCheckListerner, AdapterOptionTopping.OnSetDataListerner {
    private Activity context;
    private ArrayList<Menu> productArrayList;
    private Dialog dialogAddTopping;
    private Dialog dialogListToppings;
    private Dialog mDialog;
    private Shop shop;
    private Menu food;
    private ArrayList<Relish> relishArrayList;
    private AdapterOptionTopping adapterOptionTopping;

    public AdapterItemFoodNew(Activity context, ArrayList<Menu> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_add_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(productArrayList.get(position).getImage()).placeholder(R.drawable.no_image_available).into(holder.imgFood);
        holder.tvName.setText(productArrayList.get(position).getName());
        holder.tvCategory.setText(productArrayList.get(position).getCategoryProduct());
        holder.tvPrice.setText("$" + productArrayList.get(position).getPrice() + "");
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food = productArrayList.get(position);
                setDataToUI(food);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    @Override
    public void onCheckChange(boolean isChecked, int position) {
        relishArrayList.get(position).setChecker(isChecked);
    }

    @Override
    public void onCheckChange(Relish relish, int position, int p) {
        relishArrayList.set(position, relish);
        food.setRelishArrayList(relishArrayList);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFood;
        private TextView tvName;
        private TextView tvCategory;
        private TextView tvPrice;
        private TextView btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }

    private void showDialogAddItem(final Menu food) {
        dialogAddTopping = new Dialog(self);
        dialogAddTopping.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddTopping.setContentView(R.layout.dialog_add_item);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogAddTopping.getWindow().getAttributes());
        lp.width = 5 * (displaymetrics.widthPixels / 5);
//        lp.height = 5 * (displaymetrics.widthPixels / 5);
        dialogAddTopping.getWindow().setAttributes(lp);
        dialogAddTopping.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogAddTopping.setCancelable(false);

        ImageView imgFood = dialogAddTopping.findViewById(R.id.imgFood);
        TextView lblFoodName = dialogAddTopping.findViewById(R.id.lblFoodName);
        TextView lblCategory = dialogAddTopping.findViewById(R.id.lblCategory);
        TextView lblPrice = dialogAddTopping.findViewById(R.id.lblPrice);
        TextView txtNumberquantity = dialogAddTopping.findViewById(R.id.txtNumberquantity);
        TextView lblToppingPrice = dialogAddTopping.findViewById(R.id.lblToppingPrice);
        TextView btnAddTopping = dialogAddTopping.findViewById(R.id.btnAddTopping);
        TextView btnAddIntroduction = dialogAddTopping.findViewById(R.id.btnAddIntroduction);
        TextView lblSave = dialogAddTopping.findViewById(R.id.lblSave);
        TextView lblCancel = dialogAddTopping.findViewById(R.id.lblCancel);

        Glide.with(context).load(food.getImage()).into(imgFood);
        lblFoodName.setText(food.getName());
        lblCategory.setText(food.getCategoryProduct());
        lblPrice.setText(food.getPrice() + "");
        food.setOrderNumber(1);
        lblToppingPrice.setText("");

        btnAddTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Debug.e("CLICK ADD ITERM TO CART");
                showDialogListTopping(food);

            }
        });
        btnAddIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstructionDialog(food);

            }
        });
        txtNumberquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListQuantities(food);
            }
        });
        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food.getOrderNumber() == 0) {
                    food.setOrderNumber(1);
                }
                addToMyMenu(shop);
                dialogAddTopping.dismiss();
            }
        });
        lblCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddTopping.dismiss();
            }
        });

        dialogAddTopping.show();
    }

    private void setDataToUI(final Menu food) {
        ModelManager.getShopById(self, food.getShopId(), true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        shop = ParserUtility.parseShop(json);
                        showDialogAddItem(food);
                        Debug.e("shop cos gia tri r");
                    }

                    @Override
                    public void onError(VolleyError error) {
//                        Debug.e(error.getMessage());

                    }
                });
    }

    private void addToMyMenu(Shop shop) {
        for (int i = 0; i < GlobalValue.arrMyMenuShop.size(); i++) {
            if (shop.getShopId() == GlobalValue.arrMyMenuShop.get(i).getShopId()) {
                GlobalValue.arrMyMenuShop.get(i).addFoodOrder(food);
                CustomToast.showCustomAlert(self, "Add to Card", Toast.LENGTH_SHORT);
                return;
            }
        }
        shop.addFoodOrder(food);
        GlobalValue.arrMyMenuShop.add(shop);
        notifyDataSetChanged();
        CustomToast.showCustomAlert(self, "Add to Card", Toast.LENGTH_SHORT);
    }

    private void showListQuantities(final Menu food) {
        final Dialog dialog = new Dialog(self);
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
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(self,
                android.R.layout.simple_list_item_single_choice, numberlist);
        lsvCook.setAdapter(adapter);
        lsvCook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String a = 1 + position + "";
                food.setOrderNumber(Integer.parseInt(a));
                updateContent(food);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //add note
    private void showInstructionDialog(final Menu food) {
        mDialog = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
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
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void updateContent(final Menu food) {
        Debug.e(String.valueOf(food.getOrderNumber()));
        TextView txtNumberquantity = dialogAddTopping.findViewById(R.id.txtNumberquantity);
        TextView tvPrice = dialogAddTopping.findViewById(R.id.lblPrice);
        TextView tvnameTopping = dialogAddTopping.findViewById(R.id.lblToppingPrice);
        txtNumberquantity.setText(food.getOrderNumber() + "");
        double price = 0;
        StringBuilder listTopping = new StringBuilder();
        for (int i = 0; i < food.getRelishArrayList().size(); i++) {
            if (food.getRelishArrayList().get(i).getRelishOptionChoise() != null && !food.getRelishArrayList().get(i).getRelishOptionChoise().getName().equals(Constant.NONE)) {
                price = price + food.getRelishArrayList().get(i).getRelishPrice();
                listTopping.append(", ").append(food.getRelishArrayList().get(i).getRelishName() + ":\t" + food.getRelishArrayList().get(i).getRelishOptionChoise().getName());
            }
        }
        if (!listTopping.equals(""))
            tvnameTopping.setText(listTopping.toString());
        tvPrice.setText("$" + food.getPrice() + "+" + "(" + "$" +
                ((food.getPrice() + price) * food.getOrderNumber() - food.getPrice()) + ")");
    }

    private void showDialogListTopping(final Menu food) {
        dialogListToppings = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
        dialogListToppings.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListToppings.setContentView(R.layout.dialog_option);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogListToppings.getWindow().getAttributes());
        lp.width = 19 * (displaymetrics.widthPixels / 20);
//        lp.height = 3 * (displaymetrics.heightPixels / 5);
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
        adapterOptionTopping = new AdapterOptionTopping(self, relishArrayList, this, this);
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
                dialogListToppings.dismiss();
                updateContent(food);
            }
        });
        dialogListToppings.show();
    }
}
