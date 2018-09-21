package com.hcpt.multirestaurants.config;

import android.content.Context;

import com.hcpt.multirestaurants.R;

public final class WebServiceConfig {

	// action
	public static String getUrlAllShop(Context context) {
		return context.getString(R.string.server_backend_link) + "getListShop";
	}

	public static String getUrlShop(Context context) {
		return context.getString(R.string.server_backend_link) + "getListShopBySearch";
	}

	public static String getUrlMenu(Context context) {
		return context.getString(R.string.server_backend_link) + "getListFoodBySearch";
	}

	public static String getUrlOfferOfDay(Context context) {
		return context.getString(R.string.server_backend_link) + "getListPromotionOfDay";
	}

	public static String getUrlOfferByShop(Context context) {
		return context.getString(R.string.server_backend_link) + "getListPromotionByShop";
	}

	public static String getUrlOfferById(Context context) {
		return context.getString(R.string.server_backend_link) + "getPromotionById";
	}

	public static String getUrlAllCity(Context context) {
		return context.getString(R.string.server_backend_link) + "getListCity";
	}

	public static String getUrlAllCategory(Context context) {
		return context.getString(R.string.server_backend_link) + "getListCategory";
	}

	public static String getUrlCategoryByID(Context context) {
		return context.getString(R.string.server_backend_link) + "getCategoryById";
	}

	public static String getUrlShopById(Context context) {
		return context.getString(R.string.server_backend_link) + "getShopById";
	}

	public static String getUrlShopbyCityAndCategory(Context context) {
		return context.getString(R.string.server_backend_link) + "getListShopByCityAndCategory";
	}

	public static String getUrlShopByCategory(Context context) {
		return context.getString(R.string.server_backend_link) + "getListShopByCategory";
	}

	public static String getUrlShopByCity(Context context) {
		return context.getString(R.string.server_backend_link) + "getListShopByCity";
	}

	public static String getUrlCategoryByShop(Context context) {
		return context.getString(R.string.server_backend_link) + "getListCategoryByShop";
	}

	public static String getUrlFoodByShopAnMenu(Context context) {
		return context.getString(R.string.server_backend_link) + "getListFoodByShopAndMenu";
	}

	public static String getUrlGetFoodById(Context context) {
		return context.getString(R.string.server_backend_link) + "getFoodById";
	}

	public static String getUrlOpenHourByShop(Context context) {
		return context.getString(R.string.server_backend_link) + "getListOpenHourByShop";
	}

	public static String getUrlListFoodOfDay(Context context) {
		return context.getString(R.string.server_backend_link) + "getListFoodOfDay";
	}

	public static String getUrlListFoodByPromotion(Context context) {
		return context.getString(R.string.server_backend_link) + "getListFoodByPromotion";
	}

	public static String getUrlSendOrder(Context context) {
		return context.getString(R.string.server_backend_link) + "sendOrder";
	}
	public static String getUrlSendOrderStripe(Context context) {
		String url_payment_stripe = context.getString(R.string.server_backend_link);
		url_payment_stripe = url_payment_stripe.replace("backend/api/", "stripe/web/")+"index.php";
		return url_payment_stripe;
	}


	public static String getUrlRegister(Context context) {
		return context.getString(R.string.server_backend_link) + "register";
	}

	public static String getUrlFeedback(Context context) {
		return context.getString(R.string.server_backend_link) + "getFeedback";
	}

	public static String getUrlUserInfo(Context context) {
		return context.getString(R.string.server_backend_link) + "getUserInfo";
	}

	public static String getUrlUpdateUser(Context context) {
		return context.getString(R.string.server_backend_link) + "getUpdateUserInfo";
	}

	public static String getUrlUpdatPass(Context context) {
		return context.getString(R.string.server_backend_link) + "getUpdatePass";
	}

	public static String getUrlRequetsShopOwner(Context context) {
		return context.getString(R.string.server_backend_link) + "requestShopOwner";
	}

	public static String getUrlLogin(Context context) {
		return context.getString(R.string.server_backend_link) + "login";
	}

	public static String getUrlSearchShop(Context context) {
		return context.getString(R.string.server_backend_link) + "getListShopBySearch";
	}

	public static String getUrlSearchFood(Context context) {
		return context.getString(R.string.server_backend_link) + "getListFoodBySearch";
	}

	public static String getUrlHistoryOrder(Context context) {
		return context.getString(R.string.server_backend_link) + "getOrderGroup";
	}

	public static String getUrlOrderDetail(Context context) {
		return context.getString(R.string.server_backend_link) + "getOrderGroupDetail";
	}

	public static String getUrlComments(Context context) {
		return context.getString(R.string.server_backend_link) + "getListComment";
	}
	public static String getListOption(Context context) {
		return context.getString(R.string.server_backend_link) + "showOption";
	}

	public static String getUrlCommentFood(Context context) {
		return context.getString(R.string.server_backend_link) + "commentFood";
	}

	public static String getUrlDefaulLocation(Context context) {
		return context.getString(R.string.server_backend_link) + "getDefaultLocation";
	}

	public static String getUrlSettings(Context context) {
		return context.getString(R.string.server_backend_link) + "getVatAndShip";
	}
	// =============================Param=======================================

	// ==============================KEY========================================
	// key
	public static final String KEY_DATA = "data";
	public static final String KEY_STATUS = "status";
	public static final String KEY_STATUS_SUCCESS = "success";
	public static final String KEY_STATUS_ERROR = "error";
	public static final String KEY_MESSAGE = "message";

