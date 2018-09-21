package com.hcpt.multirestaurants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.hcpt.multirestaurants.network.ProgressDialog;

public class BaseFragment extends Fragment {
	public static boolean DEBUG_MODE = false;
	private Button btnMenu;
	public Activity self;
	protected ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		self = getActivity();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void refreshContent() {

	}

	public Activity getCurrentActivity() {
		if (self == null)
			self = getActivity();
		return self;
	}

	public void hiddenKeyboard() {
		View view = getCurrentActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getCurrentActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void onclickmenuItem(View view) {

	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivity(Class<?> cla) {
		Intent intent = new Intent(self, cla);
		self.startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.push_left_out);
	}

	public void gotoActivity(Class<?> cla, int flag) {
		Intent intent = new Intent(self, cla);
		intent.setFlags(flag);
		self.startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_left);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 */
	public void gotoActivity(Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(getActivity(), cla);
		intent.putExtras(bundle);
		self.startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_left);
	}

	// ======================= TOAST MANAGER =======================

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param strId
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(int strId) {
		Toast.makeText(getActivity(), getString(strId), Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showShortToastMessage(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	public void showShortToastMessage(int str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	public void showToastMessageResources(int str, int time) {
		String message = getActivity().getResources().getString(str);
		Toast.makeText(getActivity(), message, time).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(String str, int time) {
		Toast.makeText(getActivity(), str, time).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */

	public void showToastMessage(int resId, int time) {
		Toast.makeText(getActivity(), resId, time).show();
	}

	/**
	 * Show comming soon toast message
	 */
	public void showComingSoonMessage() {
		showToastMessage("Coming soon!");
	}

	/**
	 * Goto web page
	 * 
	 * @param url
	 */
	public void gotoWebPage(String url) {

		if (!url.contains("http"))
			url = "http://" + url;
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}

	/**
	 * Goto phone call
	 * 
	 * @param telNumber
	 */
	protected void gotoPhoneCallPage(String telNumber) {
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
		startActivity(i);
	}

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
