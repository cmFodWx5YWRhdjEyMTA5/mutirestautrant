package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.AdapterFoodNew;
import com.hcpt.multirestaurants.adapter.ListOfferAdapter;
import com.hcpt.multirestaurants.adapter.ListOfferAdapterNew;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.fragment.BannerFragment;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Banner;
import com.hcpt.multirestaurants.object.Offer;
import com.hcpt.multirestaurants.object.OpenHour;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.util.ImageUtil;
import com.hcpt.multirestaurants.util.Utils;
import com.hcpt.multirestaurants.widget.TwoWayView;

public class ShopDetailActivity extends BaseActivity implements OnClickListener {

    private ViewPager viewPager;
    private RecyclerView lsvOffer;
    private ArrayList<Banner> arrBanner;
    private ArrayList<Offer> arrOffer;
    private ArrayList<OpenHour> arrOpenHour;
    private FragmentPagerAdapter pagerAdapter;
    private ListOfferAdapter offerAdapter;
    private ListOfferAdapterNew listOfferAdapterNew;
    private Shop shop;
    private TextView lblShopName, lblPhoneNumber;
    private TextView lblBestDeal;
    private LinearLayout btnPhoneNumber, layoutOffers;
    private ImageView btnBack;
    private int shopId = -1;
    private Dialog dialog = null;
    public static BaseActivity self;
    private RatingBar rtbRating;
    private TextView btnMenu, btnTimeable, btnMap, btnViewComments, btnReport, btnContact;
    private TextView lblDescription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_shop_detail);
        initUI();
        initControl();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (shopId != -1) {
            getShopDetailInfo(shopId);
        }
    }

    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ImageUtil.calViewRatio(ShopDetailActivity.this, viewPager, 9, 9, 0);
        btnMenu = (TextView) findViewById(R.id.btnMenu);
        btnTimeable = (TextView) findViewById(R.id.btnTimeable);
        btnMap = (TextView) findViewById(R.id.btnMap);
        lsvOffer = findViewById(R.id.lsvOffers);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        btnPhoneNumber = (LinearLayout) findViewById(R.id.btnPhone);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lblBestDeal = (TextView) findViewById(R.id.lblBestDealAround);
        lblPhoneNumber = (TextView) findViewById(R.id.lblPhoneNumber);
        btnViewComments = (TextView) findViewById(R.id.btnViewComment);
        rtbRating = (RatingBar) findViewById(R.id.rtbRating);
        layoutOffers = (LinearLayout) findViewById(R.id.layoutOffers);
        btnReport = (TextView) findViewById(R.id.btnReport);
        btnContact = (TextView) findViewById(R.id.btnContact);
        lblDescription = (TextView) findViewById(R.id.lblDescription);
    }

    private void initControl() {
//        lsvOffer.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
//                                    long arg3) {
//                Offer offer = arrOffer.get(index);
//                Bundle b = new Bundle();
//                b.putInt(GlobalValue.KEY_OFFER_ID, offer.getOfferId());
//                b.putInt(GlobalValue.KEY_SHOP_ID, offer.getShopId());
//
//                gotoActivity(self, OfferActivity.class, b);
//            }
//        });
        btnMenu.setOnClickListener(this);
        btnTimeable.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnPhoneNumber.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnViewComments.setOnClickListener(this);
        layoutOffers.setVisibility(View.GONE);
        btnReport.setOnClickListener(this);
        btnContact.setOnClickListener(this);
    }

    private void getShopDetailInfo(int shopID) {
        ModelManager.getShopById(self, shopID, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {

                        String json = (String) object;
                        Debug.e(json);
                        shop = ParserUtility.parseShop(json);
                        if (shop != null) {
                            arrBanner = shop.getArrBanner();
//                            setBanner();
                            lblShopName.setText(shop.getShopName());
                            lblPhoneNumber.setText(shop.getPhone());
                            rtbRating.setRating(Float.parseFloat(Math
                                    .floor(shop.getRateValue() / 2) + ""));
                            btnViewComments.setText(shop.getRateNumber() + " "
                                    + getResources().getString(R.string.reviews));

                            lblDescription.setText(shop.getDescription());
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void getOpenHours(int shopId) {
        ModelManager.getOpenHourByShop(self, shopId, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        arrOpenHour = ParserUtility.parseListOpenHour(json);
                        createOpenHourDialog(arrOpenHour);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

    }

    private void getListOffers(final int shopId) {
        ModelManager.getListOfferByShop(self, shopId, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        arrOffer = ParserUtility.parseListOffer(json);
                        if (arrOffer.size() > 0) {
                            layoutOffers.setVisibility(View.VISIBLE);
                        } else {
                            layoutOffers.setVisibility(View.GONE);
                        }

                        lblBestDeal.setText("Best Deals: "
                                + arrOffer.size());
                        lsvOffer.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false));
                        listOfferAdapterNew = new ListOfferAdapterNew(self, arrOffer);
                        lsvOffer.setAdapter(listOfferAdapterNew);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_SHOP_ID)) {
                shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
            }
        }
        if (shopId != -1) {
            getOpenHours(shopId);
            getListOffers(shopId);
        }

    }

//    private void setBanner() {
//        pagerAdapter = new BannerPageFragmentAdapter(
//                getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setCurrentItem(0);
//    }
//
//    class BannerPageFragmentAdapter extends FragmentPagerAdapter {
//
//        public BannerPageFragmentAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            Banner banner = arrBanner.get(position);
//            return BannerFragment.InStances(banner.getImage());
//
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return arrBanner.get(position).getName();
//        }
//
//        @Override
//        public int getCount() {
//            return arrBanner.size();
//        }
//    }

    private void gotoMenuActivity(Shop shop) {
        Bundle b = new Bundle();
        b.putDouble("VAT", shop.getShopVAT());
        b.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
        b.putString(GlobalValue.KEY_SHOP_NAME, shop.getShopName());
        b.putDouble("SHIP", shop.getDeliveryPrice());
        b.putString("URL", shop.getImage());
        b.putDouble(GlobalValue.KEY_LONG, shop.getLongitude());
        b.putDouble(GlobalValue.KEY_LAT, shop.getLatitude());
        gotoActivity(self, ListCategoryActivity.class, b);
    }

    private void gotoMapDetailActivity(Shop shop) {
        Bundle b = new Bundle();
        b.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
        gotoActivity(self, MapDetailActivity.class, b);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnMap) {
            gotoMapDetailActivity(shop);
        } else if (v == btnMenu) {
            gotoMenuActivity(shop);
        } else if (v == btnTimeable) {
            if (dialog != null) {
                dialog.show();
            }
        } else if (v == btnPhoneNumber) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + shop.getPhone()));
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Toast.makeText(ShopDetailActivity.this, "Call Phone Permission is disable! Please Enable to use this feature.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            startActivity(callIntent);
        } else if (v == btnBack) {
            onBackPressed();
            return;
        } else if (v == btnViewComments) {
            openShopsCommentPage();
            return;
        } else if (v == btnReport) {
            if (GlobalValue.myAccount == null) {
                CustomToast.showCustomAlert(this,
                        this.getString(R.string.message_no_login),
                        Toast.LENGTH_SHORT);
            } else {
                Bundle b = new Bundle();
                b.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
                b.putString(GlobalValue.KEY_SHOP_NAME, shop.getShopName());
                gotoActivity(self, FeedBackActivity.class, b);
            }
        } else if (v == btnContact) {
            showContact();
        }
    }

    private void openShopsCommentPage() {
        if (shopId != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt(GlobalValue.KEY_SHOP_ID, shopId);
            gotoActivity(self, ShopsCommentActivity.class, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        self = null;
    }

    private void showContact() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contact);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(true);
        LinearLayout lladdress = (LinearLayout) dialog.findViewById(R.id.lladdress);
        TextView address = (TextView) dialog.findViewById(R.id.address);
        address.setText(shop.getAddress());
        LinearLayout llfacebook = (LinearLayout) dialog.findViewById(R.id.llfacebook);
        TextView facebook = (TextView) dialog.findViewById(R.id.facebook);
        facebook.setText(shop.getFacebook());
        LinearLayout lltwitter = (LinearLayout) dialog.findViewById(R.id.lltwitter);
        TextView twitter = (TextView) dialog.findViewById(R.id.twitter);
        twitter.setText(shop.getTwitter());
        LinearLayout llemail = (LinearLayout) dialog.findViewById(R.id.llemail);
        TextView email = (TextView) dialog.findViewById(R.id.email);
        email.setText(shop.getEmail());
        LinearLayout llliveChat = (LinearLayout) dialog.findViewById(R.id.llliveChat);
        TextView liveChat = (TextView) dialog.findViewById(R.id.liveChat);
        liveChat.setText(shop.getLive_chat());

        lladdress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMapDetailActivity(shop);
            }
        });

        llfacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb(shop.getFacebook());
            }
        });

        lltwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb(shop.getTwitter());
            }
        });

        llemail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(shop.getEmail());
            }
        });

        llliveChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb(shop.getLive_chat());
            }
        });


        dialog.show();
    }

    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void openWeb(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }

    private void createOpenHourDialog(ArrayList<OpenHour> arrOP) {
        dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_open_time);

        // monday
        TextView monOpenAM = (TextView) dialog.findViewById(R.id.lblMonOpenAM);
        TextView monCloseAM = (TextView) dialog
                .findViewById(R.id.lblMonCloseAM);
        TextView monOpenPM = (TextView) dialog.findViewById(R.id.lblMonOpenPM);
        TextView monClosePM = (TextView) dialog
                .findViewById(R.id.lblMonClosePM);
        // tuesday
        TextView tueOpenAM = (TextView) dialog.findViewById(R.id.lblTueOpenAM);
        TextView tueCloseAM = (TextView) dialog
                .findViewById(R.id.lblTueCloseAM);
        TextView tueOpenPM = (TextView) dialog.findViewById(R.id.lblTueOpenPM);
        TextView tueClosePM = (TextView) dialog
                .findViewById(R.id.lblTueClosePM);
        // wednesday
        TextView wedOpenAM = (TextView) dialog.findViewById(R.id.lblWedOpenAM);
        TextView wedCloseAM = (TextView) dialog
                .findViewById(R.id.lblWedCloseAM);
        TextView wedOpenPM = (TextView) dialog.findViewById(R.id.lblWedOpenPM);
        TextView wedClosePM = (TextView) dialog
                .findViewById(R.id.lblWedClosePM);
        // thursday
        TextView thuOpenAM = (TextView) dialog.findViewById(R.id.lblThuOpenAM);
        TextView thuCloseAM = (TextView) dialog
                .findViewById(R.id.lblThuCloseAM);
        TextView thuOpenPM = (TextView) dialog.findViewById(R.id.lblThuOpenPM);
        TextView thuClosePM = (TextView) dialog
                .findViewById(R.id.lblThuClosePM);
        // friday
        TextView friOpenAM = (TextView) dialog.findViewById(R.id.lblFriOpenAM);
        TextView friCloseAM = (TextView) dialog
                .findViewById(R.id.lblFriCloseAM);
        TextView friOpenPM = (TextView) dialog.findViewById(R.id.lblFriOpenPM);
        TextView friClosePM = (TextView) dialog
                .findViewById(R.id.lblFriClosePM);
        // saturday
        TextView satOpenAM = (TextView) dialog.findViewById(R.id.lblSatOpenAM);
        TextView satCloseAM = (TextView) dialog
                .findViewById(R.id.lblSatCloseAM);
        TextView satOpenPM = (TextView) dialog.findViewById(R.id.lblSatOpenPM);
        TextView satClosePM = (TextView) dialog
                .findViewById(R.id.lblSatClosePM);
        // sunday
        TextView sunOpenAM = (TextView) dialog.findViewById(R.id.lblSunOpenAM);
        TextView sunCloseAM = (TextView) dialog
                .findViewById(R.id.lblSunCloseAM);
        TextView sunOpenPM = (TextView) dialog.findViewById(R.id.lblSunOpenPM);
        TextView sunClosePM = (TextView) dialog
                .findViewById(R.id.lblSunClosePM);

        // set data
        for (OpenHour openHour : arrOP) {
            String closePM = "";
            switch (openHour.getDateId()) {
                case 1:
                    sunOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    sunCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    sunOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());

                    sunClosePM.setText(Html.fromHtml(closePM));
                    break;
                case 2:
                    monOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    monCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    monOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());

                    monClosePM.setText(Html.fromHtml(closePM));
                    break;
                case 3:
                    tueOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    tueCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    tueOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());

                    tueClosePM.setText(Html.fromHtml(closePM));
                    break;
                case 4:
                    wedOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    wedCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    wedOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());

                    wedClosePM.setText(Html.fromHtml(closePM));
                    break;
                case 5:
                    thuOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    thuCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    thuOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());
                    thuClosePM.setText(Html.fromHtml(closePM));
                    break;
                case 6:
                    friOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    friCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    friOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());
                    friClosePM.setText(Html.fromHtml(closePM));
                    break;
                case 7:
                    satOpenAM.setText(Utils.getTimeFromString(openHour.getOpenAM()));
                    satCloseAM.setText(Utils.getTimeFromString(openHour.getCloseAM()));
                    satOpenPM.setText(Utils.getTimeFromString(openHour.getOpenPM()));
                    //check if close PM in next day
                    if (openHour.isCloseInNextDay())
                        closePM = "<font color=\"#0a8f08\">" + Utils.getTimeFromString(openHour.getClosePM()) + "</font>";
                    else
                        closePM = Utils.getTimeFromString(openHour.getClosePM());
                    satClosePM.setText(Html.fromHtml(closePM));
                    break;

                default:
                    break;
            }
        }

    }
}
