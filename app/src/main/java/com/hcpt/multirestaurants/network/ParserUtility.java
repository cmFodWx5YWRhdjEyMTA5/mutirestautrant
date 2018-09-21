package com.hcpt.multirestaurants.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.config.Constant;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.object.Account;
import com.hcpt.multirestaurants.object.Banner;
import com.hcpt.multirestaurants.object.Category;
import com.hcpt.multirestaurants.object.CategoryNew;
import com.hcpt.multirestaurants.object.City;
import com.hcpt.multirestaurants.object.Comment;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Offer;
import com.hcpt.multirestaurants.object.OpenHour;
import com.hcpt.multirestaurants.object.Order;
import com.hcpt.multirestaurants.object.OrderGroup;
import com.hcpt.multirestaurants.object.Product;
import com.hcpt.multirestaurants.object.Relish;
import com.hcpt.multirestaurants.object.RelishOption;
import com.hcpt.multirestaurants.object.RelistData;
import com.hcpt.multirestaurants.object.Setting;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.object.ShopOrder;

public class ParserUtility {

    // account
    public static Account parseAccount(String json) {
        Account account = null;
        try {
            JSONObject object = new JSONObject(json);
            if (object.getString(WebServiceConfig.KEY_STATUS).equalsIgnoreCase(
                    WebServiceConfig.KEY_STATUS_SUCCESS)) {
                JSONObject item = object
                        .getJSONObject(WebServiceConfig.KEY_DATA);
                account = new Account();
                account.setId(item.getString(WebServiceConfig.KEY_ACCOUNT_ID));
                account.setUserName(item
                        .getString(WebServiceConfig.KEY_ACCOUNT_USER_NAME));
                account.setEmail(item
                        .getString(WebServiceConfig.KEY_ACCOUNT_EMAIL));
                account.setFull_name(item
                        .getString(WebServiceConfig.KEY_ACCOUNT_FULL_NAME));
                account.setPhone(item
                        .getString(WebServiceConfig.KEY_ACCOUNT_PHONE));
                account.setAddress(item
                        .getString(WebServiceConfig.KEY_ACCOUNT_ADDRESS));
                account.setRole(item
                        .getString(WebServiceConfig.KEY_ACCOUNT_ROLE));
                account.setRedirectLink(item.getString(WebServiceConfig.KEY_ACCOUNT_REDIRECT_LINK));
                //check requestShopOwner
                if (!item.isNull(WebServiceConfig.KEY_ACCOUNT_REQUEST_SHOP_OWNER)) {
                    account.setIsRequestShopOwner(item.getInt(WebServiceConfig.KEY_ACCOUNT_REQUEST_SHOP_OWNER) == 1);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return account;
    }

    // offer
    public static ArrayList<Offer> parseListOffer(String json) {
        ArrayList<Offer> arrOffers = new ArrayList<Offer>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;

            Offer offer;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);

                offer = new Offer();
                offer.setOfferId(item.getInt(WebServiceConfig.KEY_OFFER_ID));
                offer.setShopId(item.getInt(WebServiceConfig.KEY_SHOP_ID));
                offer.setDescription(item
                        .getString(WebServiceConfig.KEY_OFFER_DESCRIPTION));
                offer.setImage(item.getString(WebServiceConfig.KEY_OFFER_IMAGE));
                offer.setEndDate(item
                        .getString(WebServiceConfig.KEY_OFFER_END_DATE));
                offer.setEndTime(item
                        .getString(WebServiceConfig.KEY_OFFER_END_TIME));

                arrOffers.add(offer);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrOffers;
    }

    public static ArrayList<OrderGroup> parseListOrderGroup(String json) {
        ArrayList<OrderGroup> arrOffers = new ArrayList<OrderGroup>();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject item;
            OrderGroup oder;

            if (!object.isNull(WebServiceConfig.KEY_DATA)) {
                JSONArray arrjson = object
                        .getJSONArray(WebServiceConfig.KEY_DATA);

                for (int i = 0; i < arrjson.length(); i++) {
                    item = arrjson.getJSONObject(i);
                    oder = new OrderGroup();
                    if (!item.isNull("code"))
                        oder.setId(item.getString("code"));
                    if (!item.isNull("total"))
                        oder.setPrice(item.getDouble("total"));
                    if (!item.isNull("time"))
                        oder.setDatetime(item.getString("time"));
                    if (!item.isNull("status"))
                        oder.setStatus(item.getString("status"));

                    arrOffers.add(oder);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrOffers;
    }

    public static ArrayList<ShopOrder> parseListShopOrder(String json) {
        ArrayList<ShopOrder> arrOffers = new ArrayList<ShopOrder>();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject item, jsonFood;
            JSONArray arrjson, arrFoodJson, arrRelishOptionJson;
            ShopOrder shopOrder = null;
            ArrayList<Menu> arrFoods;
            Menu food;

            if (!object.isNull(WebServiceConfig.KEY_DATA)) {
                arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);

                for (int i = 0; i < arrjson.length(); i++) {
                    // parse shop order
                    item = arrjson.getJSONObject(i);
                    shopOrder = new ShopOrder();
                    if (!item.isNull("order_id"))
                        shopOrder.setOrderId(item.getString("order_id"));
                    if (!item.isNull("account_id"))
                        shopOrder.setAccountId(item.getString("account_id"));
                    if (!item.isNull("shop_name"))
                        shopOrder.setShopName(item.getString("shop_name"));
                    if (!item.isNull("shop_thumbnail"))
                        shopOrder
                                .setShopImage(item.getString("shop_thumbnail"));
                    if (!item.isNull("order_places"))
                        shopOrder.setOrderAddress(item
                                .getString("order_places"));
                    if (!item.isNull("total"))
                        shopOrder.setTotalPrice(item.getDouble("total"));
                    if (!item.isNull("tax"))
                        shopOrder.setVAT(item.getDouble("tax"));
                    if (!item.isNull("shipping"))
                        shopOrder.setShipping(item.getDouble("shipping"));
                    if (!item.isNull("grandTotal"))
                        shopOrder.setGrandTotal(item.getDouble("grandTotal"));
                    if (!item.isNull("order_time"))
                        shopOrder.setOrderTime(item.getString("order_time"));
                    if (!item.isNull("orderStatus"))
                        shopOrder.setOrderStatus(item.getInt("orderStatus"));
                    if (!item.isNull("paymentStatus"))
                        shopOrder
                                .setPaymentStatus(item.getInt("paymentStatus"));
                    if (!item.isNull("paymentMethod"))
                        shopOrder
                                .setPaymentMethod(item.getInt("paymentMethod"));

                    // parse list food
                    arrFoodJson = item.getJSONArray("foods");
                    arrFoods = new ArrayList<Menu>();
                    int size = arrFoodJson.length();
                    for (int j = 0; j < size; j++) {
                        jsonFood = arrFoodJson.getJSONObject(j);
                        food = new Menu();
                        food.setId(jsonFood.getInt("food_id"));
                        food.setOrderNumber(jsonFood.getInt("number"));
                        food.setPrice(jsonFood.getDouble("price"));
                        food.setCode(jsonFood.getString("food_code"));
                        food.setName(jsonFood.getString("food_name"));
                        food.setCategoryProduct(jsonFood.getString("food_menus"));
                        food.setCategoryId(jsonFood.getInt("food_menu"));
                        food.setImage(jsonFood.getString("food_thumbnail"));
                        food.setDescription(jsonFood.getString("food_description"));
                        //parse list relish option - by HUy
                        JSONArray jsonArrayRelish = jsonFood.getJSONArray("relishes");
                        ArrayList<Relish> relishes = new ArrayList<>();
                        for (int l = 0; l < jsonArrayRelish.length(); l++) {
                            JSONObject relishJSONObject = jsonArrayRelish.getJSONObject(l);
                            Relish relish = new Relish();
                            relish.setRelishId(relishJSONObject.getString("relish_id"));
                            relish.setRelishName(relishJSONObject.getString("relish_name"));
                            relish.setRelishPrice(relishJSONObject.getDouble("relish_price"));
                            //parse relisshoption.
                            arrRelishOptionJson = relishJSONObject.getJSONArray("relish_options");
                            ArrayList<RelishOption> relishOptions = new ArrayList<>();
                            for (int k = 0; k < arrRelishOptionJson.length(); k++) {
                                JSONObject relishOptionsObject = arrRelishOptionJson.getJSONObject(k);
                                RelishOption relishOption = new RelishOption();
                                relishOption.setId(relishOptionsObject.getString("method_id"));
                                relishOption.setName(relishOptionsObject.getString("method_name"));
                                relishOptions.add(relishOption);
                            }
                            relish.setOptionArrayList(relishOptions);
                            relishes.add(relish);
                        }
                        food.setRelishArrayList(relishes);
                        arrFoods.add(food);
                    }
                    shopOrder.setArrFoods(arrFoods);
                    arrOffers.add(shopOrder);
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("huy-log", "list-shop-order : " + e.getMessage());
            e.printStackTrace();

        }

        return arrOffers;
    }

    public static ArrayList<Order> parseListOrder(String json) {
        ArrayList<Order> arrOffers = new ArrayList<Order>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;
            Order oder;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);
                oder = new Order();
                oder.setPlacesO(item.getString("order_places"));
                oder.setStatusO(item.getString("orderStatus"));
                oder.setO_id(item.getString("order_id"));
                oder.setSttO(item.getString("count"));
                oder.setTimeO(item.getString("created"));
                oder.setTotalO(item.getString("total"));
                arrOffers.add(oder);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrOffers;
    }

    public static ArrayList<Order> parseListDetailOrder(String json) {
        ArrayList<Order> arrOffers = new ArrayList<Order>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;

            Order oder;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);

                oder = new Order();
                oder.setName(item.getString("food_name"));
                oder.setImage(item.getString("food_thumbnail"));
                oder.setNumber(item.getString("number"));
                oder.setPrice(item.getString("price"));
                oder.setPromotion(item.getString("promotion"));

                arrOffers.add(oder);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrOffers;
    }

    public static Offer parseOffer(String json) {
        Offer offer = new Offer();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject item = object.getJSONObject(WebServiceConfig.KEY_DATA);

            offer.setOfferId(item.getInt(WebServiceConfig.KEY_OFFER_ID));
            offer.setShopId(item.getInt(WebServiceConfig.KEY_SHOP_ID));
            offer.setDescription(item
                    .getString(WebServiceConfig.KEY_OFFER_DESCRIPTION));
            offer.setImage(item.getString(WebServiceConfig.KEY_OFFER_IMAGE));
            offer.setEndDate(item
                    .getString(WebServiceConfig.KEY_OFFER_END_DATE));
            offer.setEndTime(item
                    .getString(WebServiceConfig.KEY_OFFER_END_TIME));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return offer;
    }

    // shop
    public static ArrayList<Shop> getListShop(String json) {
        ArrayList<Shop> arrShop = new ArrayList<Shop>();
        try {
            JSONObject object = new JSONObject(json);
            Shop shop;
            JSONObject item;
            JSONArray arr = object.getJSONArray(WebServiceConfig.KEY_DATA);
            if (arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    item = arr.getJSONObject(i);
                    shop = new Shop();
                    shop.setShopId(item.getInt(WebServiceConfig.KEY_SHOP_ID));
                    shop.setShopName(item
                            .getString(WebServiceConfig.KEY_SHOP_NAME));
                    shop.setAddress(item
                            .getString(WebServiceConfig.KEY_SHOP_ADDRESS));
                    shop.setPhone(item
                            .getString(WebServiceConfig.KEY_SHOP_PHONE));
                    shop.setImage(item
                            .getString(WebServiceConfig.KEY_SHOP_IMAGE));
                    shop.setDescription(item
                            .getString(WebServiceConfig.KEY_SHOP_DESCRIPTION));
                    shop.setLatitude(item
                            .getDouble(WebServiceConfig.KEY_SHOP_LATITUDE));
                    shop.setLongitude(item
                            .getDouble(WebServiceConfig.KEY_SHOP_LONGTITUDE));
                    shop.setCityId(item.getInt(WebServiceConfig.KEY_SHOP_CITY));
                    shop.setIsVerified(item.getString("isVerified"));
                    shop.setIsFeatured(item.getString("isFeatured"));
                    shop.setFacebook(item.getString("facebook"));
                    shop.setTwitter(item.getString("twitter"));
                    shop.setEmail(item.getString("email"));
                    shop.setLive_chat(item.getString("live_chat"));

                    try {
                        shop.setRateNumber(Integer.parseInt(item
                                .getString(WebServiceConfig.KEY_SHOP_RATE_TIMES)));
                    } catch (Exception ex) {
                        shop.setRateNumber(0);
                    }
                    try {
                        shop.setRateValue(Float.parseFloat(item
                                .getString(WebServiceConfig.KEY_SHOP_RATE)));
                    } catch (Exception ex) {
                        shop.setRateValue(0);
                    }

//                    JSONObject arrOpenhour = item
//                            .getJSONObject(WebServiceConfig.KEY_SHOP_OPEN_HOUR);
//                    OpenHour openhour = new OpenHour();
//                    openhour.setOpenAM(arrOpenhour
//                            .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_OPEN_AM));
//                    openhour.setOpenPM(arrOpenhour
//                            .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_OPEN_PM));
//                    openhour.setCloseAM(arrOpenhour
//                            .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_CLOSE_AM));
//                    openhour.setClosePM(arrOpenhour
//                            .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_CLOSE_PM));
//                    openhour.setShopId(shop.getShopId());
//
//                    shop.setOpenHour(openhour);

                    //is open or close
                    if (!item.isNull("is_open"))
                        shop.setIsOpen(item.getString("is_open").equals("1"));

                    // get VAT
                    if (!item.isNull("shop_vat"))
                        shop.setShopVAT(item.getDouble("shop_vat"));

                    // get delivery cost
                    if (!item.isNull("shop_transport_fee")) {

                        JSONObject jsonShip = item
                                .getJSONObject("shop_transport_fee");
                        shop.setMinPriceForDelivery(jsonShip
                                .getDouble("minimum"));
                        shop.setDeliveryPrice(jsonShip
                                .getDouble("shipping_fee"));
                    }

                    arrShop.add(shop);

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return arrShop;
    }

    public static Shop parseShop(String json) {
        JSONObject object;
        Shop shop = null;
        try {
            object = new JSONObject(json);
            JSONObject item = object.getJSONObject(WebServiceConfig.KEY_DATA);
            shop = new Shop();
            shop.setShopId(item.getInt(WebServiceConfig.KEY_SHOP_ID));
            shop.setShopName(item.getString(WebServiceConfig.KEY_SHOP_NAME));
            shop.setAddress(item.getString(WebServiceConfig.KEY_SHOP_ADDRESS));
            shop.setPhone(item.getString(WebServiceConfig.KEY_SHOP_PHONE));
            shop.setImage(item.getString(WebServiceConfig.KEY_SHOP_IMAGE));
            shop.setDescription(item
                    .getString(WebServiceConfig.KEY_SHOP_DESCRIPTION));
            shop.setLatitude(item.getDouble(WebServiceConfig.KEY_SHOP_LATITUDE));
            shop.setLongitude(item
                    .getDouble(WebServiceConfig.KEY_SHOP_LONGTITUDE));
            shop.setCityId(item.getInt(WebServiceConfig.KEY_SHOP_CITY));
            shop.setFacebook(item.getString("facebook"));
            shop.setTwitter(item.getString("twitter"));
            shop.setEmail(item.getString("email"));
            shop.setLive_chat(item.getString("live_chat"));
            try {
                shop.setRateNumber(item
                        .getInt(WebServiceConfig.KEY_SHOP_RATE_TIMES));
            } catch (Exception ex) {
                shop.setRateNumber(0);
            }
            try {
                shop.setRateValue(item.getInt(WebServiceConfig.KEY_SHOP_RATE));
            } catch (Exception ex) {
                shop.setRateValue(0);
            }

            // get all banner
            JSONArray bannerJS = item
                    .getJSONArray(WebServiceConfig.KEY_SHOP_BANNERS);
            ArrayList<Banner> arrBanner = new ArrayList<Banner>();
            Banner banner;
            JSONObject itemBannerJs;
            for (int i = 0; i < bannerJS.length(); i++) {
                itemBannerJs = bannerJS.getJSONObject(i);
                banner = new Banner();
                banner.setId(itemBannerJs
                        .getInt(WebServiceConfig.KEY_BANNER_ID));
                banner.setName(itemBannerJs
                        .getString(WebServiceConfig.KEY_BANNER_NAME));
                banner.setImage(itemBannerJs
                        .getString(WebServiceConfig.KEY_BANNER_IMAGE));

                banner.setShopId(shop.getShopId());

                arrBanner.add(banner);
            }

            shop.setArrBanner(arrBanner);

            //is open or close
            if (!item.isNull("is_open"))
                shop.setIsOpen(item.getString("is_open").equals("1"));

            // get VAT
            if (!item.isNull("shop_vat"))
                shop.setShopVAT(item.getDouble("shop_vat"));

            // get delivery cost
            if (!item.isNull("shop_transport_fee")) {

                JSONObject jsonShip = item.getJSONObject("shop_transport_fee");
                shop.setMinPriceForDelivery(jsonShip.getDouble("minimum"));
                shop.setDeliveryPrice(jsonShip.getDouble("shipping_fee"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return shop;
    }

    // open hour

    public static ArrayList<OpenHour> parseListOpenHour(String json) {
        // get all openhour
        JSONObject item;
        ArrayList<OpenHour> arrOpenHour = new ArrayList<OpenHour>();
        try {
            item = new JSONObject(json);
            JSONArray openhourJS = item.getJSONArray(WebServiceConfig.KEY_DATA);
            OpenHour openhour;
            JSONObject itemOpenhourJs;
            for (int i = 0; i < openhourJS.length(); i++) {
                itemOpenhourJs = openhourJS.getJSONObject(i);
                openhour = new OpenHour();
                openhour.setDateId(itemOpenhourJs
                        .getInt(WebServiceConfig.KEY_SHOP_OPEN_HOUR_DATE_ID));
                openhour.setDateName(itemOpenhourJs
                        .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_DATE_NAME));
                openhour.setOpenAM(itemOpenhourJs
                        .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_OPEN_AM));
                openhour.setOpenPM(itemOpenhourJs
                        .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_OPEN_PM));
                openhour.setCloseAM(itemOpenhourJs
                        .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_CLOSE_AM));
                openhour.setClosePM(itemOpenhourJs
                        .getString(WebServiceConfig.KEY_SHOP_OPEN_HOUR_CLOSE_PM));

                arrOpenHour.add(openhour);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return arrOpenHour;
    }

    // city

    public static ArrayList<City> parseListCity(String json) {
        ArrayList<City> arrCities = new ArrayList<City>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;
            City city;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);
                city = new City();
                city.setId(item.getInt(WebServiceConfig.KEY_CITY_ID));
                city.setPostCode(item
                        .getString(WebServiceConfig.KEY_CITY_POST_CODE));
                city.setName(item.getString(WebServiceConfig.KEY_CITY_NAME));

                arrCities.add(city);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrCities;
    }

    // categories

    public static ArrayList<Category> parseListCategories(String json) {
        ArrayList<Category> arrCategories = new ArrayList<Category>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;
            Category category;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);
//                JSONArray arrProduct = item.getJSONArray("listProduct");
                category = new Category();
                category.setId(item.getInt(WebServiceConfig.KEY_CATEGORY_ID));
                category.setName(item
                        .getString(WebServiceConfig.KEY_CATEGORY_NAME));
                category.setDescription(item
                        .getString(WebServiceConfig.KEY_CATEGORY_DESCRIPTION));
                category.setImage(item
                        .getString(WebServiceConfig.KEY_CATEGORY_IMAGE));
                arrCategories.add(category);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrCategories;
    }

    public static Category parseCategory(String json) {
        Category category = new Category();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject item = object.getJSONObject(WebServiceConfig.KEY_DATA);

            category.setId(item.getInt(WebServiceConfig.KEY_CATEGORY_ID));
            category.setName(item.getString(WebServiceConfig.KEY_CATEGORY_NAME));
            category.setDescription(item
                    .getString(WebServiceConfig.KEY_CATEGORY_DESCRIPTION));
            category.setImage(item
                    .getString(WebServiceConfig.KEY_CATEGORY_IMAGE));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return category;
    }

    public static ArrayList<Menu> parseListFood(String json) {
        ArrayList<Menu> arrFood = new ArrayList<Menu>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;
            Menu food;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);
                food = new Menu();
                food.setId(item.getInt(WebServiceConfig.KEY_FOOD_ID));
                food.setCode(item.getString(WebServiceConfig.KEY_FOOD_CODE));
                food.setName(item.getString(WebServiceConfig.KEY_FOOD_NAME));
                food.setDescription(item
                        .getString(WebServiceConfig.KEY_FOOD_DESCRIPTION));
                food.setImage(item.getString(WebServiceConfig.KEY_FOOD_IMAGE));
                food.setPrice(item.getDouble(WebServiceConfig.KEY_FOOD_PRICE));
                food.setPercentDiscount(item
                        .getDouble(WebServiceConfig.KEY_FOOD_PERCENT_DISCOUNT));
                food.setShopId(item.getInt(WebServiceConfig.KEY_FOOD_SHOP));
                food.setCategoryId(item.getInt(WebServiceConfig.KEY_FOOD_MENU));
                food.setCategoryProduct(item.getString("food_menus"));
                try {
                    food.setRateValue(Float.parseFloat(item
                            .getString(WebServiceConfig.KEY_FOOD_RATE)));
                } catch (Exception ex) {
                    food.setRateValue(0);
                }
                try {
                    food.setRateNumber(Integer.parseInt(item
                            .getString(WebServiceConfig.KEY_FOOD_RATE_COUNT)));
                } catch (Exception ex) {
                    food.setRateNumber(0);
                }

                arrFood.add(food);
            }
        } catch (JSONException e) {
            Debug.e(e.getMessage());
            // TODO Auto-generated catch block
            Log.e("error", "Parsed list food false");
        }

        return arrFood;
    }

    public static ArrayList<Menu> parseListFoodInSearch(String json) {
        ArrayList<Menu> arrFood = new ArrayList<Menu>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrjson = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONObject item;
            Menu food;
            for (int i = 0; i < arrjson.length(); i++) {
                item = arrjson.getJSONObject(i);
                food = new Menu();
                food.setId(item.getInt(WebServiceConfig.KEY_FOOD_ID));
                food.setCode(item.getString(WebServiceConfig.KEY_FOOD_CODE));
                food.setName(item.getString(WebServiceConfig.KEY_FOOD_NAME));
                food.setDescription(item
                        .getString(WebServiceConfig.KEY_FOOD_DESCRIPTION));
                food.setImage(item.getString(WebServiceConfig.KEY_FOOD_IMAGE));
                food.setPrice(item.getDouble(WebServiceConfig.KEY_FOOD_PRICE));
                food.setPercentDiscount(item
                        .getDouble(WebServiceConfig.KEY_FOOD_PERCENT_DISCOUNT));
                food.setShopId(item.getInt(WebServiceConfig.KEY_FOOD_SHOP));
                food.setCategoryId(item.getInt(WebServiceConfig.KEY_FOOD_MENU));
                food.setCategory(item.getString("category"));
                food.setShop_address(item.getString("shop_address"));
                food.setShop_phone(item.getString("shop_phone"));
                food.setCategoryProduct(item.getString("food_menus"));
                try {
                    food.setRateValue(Float.parseFloat(item
                            .getString(WebServiceConfig.KEY_FOOD_RATE)));
                } catch (Exception ex) {
                    food.setRateValue(0);
                }
                try {
                    food.setRateNumber(Integer.parseInt(item
                            .getString(WebServiceConfig.KEY_FOOD_RATE_COUNT)));
                } catch (Exception ex) {
                    food.setRateNumber(0);
                }

                arrFood.add(food);
            }
        } catch (JSONException e) {
            Debug.e(e.getMessage());
            // TODO Auto-generated catch block
            Log.e("error", "Parsed list food false");
        }

        return arrFood;
    }

