package com.hcpt.multirestaurants.activity.tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.MyMenuActivity;
import com.hcpt.multirestaurants.adapter.ShopCartAdapter;
import com.hcpt.multirestaurants.adapter.ShopCartAdapter.ShopCartListener;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Relish;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.paypal.Paypal;
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.widget.AutoBgButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MyShopCartActivity extends BaseActivity implements OnClickListener {
    private static final int PAYPAL_METHOD = 0;
    private static final int BANKING_METHOD = 1;
    private static final int DELIVERY_METHOD = 2;
    private TextView btnOrder;
    private TextView lblSum, lblVAT, lblShip;
    private ListView lsvShops;
    private ShopCartAdapter shopAdapter;
    private double total;
    private String data;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shop_cart);
        Paypal.startPaypalService(this);
        initUI();
        initData();

    }

    private void initUI() {
        btnOrder = (TextView) findViewById(R.id.btnOrder);
        lblSum = (TextView) findViewById(R.id.lblSum);
        lsvShops = (ListView) findViewById(R.id.lsvShop);
        lblShip = (TextView) findViewById(R.id.lblShip);
        lblVAT = (TextView) findViewById(R.id.lblVAT);
        btnOrder.setOnClickListener(this);
    }

    private void initData() {

        shopAdapter = new ShopCartAdapter(self, GlobalValue.arrMyMenuShop,
                new ShopCartListener() {

                    @Override
                    public void showDetailOrder(int position) {
                        // TODO Auto-generated method stub
                        Bundle b = new Bundle();
                        b.putInt("position", position);
                        ((MainTabActivity) getParent()).gotoActivity(
                                MyMenuActivity.class, b);

                    }

                    @Override
                    public void deleteItem(int position) {
                        // TODO Auto-generated method stub
                        GlobalValue.arrMyMenuShop.remove(position);
                        shopAdapter.notifyDataSetChanged();
                    }
                });
        lsvShops.setAdapter(shopAdapter);

    }

    public void refreshContent() {

        total = (double) 0;
        shopAdapter.notifyDataSetChanged();
        double totalOfShop = 0;
        double VATOfShop = 0;
        double ShipPriceOfShop = 0;

        for (int i = 0; i < GlobalValue.arrMyMenuShop.size(); i++) {
            Shop shop = GlobalValue.arrMyMenuShop.get(i);
            if (shop.getArrOrderFoods().size() == 0) {
                GlobalValue.arrMyMenuShop.remove(i);
            } else {
                totalOfShop += shop.getTotalPrice();
                VATOfShop += shop.getCurrentTotalVAT();
                ShipPriceOfShop += shop.getcurrentShipping();
            }
        }

        total = totalOfShop + VATOfShop + ShipPriceOfShop;

        lblVAT.setText("VAT: " + getString(R.string.currency)
                + String.format("%.1f", VATOfShop));
        lblShip.setText("Ship: " + getString(R.string.currency)
                + String.format("%.1f", ShipPriceOfShop));
        lblSum.setText(getString(R.string.currency)
                + String.format("%.1f", total));
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        refreshContent();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnOrder:
                onBtnOrderClick();
                break;
            default:
                break;
        }
    }

    private void onBtnOrderClick() {
        if (GlobalValue.myAccount == null) {
            CustomToast.showCustomAlert(self,
                    self.getString(R.string.message_no_login),
                    Toast.LENGTH_SHORT);
        } else {
            if (GlobalValue.arrMyMenuShop.size() > 0) {
                showDeliveryInfo();
            } else {
                CustomToast.showCustomAlert(self,
                        self.getString(R.string.message_no_item_menu),
                        Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        this.getParent().onBackPressed();
    }

    // $data='{"account_id":"1396947160",
    // "address" : "",
    // "item":[
    // {"shop_id":29,"food_id":58,"number":2,"price":4},
    // {"shop_id":26,"food_id":51,"number":2,"price":4},
    // {"shop_id":26,"food_id":54,"number":3,"price":4}
    // ]}';

    private String createOfferJson(ArrayList<Shop> arrShops, String address) {
        JSONObject jsonOrder = new JSONObject();
        JSONArray jsonFoods = new JSONArray();
        JSONObject jsonFood;
        double price;
        try {
            for (Shop shop : arrShops) {

                for (Menu menu : shop.getArrOrderFoods()) {

                    price = menu.getPrice()
                            - (menu.getPrice() * menu.getPercentDiscount() / 100);

                    jsonFood = new JSONObject();
                    jsonFood.put(WebServiceConfig.KEY_ORDER_SHOP, menu.getShopId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_FOOD, menu.getId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_NUMBER_FOOD, menu.getOrderNumber());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_PRICE_FOOD, round(price, 2));

                    jsonFoods.put(jsonFood);


                }
            }

            jsonOrder.put(WebServiceConfig.KEY_ORDER_ACCOUT_ID,
                    GlobalValue.myAccount.getId());
            jsonOrder.put(WebServiceConfig.KEY_ORDER_ADDRESS, address);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_ITEM, jsonFoods);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonOrder.toString();
    }

    public static double round(double number, int digit) {
        if (digit > 0) {
            int temp = 1, i;
            for (i = 0; i < digit; i++)
                temp = temp * 10;
            number = number * temp;
            number = Math.round(number);
            number = number / temp;
            return number;
        } else
            return 0.0;
    }

    private void showDeliveryInfo() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_details_order);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = 3 * (displaymetrics.heightPixels / 5);
        params.width = 4 * (displaymetrics.widthPixels / 5);

        dialog.getWindow().setAttributes(params);
        dialog.setCancelable(false);

        AutoBgButton btnOk = (AutoBgButton) dialog.findViewById(R.id.btnOk);
        AutoBgButton btnCancel = (AutoBgButton) dialog
                .findViewById(R.id.btnCancel);
        final EditText txtFullName = (EditText) dialog
                .findViewById(R.id.txtFullName);
        final EditText txtEmail = (EditText) dialog.findViewById(R.id.txtEmail);
        final EditText txtPhone = (EditText) dialog.findViewById(R.id.txtPhone);
        final EditText txtAddress = (EditText) dialog
                .findViewById(R.id.txtAddress);

        txtFullName.setText(GlobalValue.myAccount.getFull_name());
        txtEmail.setText(GlobalValue.myAccount.getEmail());
        txtPhone.setText(GlobalValue.myAccount.getPhone());
        txtAddress.setText(GlobalValue.myAccount.getAddress());

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                String fullName = txtFullName.getText().toString();
//                String email = txtEmail.getText().toString();
//                String phone = txtPhone.getText().toString();
                String address = txtAddress.getText().toString();

                if (!address.isEmpty()) {
                    data = createOfferJson(GlobalValue.arrMyMenuShop,
                            txtAddress.getText().toString());

                    Log.e("Huy -test ", "Log json data =" + data);

                    sendListOrder(data, DELIVERY_METHOD);
                    dialog.dismiss();
                } else {
                    Toast.makeText(self, "Please input delivery address !",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
        hideKeyboard();
    }

    private void showTypePaymentDialog() {
        // parse data
        data = createOfferJson(GlobalValue.arrMyMenuShop,
                GlobalValue.myAccount.getAddress());

        AlertDialog levelDialog;
        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {"Online PayPal", "Banking deposit",
                "Cash on Delivery"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
        builder.setTitle("Select Payment Method :");
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                Paypal.requestPaypalPayment(MyShopCartActivity.this, total);
                                break;
                            case 1:
                                sendListOrder(data, BANKING_METHOD);
                                break;
                            case 2:
                                sendListOrder(data, DELIVERY_METHOD);
                                break;

                        }
                        dialog.dismiss();
                    }
                });
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void showBankInfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
        builder.setTitle("Banking Information :");
        builder.setMessage(Html.fromHtml(message));
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void sendListOrder(String data, final int paymentMethod) {
        ModelManager.sendListOrder(self, data, paymentMethod, true, "",
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String strJson = (String) object;
                        try {
                            JSONObject json = new JSONObject(strJson);
                            if (json.getString(WebServiceConfig.KEY_STATUS)
                                    .equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                                if (paymentMethod != BANKING_METHOD) {
                                    CustomToast.showCustomAlert(
                                            self,
                                            self.getString(R.string.message_success),
                                            Toast.LENGTH_SHORT);
                                } else {
                                    showBankInfo(json.getString("message"));
                                }
                                GlobalValue.arrMyMenuShop.clear();
                                shopAdapter.notifyDataSetChanged();
                                refreshContent();
                            } else {
                                CustomToast.showCustomAlert(
                                        self,
                                        self.getString(R.string.message_order_false),
                                        Toast.LENGTH_SHORT);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Paypal.PAYPAL_REQUEST_CODE) {

            if (Paypal.isConfirm(data)) {


                Toast toast = Toast.makeText(self,
                        "Payment successfully !", Toast.LENGTH_LONG);
                toast.setGravity(
                        Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                sendListOrder(this.data, PAYPAL_METHOD);


            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
    }


}
