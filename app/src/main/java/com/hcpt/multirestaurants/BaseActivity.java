package com.hcpt.multirestaurants;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hcpt.multirestaurants.activity.ListCategoryActivity;
import com.hcpt.multirestaurants.activity.ListFoodActivity;
import com.hcpt.multirestaurants.activity.OfferActivity;
import com.hcpt.multirestaurants.activity.ShopDetailActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.network.ProgressDialog;

public class BaseActivity extends FragmentActivity {
	public static String TAG;
	public static boolean DEBUG_MODE = false;
	protected BaseActivity self;

	protected ProgressDialog progressDialog;
	protected LinearLayout userStatusLayout;
	protected Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		TAG = self.getClass().getSimpleName();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}
	
	public void gotoShopDetail(int shopId) {
		if (ListFoodActivity.self != null) {
			ListFoodActivity.self.finish();
			ListFoodActivity.self=null;
		}
		if (ListCategoryActivity.self != null) {
			ListCategoryActivity.self.finish();
			ListCategoryActivity.self=null;
		}
	
		if (OfferActivity.self != null) {
			OfferActivity.self.finish();
			OfferActivity.self=null;
		}
		if(ShopDetailActivity.self==null)
		{
			Bundle b=new Bundle();
			b.putInt(GlobalValue.KEY_SHOP_ID,shopId);
			backActivity(ShopDetailActivity.class, b);
		}
		
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		
	}

	public void changeInputKeyBoad() {

		Locale.setDefault(Locale.JAPANESE);
		Configuration config = getResources().getConfiguration();
		config.locale = Locale.JAPANESE;
		getBaseContext().getResources().updateConfiguration(config, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */

	public void gotoActivity(Class<?> cla) {
		Intent intent = new Intent(this, cla);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
	}
	
	public void backActivity(Class<?> cla,Bundle b) {
		Intent intent = new Intent(this, ListCategoryActivity.class);
		intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
	}


	public void backActivity(Class<?> cla) {
		Intent intent = new Intent(this, cla);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	public void gotoActivity(Class<?> cla, int flag) {
		Intent intent = new Intent(this, cla);
		intent.setFlags(flag);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
	}

	/**
	 * goto view website
	 * 
	 * @param url
	 */
	public void gotoWeb(Uri uri) {
		Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(myIntent);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			int requestCode) {
		Intent intent = new Intent(context, cla);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 */
	public void gotoActivity(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 * @param requestCode
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Goto web page
	 * 
	 * @param url
	 */
	protected void gotoWebPage(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}

	/**
	 * Goto phone call
	 * 
	 * @param telNumber
	 */
	protected void gotoPhoneCallPage(String telNumber) {
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(R.string.tel
				+ telNumber));
		startActivity(i);
	}

	/**
	 * Close activity
	 */
	public void closeActivity(View v) {
		finish();
	}

	// ********************* NOTIFICATION *******************

	// ======================= TOAST MANAGER =======================

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(String str) {
		Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param strId
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(int strId) {
		Toast.makeText(self, getString(strId), Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showShortToastMessage(String str) {
		Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
	}

	public void showShortToastMessage(int str) {
		Toast.makeText(self, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(String str, int time) {
		Toast.makeText(self, str, time).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */

	public void showToastMessage(int resId, int time) {
		Toast.makeText(self, resId, time).show();
	}

	/**
	 * Show comming soon toast message
	 */
	public void showComingSoonMessage() {
		showToastMessage("Coming soon!");
	}

	// ======================= PROGRESS DIALOG ======================

	/**
	 * Open progress dialog
	 * 
	 * @param text
	 */
	public void showProgressDialog() {
		try {
			// showProgressDialog(getString(R.string.message_please_wait));
			if (progressDialog == null) {
				try {
					progressDialog = new ProgressDialog(self);
					progressDialog.show();
					progressDialog.setCancelable(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					progressDialog = new ProgressDialog(self.getParent());
					progressDialog.show();
					progressDialog.setCancelable(false);
					e.printStackTrace();
				}
			} else {
				if (!progressDialog.isShowing())
					progressDialog.show();
			}
		} catch (Exception e) {
			progressDialog = new ProgressDialog(self);
			progressDialog.show();
			progressDialog.setCancelable(false);
			e.printStackTrace();
		}
	}

	public void showProgressDialog(Context context) {
		try {
			// showProgressDialog(getString(R.string.message_please_wait));
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(context);
				progressDialog.show();
				progressDialog.setCancelable(false);

			} else {
				if (!progressDialog.isShowing())
					progressDialog.show();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog.cancel();
			progressDialog = null;
		}

	}

}
