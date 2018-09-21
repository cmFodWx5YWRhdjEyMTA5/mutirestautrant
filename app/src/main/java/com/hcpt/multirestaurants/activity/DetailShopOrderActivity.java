package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.ShopOrderAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.ShopOrder;
import com.hcpt.multirestaurants.util.NetworkUtil;

@SuppressLint("NewApi")
public class DetailShopOrderActivity extends BaseActivity implements
        OnClickListener {

    private ImageView btnBack;
    private ListView lsvShops;
    private ShopOrderAdapter shopAdapter;
    private ArrayList<ShopOrder> arrShopOrders;
    private String orderId;
    private TextView lblSum, lblVAT, lblShip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shop_order);
        initUI();
        initUIControls();
         initData();

    }

    private void initUI() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lsvShops = (ListView) findViewById(R.id.lsvShop);
        lblSum = (TextView) findViewById(R.id.lblSum);
        lblShip = (TextView) findViewById(R.id.lblShip);
        lblVAT = (TextView) findViewById(R.id.lblVAT);
    }

    private void initUIControls() {
        arrShopOrders = new ArrayList<>();
        shopAdapter = new ShopOrderAdapter(self, arrShopOrders);
        lsvShops.setAdapter(shopAdapter);
        lsvShops.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                GlobalValue.currentShopOrder = arrShopOrders.get(position);
                gotoActivity(DetailFoodOfShopOrderActivity.class);
            }
        });
        btnBack.setOnClickListener(this);
        updateOrderValue();
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        orderId = b.getString(GlobalValue.KEY_ORDER_ID);
        ModelManager.getListDetailOrder(self, orderId, true,
                new ModelManagerListener() {

                    @Override
                    public void onError(VolleyError error) {
                        Debug.e("error"+error.getMessage());
                        arrShopOrders.clear();
                        shopAdapter.notifyDataSetChanged();
                        updateOrderValue();
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        Debug.e("success");
                        // TODO Auto-generated method stub
                        String json = object.toString();
                        ArrayList<ShopOrder> list = ParserUtility
                                .parseListShopOrder(json);
                        Log.e("huy-log", "list-shop-order : " + list.size());
                        if (list.size() > 0) {
                            arrShopOrders.clear();
                            arrShopOrders.addAll(list);
                            shopAdapter.notifyDataSetChanged();
                            updateOrderValue();
                        }

                    }
                });
    }

    private void updateOrderValue() {
        double totalVAT = 0, totalShip = 0, totalPrice = 0;

        for (ShopOrder shop : arrShopOrders) {
            totalVAT += shop.getVAT();
            totalShip += shop.getShipping();
            totalPrice += shop.getGrandTotal();
        }

        lblVAT.setText(getString(R.string.vat) + getString(R.string.currency)
                + String.format("%.1f", totalVAT));
        lblShip.setText(getString(R.string.ship) + getString(R.string.currency)
                + String.format("%.1f", totalShip));
        lblSum.setText(getString(R.string.currency)
                + String.format("%.1f", totalPrice));
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
        if (!NetworkUtil.checkNetworkAvailable(this)) {
            Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
        } else {
            initData();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        }
    }
}
