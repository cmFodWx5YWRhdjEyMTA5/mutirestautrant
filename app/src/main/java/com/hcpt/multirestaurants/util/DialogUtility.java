package com.hcpt.multirestaurants.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.Toast;

import com.hcpt.multirestaurants.R;

public final class DialogUtility {
	public static void alert(Context context, int messageId) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
		alertDialog.setTitle(context.getString(R.string.app_name));
		alertDialog.setMessage(context.getString(messageId));
		alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		alertDialog.show();
	}
	public static void alert(Context context, String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
		alertDialog.setTitle(context.getString(R.string.app_name));
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		alertDialog.show();
	}

	public static void alert(Context context,String title, String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		alertDialog.show();
	}

	public static void toast(Context context, int messageId) {
		Toast.makeText(context, context.getString(messageId), Toast.LENGTH_LONG).show();
	}

	public static void showOkDialog(Context context, int messageId, int OkTextId,
			final DialogInterface.OnClickListener onOKClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
			alertDialog.setTitle(context.getString(R.string.app_name));
			alertDialog.setMessage(context.getString(messageId));

			alertDialog.setPositiveButton(OkTextId, onOKClick);
			alertDialog.show();
		}
	}

	public static void showYesNoDialog(final Context context, int messageId, int OkTextId, int cancelTextId,
			final DialogInterface.OnClickListener onOKClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
			alertDialog.setTitle(context.getString(R.string.app_name));
			alertDialog.setMessage(context.getString(messageId));
			alertDialog.setPositiveButton(cancelTextId, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();

				}
			});
			alertDialog.setNegativeButton(OkTextId, onOKClick);
			alertDialog.show();
		}
	}

	public static void showYesNoDialog(final Context context, int messageId, int OkTextId, int cancelTextId,
			final DialogInterface.OnClickListener onOKClick, final DialogInterface.OnClickListener onCancelClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
			alertDialog.setTitle(context.getString(R.string.app_name));
			alertDialog.setMessage(context.getString(messageId));
			alertDialog.setPositiveButton(cancelTextId, onCancelClick);
			alertDialog.setNegativeButton(OkTextId, onOKClick);
			alertDialog.show();
		}
	}

	private static String temp = "";

	public static void showInputDialog(Context c, int titleId, int hintId, int okTextId, int cancelTextId, int iconId,
			String returnValue) {

		AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(c, android.R.style.Theme_Holo_Light_Dialog));
		final EditText input = new EditText(c);
		alert.setView(input);

		alert.setTitle(c.getString(titleId));
		alert.setIcon(iconId);
		alert.setPositiveButton(c.getString(okTextId), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				temp = input.getText().toString().trim();
				Logger.e("DialogUtility", "Value entered" + temp);
				if (temp != "")
					dialog.cancel();
			}
		});

		alert.setNegativeButton(c.getString(cancelTextId), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();

			}
		});
		returnValue = temp;
		alert.show();
	}

	public static void showSimpleOptionDialog(Context mContext, int titleId, String[] items, String positiverButton,
			DialogInterface.OnClickListener itemOnClick, DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
		builder.setTitle(mContext.getString(titleId));
		builder.setItems(items, itemOnClick);
		builder.setPositiveButton(positiverButton, positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alert(Context context, String message, DialogInterface.OnClickListener onOkClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
			alertDialog.setTitle(R.string.app_name);
			alertDialog.setMessage(message);
			alertDialog.setPositiveButton(android.R.string.ok, onOkClick);
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
	}
}
