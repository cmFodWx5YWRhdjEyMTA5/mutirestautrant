package com.hcpt.multirestaurants.modelmanager;

import android.content.Context;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.network.HttpError;
import com.hcpt.multirestaurants.network.HttpGet;
import com.hcpt.multirestaurants.network.HttpListener;
import com.hcpt.multirestaurants.network.HttpPost;
import com.hcpt.multirestaurants.network.HttpRequest;
import com.hcpt.multirestaurants.network.ParameterFactory;
import com.hcpt.multirestaurants.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private static String TAG = "ModelManager";

    public static void getListShop(final Context context, double longitude,
                                   double latitude, boolean isProgress,
                                   final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlAllShop(context);
        String mlong = longitude + "";
        String mlat = latitude + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createLongLatParams(context, mlong, mlat);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListShopBySearch(final Context context,
                                           String keyword, String categoryId, String cityId, int page,
                                           String open, String distance, String sortBy, String sortType,
                                           String lat, String lon, boolean isProgress,
                                           final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlSearchShop(context);
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("c_id", cityId);
        params.put("m_id", categoryId);
        params.put("page", page + "");
        params.put("open", open);
        params.put("distance", distance);
        params.put("sort_name", sortBy);
        params.put("sort_type", sortType);
        params.put("lat", lat);
        params.put("long", lon);
        params.put("now", Utils.getCurrentTimestamp());

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodBySearch(final Context context,
                                           String keyword, String categoryId, String cityId, int page,
                                           String open, String distance, String sortBy, String sortType,
                                           String lat, String lon, boolean isProgress,
                                           final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlSearchFood(context);

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("c_id", cityId);
        params.put("m_id", categoryId);
        params.put("page", page + "");
        params.put("open", open);
        params.put("distance", distance);
        params.put("sort_name", sortBy);
        params.put("sort_type", sortType);
        params.put("lat", lat);
        params.put("long", lon);
        params.put("now", Utils.getCurrentTimestamp());

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getSearchShopById(final Context context, String key,
                                         String city_id, String cate_id, String page, boolean isProgress,
                                         final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlShop(context);

        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createSearchShopIdParams(context, key, city_id, cate_id, page);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getSearchMenuById(final Context context, String key,
                                         String city_id, String cate_id, String page, boolean isProgress,
                                         final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlMenu(context);

        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createSearchMenuIdParams(context, key, city_id, cate_id, page);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getSetting(final Context context, boolean isProgress,
                                  final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlSettings(context);
        Map<String, String> params = new HashMap<>();

        new HttpPost(context, url, params, HttpPost.REQUEST_STRING_PARAMS, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListOffersOfDay(final Context context,
                                          boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOfferOfDay(context);
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListOrder(final Context context, String id, int page,
                                    boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlHistoryOrder(context);
        Map<String, String> params = new HashMap<>();
        params.put("account", id);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListDetailOrder(final Context context, String id,
                                          boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOrderDetail(context);
        Map<String, String> params = new HashMap<>();
        params.put("group_code", id);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListCity(final Context context, boolean isProgress,
                                   final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlAllCity(context);
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListCategory(final Context context,
                                       boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlAllCategory(context);
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getShopById(final Context context, int shopId,
                                   boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlShopById(context);
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getCatgoryById(final Context context, int categoryId,
                                      boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlCategoryByID(context);
        String shop = categoryId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createCategoryIdParams(context, shop);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOpenHourByShop(final Context context, int shopId,
                                         boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOpenHourByShop(context);
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListShopByCity(final Context context, int cityId,
                                         boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlShopByCity(context);
        String city = cityId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createSearchShopByCityParams(context, city);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListShopByCateGory(final Context context,
                                             int categoryId, boolean isProgress,
                                             final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlShopByCategory(context);
        String category = categoryId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createSearchShopByCategoryParams(context, category);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListShopByCateGoryAndCity(final Context context,
                                                    int categoryId, int cityId, boolean isProgress,
                                                    final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlShopbyCityAndCategory(context);
        String category = categoryId + "";
        String city = cityId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createSearchShopByCategoryAndCityParams(context, category,
                        city);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListOfferByShop(final Context context, int shopId,
                                          boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOfferByShop(context);
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListCategoryByShop(final Context context, int shopId,
                                             boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlCategoryByShop(context);
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParams(context, shop);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOfferById(final Context context, int offerId,
                                    boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOfferById(context);
        String offer = offerId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createOfferIdParams(context, offer);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodByShopAndCategory(final Context context,
                                                    int shopId, int categoryId, boolean isProgress,
                                                    final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlFoodByShopAnMenu(context);
        String shop = shopId + "";
        String category = categoryId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdAndCategoryIdParams(context, shop, category);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodOfDay(final Context context,
                                        double longitude, double latitude, boolean isProgress,
                                        final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlListFoodOfDay(context);
        String mlong = longitude + "";
        String mlat = latitude + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createLongLatParams(context, mlong, mlat);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getListFoodByPromotion(final Context context, int offer,
                                              boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlListFoodByPromotion(context);
        String mpromotion = offer + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createOfferIdParams(context, mpromotion);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getFoodById(final Context context, int foodId,
                                   boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlGetFoodById(context);
        String food = foodId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createFoodIdParams(context, food);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getUserInforById(final Context context, String id,
                                        boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlUserInfo(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createUserIdParams(context, id);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void login(final Context context, String username,
                             String pass, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlLogin(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createLoginParams(context, username, pass);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void register(final Context context, String data,
                                boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlRegister(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createDataRegisterParams(context, data);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void putFeedBack(final Context context, String id,
                                   String title, String des, String type, boolean isProgress,
                                   final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlFeedback(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .putFeedBackParams(context, id, title, des, type);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updateInforUser(final Context context, String id,
                                       String email, String fullName, String phone, String address,
                                       boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlUpdateUser(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .updateInforUserParams(context, id, email, fullName, phone,
                        address);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void registerShopOwner(final Context context, String id,
                                         boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlRequetsShopOwner(context);
        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void updatePassUser(final Context context, String id,
                                      String pass, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlUpdatPass(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .updatePassUserParams(context, id, pass);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void sendListOrderStripe(final Context context, String data,
                                           int paymentMethod, boolean isProgress, String token,
                                           final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlSendOrderStripe(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createDataOrderParamsStripe(context, data, paymentMethod, token);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void sendListOrder(final Context context, String data,
                                     int paymentMethod, boolean isProgress, String token,
                                     final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlSendOrder(context);
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createDataOrderParams(context, data, paymentMethod, token);
        new HttpGet(context, url, params,isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOrderHistory(final Context context, String accountId,
                                       boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOrderDetail(context);
        Map<String, String> params = new HashMap<>();
        params.put("account", accountId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getOrderDetails(final Context context,
                                       String orderGroupId, boolean isProgress,
                                       final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlOrderDetail(context);
        Map<String, String> params = new HashMap<>();
        params.put("group_code", orderGroupId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }


    public static void getFoodsComments(final Context context, String id,
                                        int page, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlComments(context);

        Map<String, String> params = new HashMap<>();
        params.put("objectType", "food");
        params.put("objectId", id);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getShopsComments(final Context context, String id,
                                        int page, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlComments(context);

        Map<String, String> params = new HashMap<>();
        params.put("objectType", "shop");
        params.put("objectId", id);
        params.put("page", page + "");
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void addFoodReview(final Context context, String shopId,
                                     String foodId, String rate, String userId, String content, boolean isProgress,
                                     final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlCommentFood(context);

        Map<String, String> params = new HashMap<>();
        params.put("s_id", shopId);
        params.put("food_id", foodId);
        params.put("rate", rate);
        params.put("account_id", userId);
        params.put("title", "dummy");
        params.put("content", content);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }

    public static void getDefaultLocation(final Context context, boolean isProgress,
                                          final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlDefaulLocation(context);

        Map<String, String> params = new HashMap<>();

        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }
    public static void getSearchFoodListCategoryByShop(final Context context, int shopId,String search_key,
                                             boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getUrlCategoryByShop(context);
        String shop = shopId + "";
        Map<String, String> params = (Map<String, String>) ParameterFactory
                .createShopIdParamsSearchParam(context, shop,search_key);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }
    public static void getListOption(final Context context, String menuId, boolean isProgress, final ModelManagerListener listener) {
        final String url = WebServiceConfig.getListOption(context);
        Map<String, String> params = new HashMap<>();
        params.put("menuId", menuId);
        new HttpGet(context, url, params, isProgress, new HttpListener() {
            @Override
            public void onHttpRespones(Object respones) {
                if (respones != null) {
                    listener.onSuccess(respones.toString());
                } else {
                    listener.onError(null);
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
    }
}