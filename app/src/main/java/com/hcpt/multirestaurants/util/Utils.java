package com.hcpt.multirestaurants.util;

/**
 * Created by Na Pro on 08/18/2015.
 */

import android.text.format.DateFormat;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hcpt.multirestaurants.activity.tabs.HomeActivity;

import java.util.Calendar;

public class Utils {

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView,
			Button loadMore, String fromScreen) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (fromScreen.equals(HomeActivity.HOME_SCREEN)) {
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
					+ loadMore.getMeasuredHeight() + 8;
		} else {
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		}
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static String getCurrentTimestamp(){
		Long tsLong = System.currentTimeMillis()/1000;
		return tsLong.toString();
	}

	public static String getTimeFromString(String time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(time)*1000);
		return DateFormat.format("HH:mm", cal).toString();
	}

	public static int getHour(String time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(time) * 1000);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
}