    public static Menu parseFood(String json) {
        Menu food = null;
        try {
            JSONObject object = new JSONObject(json);
            JSONObject item = object.getJSONObject(WebServiceConfig.KEY_DATA);
            JSONObject item1;
            food = new Menu();
            food.setId(item.getInt(WebServiceConfig.KEY_FOOD_ID));
            food.setCode(item.getString(WebServiceConfig.KEY_FOOD_CODE));
            food.setName(item.getString(WebServiceConfig.KEY_FOOD_NAME));
            food.setDescription(item
                    .getString(WebServiceConfig.KEY_FOOD_DESCRIPTION));
            food.setImage(item.getString(WebServiceConfig.KEY_FOOD_IMAGE));
            food.setPercentDiscount(item
                    .getDouble(WebServiceConfig.KEY_FOOD_PERCENT_DISCOUNT));
            food.setPrice(item.getDouble(WebServiceConfig.KEY_FOOD_PRICE));
            food.setShopId(item.getInt(WebServiceConfig.KEY_FOOD_SHOP));
            food.setCategoryId(item.getInt(WebServiceConfig.KEY_FOOD_MENU));
            food.setCategoryProduct(item.getString("food_menus"));

            try {
                food.setRateValue(Float.parseFloat(item
                        .getString(WebServiceConfig.KEY_FOOD_RATE)));
            } catch (Exception ex) {
                food.setRateValue(0);
            }
            try {
                food.setRateNumber(Integer.parseInt(item
                        .getString(WebServiceConfig.KEY_FOOD_RATE_COUNT)));
            } catch (Exception ex) {
                food.setRateNumber(0);
            }
            JSONArray jsonArrayRelish = item.getJSONArray("relishes");
            ArrayList<Relish> relishes = new ArrayList<>();
            for (int j = 0; j < jsonArrayRelish.length(); j++) {
                item1 = jsonArrayRelish.getJSONObject(j);
                Relish relish = new Relish();
                relish.setRelishId(item1.getString("relish_id"));
                relish.setRelishName(item1.getString("relish_name"));
                relish.setRelishPrice(item1.getDouble("relish_price"));

                JSONArray arrRelishOptionJson = item1.getJSONArray("relish_options");
                ArrayList<RelishOption> relishOptions = new ArrayList<>();
                RelishOption relishHard = new RelishOption();
                relishHard.setId("0");
                relishHard.setName(Constant.NONE);
                relishOptions.add(relishHard);
                for (int k = 0; k < arrRelishOptionJson.length(); k++) {
                    RelishOption relishOption = new RelishOption();
                    JSONObject relishOptionsObject = arrRelishOptionJson.getJSONObject(k);
                    relishOption.setId(relishOptionsObject.getString("method_id"));
                    relishOption.setName(relishOptionsObject.getString("method_name"));
                    relishOptions.add(relishOption);
                }
                relish.setOptionArrayList(relishOptions);
                relishes.add(relish);
            }
            food.setRelishArrayList(relishes);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return food;
    }

    public static Setting parseSetting(String json) {
        Setting setting = new Setting();

        try {

            JSONObject item;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray itemService = jsonObject
                    .getJSONArray(WebServiceConfig.KEY_DATA);
            for (int i = 0; i < itemService.length(); i++) {
                item = itemService.getJSONObject(i);
                setting.setVat(item.getString(WebServiceConfig.KEY_VAT));
                setting.setTransportFee(item
                        .getString(WebServiceConfig.KEY_SHIP));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return setting;

    }

    public static ArrayList<Comment> parseComments(String json) {
        ArrayList<Comment> arrComment = new ArrayList<Comment>();

        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getString(WebServiceConfig.KEY_STATUS)
                        .equalsIgnoreCase(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                    JSONArray jsonArr = jsonObject
                            .getJSONArray(WebServiceConfig.KEY_DATA);
                    if (jsonArr != null && jsonArr.length() > 0) {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject obj = jsonArr.getJSONObject(i);

                            Comment cmt = new Comment();
                            cmt.setContent(obj.getString("content"));
                            cmt.setCreatedDate(obj.getString("created"));
                            cmt.setRateValue(Float.parseFloat(obj
                                    .getString("rate")));
                            cmt.setUserID(obj.getString("account_id"));

                            arrComment.add(cmt);
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return arrComment;
    }

    public static String convertAccountToJsonString(Account acc) {
        String str = "";
        if (acc.getId().isEmpty()) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", acc.getId());
                json.put("userName", acc.getUserName());
                json.put("fullName", acc.getFull_name());
                json.put("email", acc.getEmail());
                json.put("address", acc.getAddress());
                json.put("phone", acc.getPhone());
                json.put("pass", acc.getPassword());
                json.put("role", acc.getRole());
                json.put("redirectLink", acc.getRedirectLink());
                json.put("loginType", acc.getType());
                str = json.toString();

            } catch (JSONException e) {
                str = "";
            }
        }
        return str;
    }

    public static Account convertJsonStringtoAccount(String json) {
        Account account = null;
        if (json.isEmpty()) {
            try {
                account = new Account();
                JSONObject jsonObj = new JSONObject(json);
                account.setId(jsonObj.getString("id"));
                account.setUserName(jsonObj.getString("userName"));
                account.setFull_name(jsonObj.getString("fullName"));
                account.setEmail(jsonObj.getString("email"));
                account.setAddress(jsonObj.getString("address"));
                account.setPhone(jsonObj.getString("phone"));
                account.setPassword(jsonObj.getString("pass"));
                account.setRole(jsonObj.getString("role"));
                account.setRedirectLink(jsonObj.getString("redirectLink"));
                account.setType(jsonObj.getString("loginType"));

            } catch (JSONException e) {
                account = null;
            }
        }
        return account;
    }

    public static LatLng parseDefaultLocation(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return new LatLng(object.getJSONObject("data").getDouble("lat"), object.getJSONObject("data").getDouble("lon"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new LatLng(0.0000000, 0.0000000);
        }
    }

    public static boolean isSuccess(String json) {
        try {
            JSONObject object = new JSONObject(json);
            if (object.getString(WebServiceConfig.KEY_STATUS).equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getAllPageCount(String json) {
        int pageCount = 0;
        try {
            JSONObject object = new JSONObject(json);
            if (!object.isNull("allpage")) {
                pageCount = object.getInt("allpage");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pageCount;
    }

    public static CategoryNew parseListCategoriesNew(String json) {
        CategoryNew categoryNew = new CategoryNew();
        try {
            ArrayList<Category> categoryArrayList = new ArrayList<>();
            ArrayList<Menu> menuArrayList = new ArrayList<>();
            JSONObject object = new JSONObject(json);
            JSONArray arrjsonCategory = object.getJSONArray(WebServiceConfig.KEY_DATA);
            JSONArray jsonArrayProduct = object.getJSONArray("listProduct");
            categoryNew.setStatus(object.getString("status"));
            JSONObject item;
            JSONObject item1;
            Category category;
            Menu food;
            for (int i = 0; i < arrjsonCategory.length(); i++) {
                item = arrjsonCategory.getJSONObject(i);
                category = new Category();
                category.setId(item.getInt(WebServiceConfig.KEY_CATEGORY_ID));
                category.setName(item
                        .getString(WebServiceConfig.KEY_CATEGORY_NAME));
                category.setDescription(item
                        .getString(WebServiceConfig.KEY_CATEGORY_DESCRIPTION));
                category.setImage(item
                        .getString(WebServiceConfig.KEY_CATEGORY_IMAGE));
                categoryArrayList.add(category);
            }
            for (int j = 0; j < jsonArrayProduct.length(); j++) {
                item1 = jsonArrayProduct.getJSONObject(j);
                food = new Menu();
                food.setId(item1.getInt(WebServiceConfig.KEY_FOOD_ID));
                food.setCode(item1.getString(WebServiceConfig.KEY_FOOD_CODE));
                food.setName(item1.getString(WebServiceConfig.KEY_FOOD_NAME));
                food.setDescription(item1
                        .getString(WebServiceConfig.KEY_FOOD_DESCRIPTION));
                food.setImage(item1.getString(WebServiceConfig.KEY_FOOD_IMAGE));
                food.setPercentDiscount(item1
                        .getDouble(WebServiceConfig.KEY_FOOD_PERCENT_DISCOUNT));
                food.setPrice(item1.getDouble(WebServiceConfig.KEY_FOOD_PRICE));
                food.setShopId(item1.getInt(WebServiceConfig.KEY_FOOD_SHOP));
                food.setCategoryId(item1.getInt(WebServiceConfig.KEY_FOOD_MENU));
                food.setCategoryProduct(item1.getString("food_menus"));
                JSONArray jsonArrayRelish = item1.getJSONArray("relishes");
                ArrayList<Relish> relishes = new ArrayList<>();
                for (int l = 0; l < jsonArrayRelish.length(); l++) {
                    JSONObject relishJSONObject = jsonArrayRelish.getJSONObject(l);
                    Relish relish = new Relish();
                    relish.setRelishId(relishJSONObject.getString("relish_id"));
                    relish.setRelishName(relishJSONObject.getString("relish_name"));
                    relish.setRelishPrice(relishJSONObject.getDouble("relish_price"));

                    //
                    JSONArray arrRelishOptionJson = relishJSONObject.getJSONArray("relish_options");
                    ArrayList<RelishOption> relishOptions = new ArrayList<>();
                    RelishOption relishHard = new RelishOption();
                    relishHard.setId("0");
                    relishHard.setName(Constant.NONE);
                    relishOptions.add(relishHard);
                    for (int k = 0; k < arrRelishOptionJson.length(); k++) {
                        RelishOption relishOption = new RelishOption();
                        JSONObject relishOptionsObject = arrRelishOptionJson.getJSONObject(k);
                        relishOption.setId(relishOptionsObject.getString("method_id"));
                        relishOption.setName(relishOptionsObject.getString("method_name"));
                        relishOptions.add(relishOption);
                    }
                    relish.setOptionArrayList(relishOptions);
                    relishes.add(relish);
                }
                food.setRelishArrayList(relishes);
                try {
                    food.setRateValue(Float.parseFloat(item1
                            .getString(WebServiceConfig.KEY_FOOD_RATE)));
                } catch (Exception ex) {
                    food.setRateValue(0);
                }
                try {
                    food.setRateNumber(Integer.parseInt(item1
                            .getString(WebServiceConfig.KEY_FOOD_RATE_COUNT)));
                } catch (Exception ex) {
                    food.setRateNumber(0);
                }
                menuArrayList.add(food);
            }
            categoryNew.setCategoryArrayList(categoryArrayList);
            categoryNew.setMenuArrayList(menuArrayList);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return categoryNew;
    }
}
