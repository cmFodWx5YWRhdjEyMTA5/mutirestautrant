package com.hcpt.multirestaurants.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hcpt.multirestaurants.R;

public class CustomToast {

	public static void showCustomAlert(Activity activity, String message,
			int duration) {

		// Create layout inflator object to inflate toast.xml file
		LayoutInflater inflater = activity.getLayoutInflater();
		// Call toast.xml file for toast layout
		View toastRoot = inflater.inflate(R.layout.toast_layout, null);
		TextView lblMessage = (TextView) toastRoot
				.findViewById(R.id.lblMessage);
		lblMessage.setText(message);
		Toast toast = new Toast(activity);
		// Set layout to toast
		toast.setView(toastRoot);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.setDuration(duration);
		toast.show();

	}

}
