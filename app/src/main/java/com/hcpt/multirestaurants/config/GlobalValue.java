package com.hcpt.multirestaurants.config;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hcpt.multirestaurants.object.Account;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.object.ShopOrder;
import com.hcpt.multirestaurants.util.MySharedPreferences;

public class GlobalValue {

	public static MySharedPreferences preferences;
	private static Typeface typeface;
	public static ArrayList<Shop> arrMyMenuShop =new ArrayList<>();
	public static ShopOrder currentShopOrder;
	public final static float ZOOM_SIZE = 12;
	public static Account myAccount;
	public static LatLng glatlng;
	public static boolean autoBack = false;

	public static final int COMMENT_PAGE = 5;

	public static void constructor(Context context) {
		if (preferences == null) {
			preferences = new MySharedPreferences(context);
		}
		createTypeFace(context);
	}

	public static void createTypeFace(Context context) {
		typeface = Typeface.createFromAsset(context.getAssets(),
				preferences.getFont());
	}

	public static void setTypeFace(TextView... lbl) {
		for (TextView textView : lbl) {
			textView.setTextSize(preferences.getFontSize());
			textView.setTypeface(typeface);
		}
	}

	public static void setTypeFace(Context context, String font, float size,
			TextView... lbl) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
		for (TextView textView : lbl) {
			textView.setTextSize(size);
			textView.setTypeface(tf);
		}
	}

	// ****************** TAB *******************************
	public final static String KEY_TAB_HOME = "tab_home";
	public final static String KEY_TAB_SEARCH = "tab_search";
	public final static String KEY_TAB_MY_MENU = "tab_myMenu";
	public final static String KEY_TAB_SETTING = "tab_setting";

	// ***************** Key ********************************
	public static String KEY_CITY_ID = "city_id";
	public final static String KEY_SHOP_ID = "shop_id";
	public final static String KEY_SHOP_NAME = "shop_name";
	public final static String KEY_OFFER_ID = "offer_id";
	public static String KEY_CATEGORY_ID = "category_id";
	public final static String KEY_CATEGORY_NAME = "category_name";
	public final static String KEY_NAVIGATE_TYPE = "KEY_NAVIGATE_TYPE";
	public static final String KEY_FROM_SCREEN = "fromScreen";
	public static String KEY_LOCAL_NAME = "location_name";
	public static String KEY_ORDER_ID = "order_id";

	public final static String KEY_FOOD_ID = "food_id";
	public final static String KEY_SEARCH = "keyword";

	public static final String KEY_OPEN = "open";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_SORT_BY = "sortBy";
	public static final String KEY_SORT_TYPE = "sortType";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LONG = "long";
}
