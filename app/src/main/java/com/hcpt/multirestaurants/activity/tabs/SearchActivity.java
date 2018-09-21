package com.hcpt.multirestaurants.activity.tabs;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.SearchListFoodResultActivity;
import com.hcpt.multirestaurants.activity.SearchShopResultActivity;
import com.hcpt.multirestaurants.adapter.CategoryAdapter;
import com.hcpt.multirestaurants.adapter.CityAdapter;
import com.hcpt.multirestaurants.adapter.SpinnerSimpleAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Category;
import com.hcpt.multirestaurants.object.City;
import com.hcpt.multirestaurants.util.NetworkUtil;
import com.hcpt.multirestaurants.widget.AutoBgButton;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class SearchActivity extends BaseActivity implements OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Spinner spnCategories, spnCity, spnSortBy, spnSortType;
    private ArrayList<City> arrCities = new ArrayList<>();
    private ArrayList<Category> arrCategory = new ArrayList<>();
    private EditText edtSearch;
    private TextView btnSearch;
    private LinearLayout btnSelectShop, btnSelectMenu, btnSelectAll,
            btnSelectOpen;
    private ImageView imgticShop, imgticMenu, imgTickAll, imgTickOpen;
    private TextView lblMenu, lblShop, lblAll, lblOpen, lblDistance;
    private boolean isSelectShop = true;
    private boolean isOpen = false;

    private String cityId = "";
    private String categoryId = "";

    private SeekBar skbDistance;

    private static final String ALL = "0";
    private static final String OPEN = "1";
    private static String ALL_OR_OPEN = ALL;

    private static final String SORT_BY_RATING = "rate";
    private static final String SORT_BY_NAME = "name";
    private static final String SORT_BY_DATE = "date";
    private static String SORT_BY = SORT_BY_RATING;

    private GoogleApiClient mGoogleApiClient;

    private static final String SORT_TYPE_DESC = "desc";
    private static final String SORT_TYPE_ASC = "asc";
    private static String SORT_TYPE = SORT_TYPE_DESC;

    private String distance = "0";
    private String lat = "", lon = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_search);
        initGoogleApi();
        initUI();
        initControl();
        setData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            refreshContent();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void refreshContent() {
        if ((arrCategory.size() < 2) || (arrCities.size() < 2)) {
            setData();
        }
    }

    private void initUI() {
        spnCategories = (Spinner) findViewById(R.id.spnCategories);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnSortBy = (Spinner) findViewById(R.id.spnSortBy);
        spnSortType = (Spinner) findViewById(R.id.spnSortType);
        btnSearch = (TextView) findViewById(R.id.btnSearch);
        btnSelectMenu = (LinearLayout) findViewById(R.id.btnSelectMenu);
        btnSelectShop = (LinearLayout) findViewById(R.id.btnSelectShop);
        btnSelectAll = (LinearLayout) findViewById(R.id.btnSelectAll);
        btnSelectOpen = (LinearLayout) findViewById(R.id.btnSelectOpen);
        imgticMenu = (ImageView) findViewById(R.id.imgticMenu);
        imgticShop = (ImageView) findViewById(R.id.imgticShop);
        imgTickAll = (ImageView) findViewById(R.id.imgTickAll);
        imgTickOpen = (ImageView) findViewById(R.id.imgTickOpen);
        lblMenu = (TextView) findViewById(R.id.lblMenu);
        lblShop = (TextView) findViewById(R.id.lblShop);
        lblAll = (TextView) findViewById(R.id.lblAll);
        lblOpen = (TextView) findViewById(R.id.lblOpen);
        lblDistance = (TextView) findViewById(R.id.lbl_distance);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        skbDistance = (SeekBar) findViewById(R.id.skb_distance);
    }

    private void initControl() {

        btnSearch.setOnClickListener(this);
        btnSelectMenu.setOnClickListener(this);
        btnSelectShop.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);
        btnSelectOpen.setOnClickListener(this);

        spnCity.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
                if (index != 0) {
                    cityId = arrCities.get(index).getId() + "";
                } else {
                    cityId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnCategories.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int index, long arg3) {
                // TODO Auto-generated method stub
                if (index != 0) {
                    categoryId = arrCategory.get(index).getId() + "";
                } else {
                    categoryId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnSortBy.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    SORT_BY = SORT_BY_RATING;
                } else if (position == 1) {
                    SORT_BY = SORT_BY_NAME;
                } else if (position == 2) {
                    SORT_BY = SORT_BY_DATE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spnSortType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0) {
                    SORT_TYPE = SORT_TYPE_DESC;
                } else {
                    SORT_TYPE = SORT_TYPE_ASC;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        skbDistance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                distance = progress + "";
                if (distance.equals("0")) {
                    lblDistance.setText("All");
                } else {
                    lblDistance.setText(distance + " Km");
                }
            }
        });
    }

    private void setData() {
        // set up city
        arrCities.clear();
        arrCities.add(new City("All Cities"));

        ModelManager.getListCity(this, true, new ModelManagerListener() {

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                ArrayList<City> arr = ParserUtility.parseListCity(json);
                arrCities.addAll(arr);
                setDataCityToList(arrCities);

            }

            @Override
            public void onError(VolleyError error) {
                // TODO Auto-generated method stub
                setDataCityToList(arrCities);
                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
            }
        });

        // setup category
        arrCategory.clear();
        arrCategory.add(new Category());

        ModelManager.getListCategory(this, true, new ModelManagerListener() {

            @Override
            public void onSuccess(Object object) {
                String json = (String) object;
                ArrayList<Category> arr = ParserUtility
                        .parseListCategories(json);
                arrCategory.addAll(arr);
                setDataCategoryToList(arrCategory);

            }

            @Override
            public void onError(VolleyError error) {
                // TODO Auto-generated method stub
                setDataCategoryToList(arrCategory);
                Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
            }
        });

        setSortByData();
        setSortTypeData();
        updateAllOpenButton();
    }

    private void updateMenuShopButton() {
        if (isSelectShop) {
            // set select shop
            btnSelectShop
                    .setBackgroundResource(R.drawable.bg_button_select_right);
            imgticShop.setVisibility(View.VISIBLE);
            lblShop.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            // set unselect menu
            btnSelectMenu
                    .setBackgroundResource(R.drawable.bg_button_not_select_left);
            imgticMenu.setVisibility(View.GONE);
            lblMenu.setTextColor(ContextCompat.getColor(this, R.color.primary_dark));
        } else {
            // set select shop
            btnSelectShop
                    .setBackgroundResource(R.drawable.bg_button_not_select_right);
            imgticShop.setVisibility(View.GONE);
            lblShop.setTextColor(ContextCompat.getColor(this, R.color.primary_dark));
            // set unselect menu
            btnSelectMenu
                    .setBackgroundResource(R.drawable.bg_button_select_left);
            imgticMenu.setVisibility(View.VISIBLE);
            lblMenu.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
        }
    }

    private void updateAllOpenButton() {
        if (isOpen) {
            // set select shop
            ALL_OR_OPEN = OPEN;
            btnSelectOpen
                    .setBackgroundResource(R.drawable.bg_button_select_right);
            imgTickOpen.setVisibility(View.VISIBLE);
            lblOpen.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            // set unselect menu
            btnSelectAll
                    .setBackgroundResource(R.drawable.bg_button_not_select_left);
            imgTickAll.setVisibility(View.GONE);
            lblAll.setTextColor(ContextCompat.getColor(this, R.color.primary_dark));
        } else {
            // set select shop
            ALL_OR_OPEN = ALL;
            btnSelectOpen
                    .setBackgroundResource(R.drawable.bg_button_not_select_right);
            imgTickOpen.setVisibility(View.GONE);
            lblOpen.setTextColor(ContextCompat.getColor(this, R.color.primary_dark));
            // set unselect menu
            btnSelectAll
                    .setBackgroundResource(R.drawable.bg_button_select_left);
            imgTickAll.setVisibility(View.VISIBLE);
            lblAll.setTextColor(ContextCompat.getColor(this,R.color.cl_white));
        }
    }

    private void setDataCityToList(ArrayList<City> arrCity) {
        CityAdapter cityAdapter = new CityAdapter(self,
                android.R.layout.simple_spinner_item, arrCity);
        cityAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(cityAdapter);
        spnCity.setSelection(0);
    }

    private void setDataCategoryToList(ArrayList<Category> arrCategory) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(self,
                android.R.layout.simple_spinner_item, arrCategory);
        categoryAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategories.setAdapter(categoryAdapter);
        spnCategories.setSelection(0);
    }

    private void setSortByData() {
        SpinnerSimpleAdapter sortByAdapter = new SpinnerSimpleAdapter(self,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.arr_sort_by));
        spnSortBy.setAdapter(sortByAdapter);
    }

    private void setSortTypeData() {
        SpinnerSimpleAdapter sortTypeAdapter = new SpinnerSimpleAdapter(self,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.arr_sort_type));
        spnSortType.setAdapter(sortTypeAdapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSelectMenu) {
            isSelectShop = false;
            updateMenuShopButton();
            return;
        }
        if (v == btnSelectShop) {
            isSelectShop = true;
            updateMenuShopButton();
            return;
        }
        if (v == btnSelectOpen) {
            ALL_OR_OPEN = OPEN;
            isOpen = true;
            updateAllOpenButton();
            return;
        }
        if (v == btnSelectAll) {
            ALL_OR_OPEN = ALL;
            isOpen = false;
            updateAllOpenButton();
            return;
        }
        if (v == btnSearch) {
            if (NetworkUtil.checkNetworkAvailable(self))
                onClickSearch();
            else {
                Toast.makeText(self, R.string.message_network_is_unavailable,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onClickSearch() {
        // TODO Auto-generated method stub
        getLatLong();
        Bundle b = new Bundle();
        b.putString(GlobalValue.KEY_SEARCH, edtSearch.getText().toString());
        b.putString(GlobalValue.KEY_CATEGORY_ID, categoryId);
        b.putString(GlobalValue.KEY_CITY_ID, cityId);
        b.putString(GlobalValue.KEY_OPEN, ALL_OR_OPEN);
        b.putString(GlobalValue.KEY_DISTANCE, distance);
        b.putString(GlobalValue.KEY_SORT_BY, SORT_BY);
        b.putString(GlobalValue.KEY_SORT_TYPE, SORT_TYPE);
        b.putString(GlobalValue.KEY_LAT, lat);
        b.putString(GlobalValue.KEY_LONG, lon);
        if (!distance.equals("0") && (lat.length() == 0 || lon.length() ==  0) ){
            distance = "0";
        }

        if (isSelectShop){
            Debug.e("SEARCH SHOP RESULT ACTIVITY");
            ((MainTabActivity) getParent()).gotoActivity(
                    SearchShopResultActivity.class, b);
        }
        else{
            Debug.e("SEARCH FOOD RESULT ACTIVITY");
            ((MainTabActivity) getParent()).gotoActivity(
                    SearchListFoodResultActivity.class, b);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        this.getParent().onBackPressed();
    }

    private void initGoogleApi(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(self)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void getLatLong() {

        /*new MapsUtil.GetCurrentLatLong(self, new IMaps() {

            @Override
            public void processFinished(Object obj) {
                LatLng latLng = (LatLng) obj;
                lat = latLng.latitude + "";
                lon = latLng.longitude + "";
            }
        }).execute();*/

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
            lat = String.valueOf(lastLocation.getLatitude());
            lon = String.valueOf(lastLocation.getLongitude());

        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        getLatLong();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
