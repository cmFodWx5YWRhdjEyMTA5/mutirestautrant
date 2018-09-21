package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.AdapterItemFoodNew;
import com.hcpt.multirestaurants.adapter.ListCategoryAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Category;
import com.hcpt.multirestaurants.object.CategoryNew;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Product;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.util.NetworkUtil;

public class ListCategoryActivity extends BaseActivity {

    private TextView lblShopName;
    private ImageView btnBack;
    private CategoryNew arrCategoriesNew;
    private ListCategoryAdapter categoryAdapterNew;
    private int shopId = -1;
    private String shopName = "";
    public static BaseActivity self;
    private TextView tvCategory;
    private LinearLayout lnlCategory;
    private Dialog dialogListCategory;
    private RecyclerView rclListItem;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Menu> menuArrayList;
    private AdapterItemFoodNew adapterItemFoodNew;
    private ImageView imgSearch;
    private EditText edtFoodName;
    private Shop shop;
    private Boolean isFastSearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        self = this;
        initUI();
        initData();
        initControl();
        edtFoodName = findViewById(R.id.edtFoodName);
        edtFoodName.setText("");
        imgSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelManager.getSearchFoodListCategoryByShop(self, shopId, edtFoodName.getText().toString(), true,
                        new ModelManagerListener() {

                            @Override
                            public void onSuccess(Object object) {
                                // TODO Auto-generated method stub
                                String json = (String) object;
                                Debug.e("parseListCategoriNew");
                                arrCategoriesNew = ParserUtility.parseListCategoriesNew(json);
                                Debug.e(arrCategoriesNew.getStatus());
                                if (arrCategoriesNew.getStatus().equals("success")) {
                                    menuArrayList = new ArrayList<>();
                                    menuArrayList = arrCategoriesNew.getMenuArrayList();
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    rclListItem.setLayoutManager(mLayoutManager);
                                    adapterItemFoodNew = new AdapterItemFoodNew(self, menuArrayList);
                                    rclListItem.setAdapter(adapterItemFoodNew);
                                } else {
                                    Toast.makeText(ListCategoryActivity.this, "No have more Data", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void initUI() {
        lnlCategory = findViewById(R.id.lnlCategory);
        tvCategory = findViewById(R.id.tvCategory);
        lblShopName = findViewById(R.id.lblShopName);
        btnBack = findViewById(R.id.btnBack);
        rclListItem = findViewById(R.id.rclListItem);
        imgSearch = findViewById(R.id.imgSearch);
    }

    private void initControl() {
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lnlCategory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogListCateGory();
            }
        });
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
            shopName = b.getString(GlobalValue.KEY_SHOP_NAME);
            lblShopName.setText(shopName);
            String urlImageShop = b.getString("URL");
            Double shopVat = b.getDouble("VAT");
            Double shopShip = b.getDouble("SHIP");
            Debug.e(urlImageShop + shopVat + shopShip);
        }
        if (shopId != -1) {
            if (!NetworkUtil.checkNetworkAvailable(this)) {
                Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
            } else
                ModelManager.getListCategoryByShop(self, shopId, true,
                        new ModelManagerListener() {

                            @Override
                            public void onSuccess(Object object) {
                                // TODO Auto-generated method stub
                                String json = (String) object;
                                arrCategoriesNew = ParserUtility.parseListCategoriesNew(json);
                                categoryArrayList=arrCategoriesNew.getCategoryArrayList();
                            }

                            @Override
                            public void onError(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                            }
                        });
        }

    }

    private void showDialogListCateGory() {
        dialogListCategory = new Dialog(self);
        dialogListCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListCategory.setContentView(R.layout.dialog_select_category);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogListCategory.getWindow().getAttributes());
        lp.width = 5 * (displaymetrics.widthPixels / 5);
        dialogListCategory.getWindow().setAttributes(lp);
        dialogListCategory.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogListCategory.setCancelable(false);

        RecyclerView rclDialogCategory = dialogListCategory.findViewById(R.id.rclDialogCategory);
        TextView btnClose = dialogListCategory.findViewById(R.id.btnClose);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rclDialogCategory.setLayoutManager(mLayoutManager);
        categoryAdapterNew = new ListCategoryAdapter(self, categoryArrayList, fragmentCommunication);
        rclDialogCategory.setAdapter(categoryAdapterNew);

        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListCategory.dismiss();
            }
        });
        dialogListCategory.show();
    }

    ListCategoryAdapter.FragmentCommunication fragmentCommunication = new ListCategoryAdapter.FragmentCommunication() {
        @Override
        public void respond(int menuID, String menuName) {
            Debug.e(String.valueOf(menuID));
            dialogListCategory.dismiss();
            tvCategory.setText(menuName);
            initData2(menuID);

        }
    };

    private void initData2(int menuID) {
        menuArrayList = new ArrayList<>();
        menuArrayList = arrCategoriesNew.getMenuArrayList();
        ArrayList<Menu> productArrayList2 = new ArrayList<>();
        for (Menu item : menuArrayList) {
            if (menuID == item.getCategoryId()) {
                productArrayList2.add(item);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rclListItem.setLayoutManager(mLayoutManager);
                adapterItemFoodNew = new AdapterItemFoodNew(self, productArrayList2);
                rclListItem.setAdapter(adapterItemFoodNew);
            }
        }
    }
}
