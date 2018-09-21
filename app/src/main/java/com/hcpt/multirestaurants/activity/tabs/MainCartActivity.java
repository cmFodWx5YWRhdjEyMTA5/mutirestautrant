package com.hcpt.multirestaurants.activity.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.cart.ShopCartFragment;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.util.MySharedPreferences;

public class MainCartActivity extends FragmentActivity {

	public static final int PAGE_SHOP_CART = 0;
	public static final int PAGE_CONFIRM = 1;

	public Fragment[] listFragments;
	private FragmentManager fm;
	public int curFragment;
	public int preFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab_cart);
		initUI();
		initControl();
		checkAccount();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			refreshContent();
		}
	}

	private void checkAccount()
	{
		if (GlobalValue.myAccount == null)
			GlobalValue.myAccount = ParserUtility.convertJsonStringtoAccount(new MySharedPreferences(this).getCachUserInfo());
	}

	private void initUI() {
		fm = getSupportFragmentManager();
		listFragments = new Fragment[2];
		listFragments[PAGE_SHOP_CART] = fm
				.findFragmentById(R.id.fragmentShopCart);
		listFragments[PAGE_CONFIRM] = fm
				.findFragmentById(R.id.fragmentDeliveryInfo);
	}

	private void initControl() {
		refreshContent();
	}

	public void refreshContent() {
		ShopCartFragment fm = (ShopCartFragment) listFragments[PAGE_SHOP_CART];
		fm.refreshContent();
		showFragment(PAGE_SHOP_CART);
	}

	public void showFragment(int fragmentIndex) {
		preFragment = curFragment;
		curFragment = fragmentIndex;
		FragmentTransaction transaction = fm.beginTransaction();

		for (int i = 0; i < listFragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(listFragments[i]);
			} else {
				transaction.hide(listFragments[i]);
			}
		}

		transaction.commit();
	}

	public void gotoFragment(int fragment) {
		preFragment = curFragment;
		curFragment = fragment;
		Log.e("huy-log", "current-fragment 1:" + curFragment);
		FragmentTransaction transaction = fm.beginTransaction();

		transaction.setCustomAnimations(R.anim.push_left_in,
				R.anim.push_left_out);
		for (Fragment item : listFragments) {
			transaction.hide(item);
		}
		transaction.show(listFragments[fragment]);
		transaction.commit();
	}

	public void backFragment(int fragment) {
		preFragment = curFragment;
		curFragment = fragment;
		FragmentTransaction transaction = fm.beginTransaction();

		transaction.setCustomAnimations(R.anim.push_right_in,
				R.anim.push_right_out);
		transaction.hide(listFragments[preFragment]);
		transaction.show(listFragments[fragment]);
		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (curFragment != PAGE_SHOP_CART) {
			backFragment(preFragment);
		} else {
			this.getParent().onBackPressed();
		}
	}

}
