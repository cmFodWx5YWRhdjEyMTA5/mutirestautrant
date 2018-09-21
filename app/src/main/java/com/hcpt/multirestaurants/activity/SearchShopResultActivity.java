package com.hcpt.multirestaurants.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.ShopAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.pulltorefresh.PullToRefreshBase;
import com.hcpt.multirestaurants.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.hcpt.multirestaurants.pulltorefresh.PullToRefreshListView;
import com.hcpt.multirestaurants.util.GPSTracker;
import com.hcpt.multirestaurants.util.ImageUtil;
import com.hcpt.multirestaurants.util.Logger;
import com.hcpt.multirestaurants.widget.AutoBgButton;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class SearchShopResultActivity extends BaseActivity implements
		 OnClickListener {

	private GoogleMap googleMap;
	private ArrayList<Shop> arrShop = new ArrayList<>();
	private Marker currentMaker;
	private HashMap<String, Shop> markerRestaurantMap;
	private PullToRefreshListView lsvShop;
	private ListView lsvActually;
	private Activity self;
	private AutoBgButton btnChooseList;
	private boolean isChooseList = true;
	private LinearLayout layoutMap;
	private ImageView btnBack;
	private AQuery aq;
	Bundle b;
	private GPSTracker gps;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.e("day nay");
		self = this;
		aq = new AQuery(this);
		gps = new GPSTracker(self);
		setContentView(R.layout.activity_search_shop_result);
		initUI();
		initControl();
//		updateUI();
//		initGoogleMap();
		setData();

	}

	private void initUI() {
		lsvShop = (PullToRefreshListView) findViewById(R.id.lsvShop);
		lsvActually = lsvShop.getRefreshableView();
		btnChooseList = (AutoBgButton) findViewById(R.id.btnChooseList);
		btnBack = (ImageView) findViewById(R.id.btnBack);
	}

	private void initControl() {

		lsvActually.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
									long arg3) {
				// TODO Auto-generated method stub
				Shop shop = arrShop.get(index - 1);
				gotoShopDetailActivity(shop.getShopId(),shop.getImage());
			}
		});

		if (lsvShop!=null)
			lsvShop.setOnRefreshListener(new OnRefreshListener2<ListView>() {
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

		btnChooseList.setOnClickListener(this);
//		btnChooseMap.setOnClickListener(this);
		btnBack.setOnClickListener(this);

	}

//	private void updateUI() {
//		if (isChooseList) {
//			lsvShop.setVisibility(View.VISIBLE);
//			layoutMap.setVisibility(View.GONE);
//		} else {
//			lsvShop.setVisibility(View.GONE);
//			layoutMap.setVisibility(View.VISIBLE);
//		}
//	}
//
//	private void initGoogleMap() {
//		initImageLoader();
//		int status = GooglePlayServicesUtil
//				.isGooglePlayServicesAvailable(getBaseContext());
//		if (status != ConnectionResult.SUCCESS) {
//			int requestCode = 10;
//			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
//					requestCode);
//			dialog.show();
//
//		} else {
//			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.map);
//			googleMap = fm.getMap();
//			googleMap.setMyLocationEnabled(true);
//			googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
//			googleMap
//					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
//
//						@Override
//						public void onInfoWindowClick(Marker marker) {
//							// TODO Auto-generated method stub
//							String title = marker.getTitle();
//							for (int i = 0; i < arrShop.size(); i++) {
//								if (arrShop.get(i).getShopName().equals(title)) {
//									gotoShopDetailActivity(arrShop.get(i)
//											.getShopId());
//									break;
//								}
//							}
//						}
//					});
//		}
//	}
//
//	private void setLocationLatLong(Location location) {
//		if (location!=null ){
//			double latitude = location.getLatitude();
//			double longitude = location.getLongitude();
//			LatLng latLng = new LatLng(latitude, longitude);
//			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//			googleMap.animateCamera(CameraUpdateFactory
//					.zoomTo(12));// zoom : 2-21
//		}
//
//	}

	private void setData() {

		// get data search ;
		Bundle b = getIntent().getExtras();
		searchKey = b.getString(GlobalValue.KEY_SEARCH);
		cityId = b.getString(GlobalValue.KEY_CITY_ID);
		categoryId = b.getString(GlobalValue.KEY_CATEGORY_ID);
		open = b.getString(GlobalValue.KEY_OPEN);
		distance = b.getString(GlobalValue.KEY_DISTANCE);
		sortBy = b.getString(GlobalValue.KEY_SORT_BY);
		sortType = b.getString(GlobalValue.KEY_SORT_TYPE);
		lat = b.getString(GlobalValue.KEY_LAT);
		lon = b.getString(GlobalValue.KEY_LONG);
		getDataSearch(page, false);
	}

	private void getDataSearch(int page, boolean isPull) {
		if (page <= 1) {
			arrShop.clear();
		}

		ModelManager.getListShopBySearch(self, searchKey, categoryId, cityId,
				page, open, distance, sortBy, sortType, lat, lon, !isPull,
				new ModelManagerListener() {

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						lsvShop.onRefreshComplete();
						Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(Object object) {
						String json = (String) object;
						ArrayList<Shop> arr = ParserUtility.getListShop(json);
						if (arr.size() > 0) {
							isMore = true;
							arrShop.addAll(arr);
//							setDataToMap(arrShop);
							setDataShopToList(arrShop);
						} else {
							isMore = false;
							Toast.makeText(self,getResources().getString(R.string.have_no_more_date),
									Toast.LENGTH_SHORT).show();
						}
						lsvShop.onRefreshComplete();

					}
				});
	}

//	private void setDataToMap(final ArrayList<Shop> arrShops) {
//		if (markerRestaurantMap == null) {
//			markerRestaurantMap = new HashMap<String, Shop>();
//		} else {
//			if (!markerRestaurantMap.isEmpty()) {
//				markerRestaurantMap.clear();
//			}
//			googleMap.clear();
//		}
//
//		Marker marker;
//		Bitmap bitmap = null;
//		for (final Shop shop : arrShops) {
//
//			LatLng item = new LatLng(shop.getLatitude(), shop.getLongitude());
//			marker = googleMap.addMarker(new MarkerOptions().position(item)
//					.title(shop.getShopName()));
//			try {
//				// set marker icon
//				bitmap = ImageUtil.createBitmapFromUrl(shop.getImage());
//				shop.setBmImage(bitmap);
//				// resize
//				int size = ImageUtil.getSizeBaseOnDensity(self, 30);
//				bitmap = ImageUtil.getResizedBitmap(bitmap, size, size);
//				// ser Bitmap to marker
//				marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
//			} catch (Exception e) {
//				marker.setIcon(BitmapDescriptorFactory
//						.fromResource(R.drawable.ic_address_map));
//			}
//
//			markerRestaurantMap.put(marker.getId(), shop);
//		}
//		googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
//
//		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
//
//			@Override
//			public void onInfoWindowClick(Marker marker) {
//				// TODO Auto-generated method stub
//				String title = marker.getTitle();
//
//				for (int i = 0; i < arrShops.size(); i++) {
//					if (arrShop.get(i).getShopName().equals(title)) {
//						gotoShopDetailActivity(arrShop.get(i).getShopId());
//						break;
//					}
//				}
//			}
//		});
//
//		if (gps.canGetLocation()) {
//			setLocationLatLong(gps.getLocation());
//		}
//
//	}

	private void setDataShopToList(ArrayList<Shop> arrShops) {
		ShopAdapter shopAdapter = new ShopAdapter(self, arrShops);
		shopAdapter.notifyDataSetChanged();
		lsvActually.setAdapter(shopAdapter);
	}

	private class CustomInfoWindowAdapter implements InfoWindowAdapter {

		private View v;

		public CustomInfoWindowAdapter() {
			v = getLayoutInflater().inflate(R.layout.map_detail, null);
		}

		@Override
		public View getInfoContents(Marker marker) {
			if (currentMaker != null && currentMaker.isInfoWindowShown()) {
				currentMaker.hideInfoWindow();
				currentMaker.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			currentMaker = marker;
			Logger.d("MapMaker iD ", marker.getId());
			Logger.d("MapMaker", markerRestaurantMap.toString());

			Shop shop = markerRestaurantMap.get(marker.getId());
			TextView lblName = (TextView) v.findViewById(R.id.lblShopName);
			TextView lblPhone = (TextView) v.findViewById(R.id.lblPhone);
			final ImageView imgShop = (ImageView) v.findViewById(R.id.img_map);
			TextView lblDescription = (TextView) v
					.findViewById(R.id.lblDescription);
			lblName.setSelected(true);
			lblDescription.setSelected(true);
			if (shop != null) {
				lblName.setText(shop.getShopName());
				lblDescription.setText(shop.getAddress());
				lblPhone.setText(shop.getPhone());
				if (shop.getImage() != null
						&& !shop.getImage().equalsIgnoreCase("")) {
					if (shop.getBmImage() != null) {
						aq.id(imgShop).image(shop.getBmImage());
					} else {
						aq.id(imgShop).image(shop.getImage(), true, true, 0,
								R.drawable.no_image_available,
								new BitmapAjaxCallback() {
									@Override
									public void callback(String url, ImageView iv,
														 Bitmap bm, AjaxStatus status) {

										if (bm != null) {
											Drawable d = new BitmapDrawable(
													getResources(), ImageUtil
													.getResizedBitmap(bm,
															150, 150));
											imgShop.setBackground(d);
										} else {
											imgShop.setBackgroundResource(R.drawable.no_image_available);
										}
									}
								});
					}
				} else {
					imgShop.setImageResource(R.drawable.ic_logo);
				}
			} else
				Logger.d("AAA", "Restaurant null");
			return v;
		}
	}

	public void gotoActive(Shop shop, Class<?> cls) {
		// startActivity(intent);
		Intent returnIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("id", shop.getShopId());
		bundle.putString("name", shop.getShopName());
		returnIntent.putExtra("result", bundle);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void gotoShopDetailActivity(int shopId,String urlImageShop) {
		Bundle bundle = new Bundle();
		bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId);
		gotoActivity(self, ShopDetailActivity.class, bundle);
	}

	private void initImageLoader() {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024;
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		final ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(
				this)
				.threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.memoryCache(
						new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)).build();

		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onClick(View v) {

		if (v==btnBack){
			onBackPressed();
		}
		// TODO Auto-generated method stub
//
//		} else if (v == btnChooseMap) {
//			isChooseList = fa	if (v == btnChooseList) {
////			isChooseList = true;
////			updateUI();lse;
//			updateUI();
//		} else if
//			(v == btnBack) {
//			onBackPressed();
//		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
