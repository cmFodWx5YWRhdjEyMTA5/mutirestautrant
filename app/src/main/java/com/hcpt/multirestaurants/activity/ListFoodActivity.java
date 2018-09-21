package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.os.Bundle;
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
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.ListFoodAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.util.NetworkUtil;

public class ListFoodActivity extends BaseActivity implements OnClickListener {

    private TextView lblShopName, lblMenuName;
    private ImageView btnBack;
    private ArrayList<Menu> arrFood = new ArrayList<Menu>();
    private ListView lsvFood;
    private ListFoodAdapter foodAdapter;
    private int shopId = -1, categoryId = -1;
    private String shopName = "", categoryName = "";
    public static BaseActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_list_food);
        initUI();
        initControl();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (shopId != -1 && categoryId != -1) {
            ModelManager.getListFoodByShopAndCategory(self, shopId, categoryId,
                    true, new ModelManagerListener() {

                        @Override
                        public void onSuccess(Object object) {
                            // TODO Auto-generated method stub
                            String json = (String) object;
                            if(ParserUtility.parseListFood(json).size() == 0){
                                showToastMessage(getResources().getString(R.string.have_no_date));
                            }else{
                                arrFood.clear();
                                arrFood.addAll(ParserUtility.parseListFood(json));
                                foodAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            // TODO Auto-generated method stub
                            arrFood.clear();
                            foodAdapter.notifyDataSetChanged();
                            Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void initUI() {
        lsvFood = (ListView) findViewById(R.id.lsvFood);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblMenuName = (TextView) findViewById(R.id.lblMenuName);
        btnBack = (ImageView) findViewById(R.id.btnBack);
    }

    private void initControl() {
        foodAdapter = new ListFoodAdapter(self, arrFood);
        foodAdapter.showShop = false;
        lsvFood.setAdapter(foodAdapter);
        lsvFood.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                if (NetworkUtil.checkNetworkAvailable(self)) {
                    Menu food = arrFood.get(index);
                    Bundle b = new Bundle();
                    b.putInt(GlobalValue.KEY_FOOD_ID, food.getId());
                    b.putString(GlobalValue.KEY_SHOP_NAME, shopName);
                    b.putString(GlobalValue.KEY_CATEGORY_NAME, categoryName);

                    gotoActivity(self, FoodDetailActivity.class, b);
                } else {
                    Toast.makeText(self,
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        btnBack.setOnClickListener(this);
        lblShopName.setOnClickListener(this);
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)
                    && b.containsKey(GlobalValue.KEY_CATEGORY_ID)) {
                shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
                categoryId = b.getInt(GlobalValue.KEY_CATEGORY_ID);
            }
            if (b.containsKey(GlobalValue.KEY_SHOP_NAME)) {
                shopName = b.getString(GlobalValue.KEY_SHOP_NAME);
                lblShopName.setText(shopName);
            }
            if (b.containsKey(GlobalValue.KEY_CATEGORY_NAME)) {
                categoryName = b.getString(GlobalValue.KEY_CATEGORY_NAME);
                lblMenuName.setText(categoryName);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        } else if (v == lblShopName) {
            gotoShopDetail(shopId);
        }
    }
}
