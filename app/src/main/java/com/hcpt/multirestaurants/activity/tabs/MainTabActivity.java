package com.hcpt.multirestaurants.activity.tabs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.DialogUtility;
import com.hcpt.multirestaurants.util.MySharedPreferences;


@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    TabHost tabHost = null;
    private Activity self;
    public static final int TAB_HOME=0;
    public static final int TAB_SEARCH=1;
    public static final int TAB_CART=2;
    public static final int TAB_ACCOUNT=3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);
        self = this;
        // restart app when catching crash issues.
        // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        // init tabhost
        initTabPages();
        // Set value for shop cart
        if (GlobalValue.arrMyMenuShop == null) {
            GlobalValue.arrMyMenuShop = new ArrayList<>();
        }
    }

    private void initTabPages() {

        tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec(GlobalValue.KEY_TAB_HOME)
                .setIndicator(createTabIndicator(R.drawable.ic_home_white))
                .setContent(new Intent(this, HomeActivity.class)));

        tabHost.addTab(tabHost
                .newTabSpec(GlobalValue.KEY_TAB_SEARCH)
                .setIndicator(
                        createTabIndicator(R.drawable.ic_search))
                .setContent(new Intent(this, SearchActivity.class)));

        tabHost.addTab(tabHost
                .newTabSpec(GlobalValue.KEY_TAB_MY_MENU)
                .setIndicator(
                        createTabIndicator(R.drawable.ic_cart))
                .setContent(new Intent(this, MainCartActivity.class)));

        tabHost.addTab(tabHost
                .newTabSpec(GlobalValue.KEY_TAB_SETTING)
                .setIndicator(createTabIndicator(R.drawable.ic_user))
                .setContent(new Intent(this, MainUserActivity.class)));

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                updateIconSelected();
                if (tabId.equals(GlobalValue.KEY_TAB_MY_MENU)) {
                    MainCartActivity activity = (MainCartActivity) getLocalActivityManager()
                            .getActivity(GlobalValue.KEY_TAB_MY_MENU);
                    activity.refreshContent();

                } else if (tabId.equals(GlobalValue.KEY_TAB_HOME)) {
                    // HomeActivity activity = (HomeActivity)
                    // getLocalActivityManager()
                    // .getActivity(GlobalValue.KEY_TAB_HOME);
                    // activity.refreshContent();
                } else if (tabId.equals(GlobalValue.KEY_TAB_SETTING)) {
                    MainUserActivity activity = (MainUserActivity) getLocalActivityManager()
                            .getActivity(GlobalValue.KEY_TAB_SETTING);
                    activity.refreshContent();
                }
            }
        });

        tabHost.setCurrentTab(TAB_HOME);
        updateIconSelected();
    }

    private void updateIconSelected() {
        TabWidget tabwidget = tabHost.getTabWidget();
        if (tabHost.getCurrentTab() == TAB_HOME) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_home_white);
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_search);
            tabwidget.getChildTabViewAt(TAB_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart);
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_user);

        } else if (tabHost.getCurrentTab() == TAB_SEARCH) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_home);
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_search_white);
            tabwidget.getChildTabViewAt(TAB_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart);
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_user);
        } else if (tabHost.getCurrentTab() == TAB_CART) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_home);
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_search);
            tabwidget.getChildTabViewAt(TAB_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart_white);
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_user);
        } else if (tabHost.getCurrentTab() == TAB_ACCOUNT) {
            tabwidget.getChildTabViewAt(TAB_HOME).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_home);
            tabwidget.getChildTabViewAt(TAB_SEARCH).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_search);
            tabwidget.getChildTabViewAt(TAB_CART).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_cart);
            tabwidget.getChildTabViewAt(TAB_ACCOUNT).findViewById(R.id.imgIcon)
                    .setBackgroundResource(R.drawable.ic_user_white);
        }
    }

    private View createTabIndicator(int resource) {
        View tabIndicator = getLayoutInflater()
                .inflate(R.layout.view_tab, null);
        ImageView image = (ImageView) tabIndicator.findViewById(R.id.imgIcon);
        image.setBackgroundResource(resource);
        return tabIndicator;
    }

    public void gotoActivity(Class<?> classs) {
        Intent intent = new Intent(this, classs);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
    }

    public void gotoActivity(Class<?> cla, Bundle bundle) {
        Intent intent = new Intent(this, cla);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        showQuitDialog();
    }

    private void showQuitDialog() {

        DialogUtility.showYesNoDialog(self, R.string.message_quit_app, R.string.yes, R.string.no, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (GlobalValue.myAccount != null) {
                    GlobalValue.myAccount = null;
                }
                new MySharedPreferences(self).setCacheUserInfo("");
                finish();
            }
        });
    }
}