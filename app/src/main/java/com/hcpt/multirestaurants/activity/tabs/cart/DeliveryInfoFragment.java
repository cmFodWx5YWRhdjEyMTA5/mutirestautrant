package com.hcpt.multirestaurants.activity.tabs.cart;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseFragment;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.MainCartActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.util.DialogUtility;
import com.hcpt.multirestaurants.util.NetworkUtil;
import com.hcpt.multirestaurants.widget.AutoBgButton;

@SuppressLint("NewApi")
public class DeliveryInfoFragment extends BaseFragment implements
        OnClickListener {

    public static final String MSG_SUCESS = "success";
    private static final int PAYPAL_METHOD = 0;
    private static final int BANKING_METHOD = 1;
    private static final int DELIVERY_METHOD = 2;
    private View view;
    private EditText txtFullName, txtEmail, txtPhone, txtAddress;
    private AutoBgButton btnOk, btnCancel;
    private String data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater
                .inflate(R.layout.dialog_details_order, container, false);
        initUI(view);
        initControls();
        return view;
    }

    private void initUI(View view) {
        btnOk = (AutoBgButton) view.findViewById(R.id.btnOk);
        btnCancel = (AutoBgButton) view.findViewById(R.id.btnCancel);
        txtFullName = (EditText) view.findViewById(R.id.txtFullName);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        txtAddress = (EditText) view.findViewById(R.id.txtAddress);

    }

    private void initControls() {
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshContent();
        }
    }

    @Override
    public void refreshContent() {
        txtFullName.setText(GlobalValue.myAccount.getFull_name());
        txtEmail.setText(GlobalValue.myAccount.getEmail());
        txtPhone.setText(GlobalValue.myAccount.getPhone());
        txtAddress.setText(GlobalValue.myAccount.getAddress());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnOk) {
            onClickOrder();

        } else if (v == btnCancel) {
            // hidden keyboard :
            hiddenKeyboard();
            MainCartActivity activity = (MainCartActivity) getCurrentActivity();
            activity.onBackPressed();
        }
    }

    private void onClickOrder() {
        // TODO Auto-generated method stub
        String fullName = txtFullName.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        String address = txtAddress.getText().toString();

        if (!address.isEmpty()) {
            data = createOfferJson(GlobalValue.arrMyMenuShop,fullName,email,phone,address);
            Log.e("Huy -test ", "Log json data =" + data);
            if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
                sendListOrder(data, DELIVERY_METHOD);
            } else {
                Toast.makeText(getCurrentActivity(),
                        R.string.message_network_is_unavailable,
                        Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getCurrentActivity(),
                    "Please input delivery address !", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void sendListOrder(String data, final int paymentMethod) {
        ModelManager.sendListOrder(getCurrentActivity(), data, paymentMethod,
                true, "", new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String strJson = (String) object;
                        try {
                            JSONObject json = new JSONObject(strJson);
                            if (json.getString(WebServiceConfig.KEY_STATUS)
                                    .equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                                if (paymentMethod != 1) {
                                    CustomToast.showCustomAlert(
                                            getCurrentActivity(),
                                            getCurrentActivity().getString(
                                                    R.string.message_success),
                                            Toast.LENGTH_SHORT);
                                }
                                // clear list
                                GlobalValue.arrMyMenuShop.clear();
                                // go back list shop cart
                                ((MainCartActivity) getCurrentActivity())
                                        .onBackPressed();

                            } else {
                                CustomToast.showCustomAlert(
                                        getCurrentActivity(),
                                        getCurrentActivity().getString(
                                                R.string.message_order_false),
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

    private void showBankInfo(String message) {
        DialogUtility.alert(getCurrentActivity(), "Banking Information :", Html.fromHtml(message).toString());
    }

    private String createOfferJson(ArrayList<Shop> arrShops,String name,String phone,String email, String address) {
        JSONObject jsonOrder = new JSONObject();
        JSONArray jsonFoods = new JSONArray();
        JSONObject jsonFood = null;
        Double price = null;

        try {
            for (Shop shop : arrShops) {

                for (Menu menu : shop.getArrOrderFoods()) {

                    price = menu.getPrice()
                            - (menu.getPrice() * menu.getPercentDiscount() / 100);

                    jsonFood = new JSONObject();
                    jsonFood.put(WebServiceConfig.KEY_ORDER_SHOP,
                            menu.getShopId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_FOOD, menu.getId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_NUMBER_FOOD,
                            menu.getOrderNumber());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_PRICE_FOOD,
                            round(price, 2));
                    jsonFoods.put(jsonFood);

                }
            }

            jsonOrder.put(WebServiceConfig.KEY_ORDER_ACCOUT_ID,
                    GlobalValue.myAccount.getId());
            jsonOrder.put(WebServiceConfig.KEY_ORDER_NAME, name);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_PHONE, phone);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_EMAIL, email);
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

}