	// account
	public static final String KEY_ACCOUNT_ID = "id";
	public static final String KEY_ACCOUNT_USER_NAME = "user_name";
	public static final String KEY_ACCOUNT_FULL_NAME = "full_name";
	public static final String KEY_ACCOUNT_PASSWORD = "password";
	public static final String KEY_ACCOUNT_EMAIL = "email";
	public static final String KEY_ACCOUNT_PHONE = "phone";
	public static final String KEY_ACCOUNT_ADDRESS = "address";
	public static final String KEY_ACCOUNT_ROLE = "role";
	public static final String KEY_ACCOUNT_REDIRECT_LINK = "redirect";
	public static final String KEY_ACCOUNT_REQUEST_SHOP_OWNER="alreadySendRequest";

	// offers
	public static final String KEY_OFFER_ID = "promotion_id";
	public static final String KEY_SHOP_ID = "shop_id";
	public static final String KEY_OFFER_DESCRIPTION = "promotion_description";
	public static final String KEY_OFFER_IMAGE = "promotion_thumbnail";
	public static final String KEY_OFFER_END_DATE = "promotion_end_date";
	public static final String KEY_OFFER_END_TIME = "promotion_end_time";

	// shop
	public static final String KEY_SHOP_NAME = "shop_name";
	public static final String KEY_SHOP_ADDRESS = "shop_address";
	public static final String KEY_SHOP_CITY = "shop_city";
	public static final String KEY_SHOP_IMAGE = "shop_thumbnail";
	public static final String KEY_SHOP_PHONE = "shop_tel";
	public static final String KEY_SHOP_DESCRIPTION = "shop_description";
	public static final String KEY_SHOP_LATITUDE = "shop_latitude";
	public static final String KEY_SHOP_LONGTITUDE = "shop_longitude";
	public static final String KEY_SHOP_OPEN_HOUR = "shop_openHourInDay";
	public static final String KEY_SHOP_BANNERS = "shop_banners";
	public static final String KEY_SHOP_ALL_OPEN_HOUR = "shop_Time";
	public static final String KEY_SHOP_OPEN_HOUR_DATE_ID = "date_id";
	public static final String KEY_SHOP_OPEN_HOUR_DATE_NAME = "date_name";
	public static final String KEY_SHOP_OPEN_HOUR_OPEN_AM = "open_AM";
	public static final String KEY_SHOP_OPEN_HOUR_CLOSE_AM = "close_AM";
	public static final String KEY_SHOP_OPEN_HOUR_OPEN_PM = "open_PM";
	public static final String KEY_SHOP_OPEN_HOUR_CLOSE_PM = "close_PM";
	public static final String KEY_SHOP_RATE = "rate";
	public static final String KEY_SHOP_RATE_TIMES = "rate_times";

	// categories
	public static final String KEY_CATEGORY_ID = "menu_id";
	public static final String KEY_CATEGORY_NAME = "menu_name";
	public static final String KEY_CATEGORY_DESCRIPTION = "menu_description";
	public static final String KEY_CATEGORY_IMAGE = "menu_thumbnail";

	// city
	public static final String KEY_CITY_ID = "city_id";
	public static final String KEY_CITY_NAME = "city_name";
	public static final String KEY_CITY_POST_CODE = "city_post_code";

	// banner
	public static final String KEY_BANNER_ID = "banner_id";
	public static final String KEY_BANNER_NAME = "banner_name";
	public static final String KEY_BANNER_IMAGE = "banner_image";

	// food
	public static final String KEY_FOOD_ID = "food_id";
	public static final String KEY_FOOD_CODE = "food_code";
	public static final String KEY_FOOD_NAME = "food_name";
	public static final String KEY_FOOD_PRICE = "food_price";
	public static final String KEY_FOOD_PERCENT_DISCOUNT = "food_percent_discount";
	public static final String KEY_FOOD_SHOP = "food_shop";
	public static final String KEY_FOOD_MENU = "food_menu";
	public static final String KEY_FOOD_DESCRIPTION = "food_description";
	public static final String KEY_FOOD_IMAGE = "food_thumbnail";
	public static final String KEY_FOOD_RATE = "rate";
	public static final String KEY_FOOD_RATE_COUNT = "rate_times";

	public static final String KEY_ORDER_ACCOUT_ID = "account_id";
	public static final String KEY_ORDER_ADDRESS = "address";
	public static final String KEY_ORDER_NAME = "name";
	public static final String KEY_ORDER_PHONE = "phone";
	public static final String KEY_ORDER_EMAIL = "email";
	public static final String KEY_ORDER_ITEM = "item";
	public static final String KEY_ORDER_SHOP = "shop_id";
	public static final String KEY_ORDER_FOOD = "food_id";
	public static final String KEY_ORDER_NUMBER_FOOD = "number";
	public static final String KEY_ORDER_PRICE_FOOD = "price";
	public static final String KEY_ORDER_OPTION = "option";
	public static final String KEY_ORDER_OPTION_ID = "rs_id";
	public static final String KEY_ORDER_OPTION_PRICE = "rs_price";
	public static final String KEY_NOTE="note";

	// setting
	public static final String KEY_VAT = "VAT";
	public static final String KEY_SHIP = "transportFee";
}
