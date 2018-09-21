package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.hcpt.multirestaurants.pulltorefresh.PullToRefreshBase;
import com.hcpt.multirestaurants.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.hcpt.multirestaurants.pulltorefresh.PullToRefreshListView;

public class SearchListFoodResultActivity extends BaseActivity implements
		OnClickListener {

	private ImageView btnBack;
	private ArrayList<Menu> arrFood;
	private PullToRefreshListView lsvFood;
	private ListView lsvActually;
	private ListFoodAdapter foodAdapter;
	public static BaseActivity self;

	private int page = 1;
	private String searchKey = "";
	private String categoryId = "";
	private String cityId = "";
	private String open = "";
	private String distance = "";
	private String sortBy = "";
	private String sortType = "";
	private String lat = "";
	private String lon = "";
	private boolean isMore = true;

	public static final String SEARCH_SCREEN = "searchActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_search_menu);
		initUI();
		initControl();
		initData();

	}

	private void initUI() {
		lsvFood = (PullToRefreshListView) findViewById(R.id.lsvfood);
		lsvActually = lsvFood.getRefreshableView();
		btnBack = (ImageView) findViewById(R.id.btnBack);
	}

	private void initControl() {

		initListFood();

		lsvActually.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				Menu food = arrFood.get(index - 1);
				Bundle b = new Bundle();
				b.putInt(GlobalValue.KEY_FOOD_ID, food.getId());
				b.putString(GlobalValue.KEY_NAVIGATE_TYPE, "FAST");
				GlobalValue.KEY_LOCAL_NAME = food.getLocalName();
				b.putString(GlobalValue.KEY_FROM_SCREEN, SEARCH_SCREEN);
				gotoActivity(self, FoodDetailActivity.class, b);

			}
		});

		lsvFood.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(self,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				page = 1;
				isMore = true;
				getDataSearch(page, true);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (isMore) {
					page++;
				}
				getDataSearch(page, true);
			}
		});

		btnBack.setOnClickListener(this);
	}

	private void initListFood() {
		arrFood = new ArrayList<Menu>();
		foodAdapter = new ListFoodAdapter(self, arrFood);
		foodAdapter.showShop = true;
		lsvActually.setAdapter(foodAdapter);
	}

	private void getDataSearch(int page, boolean isPull) {
		if (page <= 1) {
			arrFood.clear();
		}

		ModelManager.getListFoodBySearch(self, searchKey, categoryId, cityId,
				page, open, distance, sortBy, sortType, lat, lon, !isPull,
				new ModelManagerListener() {

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
						lsvFood.onRefreshComplete();
					}

					@Override
					public void onSuccess(Object object) {
						String json = (String) object;
						ArrayList<Menu> arr = ParserUtility.parseListFoodInSearch(json);
						if (arr.size() > 0) {
							isMore = true;
							arrFood.addAll(arr);
							foodAdapter.notifyDataSetChanged();
						} else {
							isMore = false;
							Toast.makeText(self, "No more data !",
									Toast.LENGTH_SHORT).show();
						}
						lsvFood.onRefreshComplete();

					}
				});
	}

	private void initData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			searchKey = b.getString(GlobalValue.KEY_SEARCH);
			cityId = b.getString(GlobalValue.KEY_CITY_ID);
			categoryId = b.getString(GlobalValue.KEY_CATEGORY_ID);
			open = b.getString(GlobalValue.KEY_OPEN);
			distance = b.getString(GlobalValue.KEY_DISTANCE);
			sortBy = b.getString(GlobalValue.KEY_SORT_BY);
			sortType = b.getString(GlobalValue.KEY_SORT_TYPE);
			lat = b.getString(GlobalValue.KEY_LAT);
			lon = b.getString(GlobalValue.KEY_LONG);
		}
		page = 1;
		getDataSearch(page, false);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnBack) {
			onBackPressed();
		}
	}
}
