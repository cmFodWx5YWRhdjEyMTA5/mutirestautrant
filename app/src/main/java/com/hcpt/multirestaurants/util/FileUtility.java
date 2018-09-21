package com.hcpt.multirestaurants.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public final class FileUtility extends Activity {

	

	public Bitmap getBitmapFromAssets(Context context, String fileName)
			throws IOException {
		AssetManager assetManager = context.getAssets();
		InputStream istr = assetManager.open(fileName);
		Bitmap bitmap = BitmapFactory.decodeStream(istr);

		return bitmap;
	}

	/**
	 * filename need external file
	 * 
	 * @param Url
	 * @param filename
	 * @return
	 */
	public static boolean storeImage(String Url, String filename) {
		// get path to external storage (SD card)
		String musicStoragePath = Environment.getExternalStorageDirectory()
				+ "/music/";
		File StorageDir = new File(musicStoragePath);
		// create storage directories, if they don't exist
		if (!StorageDir.exists()) {
			StorageDir.mkdirs();
		}
		try {
			File file = new File(StorageDir.toString(), filename);

			if (file.exists()) {
				file.delete();
			}

			FileOutputStream out = new FileOutputStream(file);
			InputStream is = (InputStream) new URL(Url).getContent();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			out.flush();
			out.close();
			Log.d("asd", "asd: finish ");
		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving music file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving music file: " + e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean storeFile(String urlFrom,String urlDestination,String fileName) {
		int count;
		try {
			URL urlfile = new URL(urlFrom);
			URLConnection ucon = urlfile.openConnection();
			InputStream input = ucon.getInputStream();

			File dataFolder = new File(urlDestination);
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}
			String fullName=urlDestination+"/"+fileName;
			OutputStream output = new FileOutputStream(fullName);

			byte data[] = new byte[1024];
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}
			// flushing output
			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static String readStringFromIS(InputStream is) {
		String datax = "";
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader buffreader = new BufferedReader(isr);

			String readString = buffreader.readLine();
			while (readString != null) {
				datax = datax + readString;
				readString = buffreader.readLine();
			}

			isr.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return datax;
	}

}
