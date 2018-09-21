/*
 * Name: $RCSfile: ParameterFactory.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:45:36 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.hcpt.multirestaurants.network;

import android.content.Context;

import com.hcpt.multirestaurants.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ParameterFactory class builds parameters for web service apis
 * 
 */
public final class ParameterFactory {

	private static final String PARAM_KEY_CATEGORY = "m_id";
	private static final String PARAM_KEY_CITY = "c_id";
	private static final String PARAM_KEY_SHOP = "s_id";
	private static final String PARAM_KEY_OFFER = "o_id";
	private static final String PARAM_KEY_FOOD = "f_id";
	private static final String PARAM_KEY_LATITUDE = "lat";
	private static final String PARAM_KEY_LONGITUDE = "long";
	private static final String PARAM_KEY_USER_NAME = "user";
	private static final String PARAM_KEY_PASSWORD = "pass";
	private static final String PARAM_KEY_DATA = "data";

	public static Map<String,String> createSearchShopByCityParams(
			Context context, String cityId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_CITY, cityId);

		return parameters;
	}

	public static Map<String,String> createSearchShopByCategoryParams(
			Context context, String categoryId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_CATEGORY, categoryId);

		return parameters;
	}

	public static Map<String,String> createSearchShopByCategoryAndCityParams(
			Context context, String categoryId, String cityId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_CATEGORY, categoryId);
		parameters.put(PARAM_KEY_CITY, cityId);
		return parameters;
	}

	public static Map<String,String> createShopIdParams(Context context,
			String shopId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_SHOP, shopId);

		return parameters;
	}
	public static Map<String,String> createShopIdParamsSearchParam(Context context,
														String shopId,String search_key) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_SHOP, shopId);
		parameters.put("search_key",search_key);
		return parameters;
	}

	public static Map<String,String> createSearchShopIdParams(Context context,
			String key, String city_id, String cate_id, String page) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("keyword", key);
		parameters.put("c_id", city_id);
		parameters.put("m_id", cate_id);
		parameters.put("page", page);
		return parameters;
	}

	public static Map<String,String> createSearchMenuIdParams(Context context,
			String key, String city_id, String cate_id, String page) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("keyword", key);
		parameters.put("c_id", city_id);
		parameters.put("m_id", cate_id);
		parameters.put("page", page);
		return parameters;
	}

	public static Map<String,String> getListOrderById(Context context,
			String shopId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("account", shopId);

		return parameters;
	}

	public static Map<String,String> getListOrderDetailById(Context context,
			String shopId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("o_id", shopId);

		return parameters;
	}

	public static Map<String,String> createCategoryIdParams(Context context,
			String categoryId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_CATEGORY, categoryId);

		return parameters;
	}

	public static Map<String,String> createShopIdAndCategoryIdParams(
			Context context, String shopId, String categoryId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_SHOP, shopId);
		parameters.put(PARAM_KEY_CATEGORY, categoryId);

		return parameters;
	}

	public static Map<String,String> createOfferIdParams(Context context,
			String offerId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_OFFER, offerId);

		return parameters;
	}

	public static Map<String,String> createFoodIdParams(Context context,
			String foodId) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_FOOD, foodId);

		return parameters;
	}

	public static Map<String,String> createUserIdParams(Context context,
			String id) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("id", id);

		return parameters;
	}

	public static Map<String,String> createLongLatParams(Context context,
			String longitude, String latitude) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_LONGITUDE, longitude);
		parameters.put(PARAM_KEY_LATITUDE, latitude);
		parameters.put("now", Utils.getCurrentTimestamp());

		return parameters;
	}

	public static Map<String,String> createLoginParams(Context context,
			String username, String password) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_USER_NAME, username);
		parameters.put(PARAM_KEY_PASSWORD, password);

		return parameters;
	}

	public static Map<String, String> createDataOrderParams(Context context,
															String data, int paymentMehthod, String token) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_DATA, data);
		parameters.put("paymentMethod", paymentMehthod
				+ "");
		parameters.put("transactionId", token
				+ "");

		return parameters;
	}

	public static Map<String, String> createDataOrderParamsStripe(Context context,
																  String data, int paymentMehthod, String token) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_DATA, data);
		parameters.put("paymentMethod", paymentMehthod
				+ "");
		parameters.put("token", token
				+ "");

		return parameters;
	}

	public static Map<String,String> createDataRegisterParams(Context context,
			String data) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put(PARAM_KEY_DATA, data);

		return parameters;
	}

	public static Map<String,String> putFeedBackParams(Context context,
			String id, String title, String des,String type) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("account_id", id);
		parameters.put("tittle", title);
		parameters.put("description", des);
		parameters.put("type", type);
		return parameters;
	}

	public static Map<String,String> updateInforUserParams(Context context,
			String id, String email, String fullName, String phone,
			String address) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("id", id);
		parameters.put("email", email);
		parameters.put("full_name", fullName);
		parameters.put("phone", phone);
		parameters.put("address", address);

		return parameters;
	}

	public static Map<String,String> updatePassUserParams(Context context,
			String id, String pass) {
		Map<String,String> parameters = new HashMap<>();
		parameters.put("id", id);
		parameters.put("pass", pass);

		return parameters;
	}
}