package com.hcpt.multirestaurants.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.hcpt.multirestaurants.interfaces.IMaps;

public class MapsUtil {

	private static final String TAG = "MapsUtil";

	public static class GetLatLngByAddress extends
			AsyncTask<String, Void, LatLng> {

		private IMaps imap;

		public GetLatLngByAddress(IMaps imap) {
			this.imap = imap;
		}

		@Override
		protected LatLng doInBackground(String... params) {
			try {
				String strAddress = params[0];
				if (strAddress.contains(" ")) {
					strAddress = strAddress.replace(" ", "%20");
				}
				String uri = "http://maps.google.com/maps/api/geocode/json?address="
						+ strAddress + "&sensor=false";
				HttpGet httpGet = new HttpGet(uri);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;
				StringBuilder stringBuilder = new StringBuilder();

				try {
					response = client.execute(httpGet);
					HttpEntity entity = response.getEntity();
					InputStream stream = entity.getContent();
					int b;
					while ((b = stream.read()) != -1) {
						stringBuilder.append((char) b);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return new LatLng(0.0, 0.0);
				}

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject = new JSONObject(stringBuilder.toString());

					double lng = ((JSONArray) jsonObject.get("results"))
							.getJSONObject(0).getJSONObject("geometry")
							.getJSONObject("location").getDouble("lng");

					double lat = ((JSONArray) jsonObject.get("results"))
							.getJSONObject(0).getJSONObject("geometry")
							.getJSONObject("location").getDouble("lat");

					return new LatLng(lat, lng);
				} catch (Exception ex) {
					ex.printStackTrace();
					return new LatLng(0.0, 0.0);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return new LatLng(0.0, 0.0);
			}
		}

		@Override
		protected void onPostExecute(LatLng result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				imap.processFinished(result);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static class GetAddressByLatLng extends
			AsyncTask<LatLng, Void, String> {

		private IMaps imap;

		public GetAddressByLatLng(IMaps imap) {
			this.imap = imap;
		}

		@Override
		protected String doInBackground(LatLng... params) {
			try {
				LatLng latLng = params[0];

				String uri = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
						+ latLng.latitude
						+ ","
						+ latLng.longitude
						+ "&sensor=true";

				HttpParams par = new BasicHttpParams();
				HttpProtocolParams.setVersion(par, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(par, "UTF-8");
				par.setBooleanParameter("http.protocol.expect-continue", false);

				HttpGet httpGet = new HttpGet(uri);
				HttpClient client = new DefaultHttpClient(par);
				HttpResponse response;

				String strResponse = "";
				try {
					response = client.execute(httpGet);
					HttpEntity entity = response.getEntity();

					strResponse = EntityUtils.toString(entity, HTTP.UTF_8);
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject = new JSONObject(strResponse);

					String address = ((JSONArray) jsonObject.get("results"))
							.getJSONObject(0).getString("formatted_address");

					return address;
				} catch (Exception ex) {
					ex.printStackTrace();
					return "";
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				imap.processFinished(result);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static class GetCurrentLocation extends AsyncTask<Void, Void, Void>
			implements LocationListener {

		private GoogleMap maps;
		private int zoom;
		private boolean enableMyLoc, updateMyLoc;
		private Context context;

		public GetCurrentLocation(Context context, GoogleMap maps, int zoom,
				boolean enableMyLoc, boolean updateMyLoc) {
			this.context = context;
			this.maps = maps;
			this.zoom = zoom;
			this.enableMyLoc = enableMyLoc;
			this.updateMyLoc = updateMyLoc;
		}

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				// Check google play services sdk.
				if (GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
					maps.setMyLocationEnabled(enableMyLoc);

					// Move to current location
					LocationManager locManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);

					Criteria crit = new Criteria();

					String provider = locManager.getBestProvider(crit, true);

					Location loc = locManager.getLastKnownLocation(provider);

					if (loc != null) {
						onLocationChanged(loc);
					}

					// Update location
					if (updateMyLoc) {
						locManager.requestLocationUpdates(provider, 20000, 0,
								this);
					}
				} else {
					GooglePlayServicesUtil
							.getErrorDialog(
									GooglePlayServicesUtil
											.isGooglePlayServicesAvailable(context),
									(Activity) context, 1).show();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onLocationChanged(Location location) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());

			maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	}

	public static class GetCurrentLatLong extends AsyncTask<Void, Void, LatLng>
			implements LocationListener {

		private Context context;
		private IMaps iMaps;

		public GetCurrentLatLong(Context ctx, IMaps iMaps) {
			context = ctx;
			this.iMaps = iMaps;
		}

		@Override
		protected LatLng doInBackground(Void... params) {
			try {
				// Check google play services sdk.
				if (GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {

					// Move to current location
					LocationManager locManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);

					Criteria crit = new Criteria();

					String provider = locManager.getBestProvider(crit, true);

					Location loc = locManager.getLastKnownLocation(provider);

					if (loc != null) {
						return new LatLng(loc.getLatitude(), loc.getLongitude());
					}
				} else {
					GooglePlayServicesUtil
							.getErrorDialog(
									GooglePlayServicesUtil
											.isGooglePlayServicesAvailable(context),
									(Activity) context, 1).show();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(LatLng result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				iMaps.processFinished(result);
			}
		}

		@Override
		public void onLocationChanged(Location location) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	}

	// Seems this method is working not correctly.
	public static class GetAddressCompletely extends
			AsyncTask<LatLng, Void, Address> {

		private IMaps imap;
		private Context context;

		public GetAddressCompletely(Context context, IMaps imap) {
			this.context = context;
			this.imap = imap;
		}

		@Override
		protected Address doInBackground(LatLng... params) {
			// TODO Auto-generated method stub
			LatLng latLng = params[0];

			Geocoder geocoder;
			List<Address> addresses = null;
			geocoder = new Geocoder(context, Locale.getDefault());

			// Here 1 represent max location result to returned, by documents it
			// recommended 1 to 5
			try {
				addresses = geocoder.getFromLocation(latLng.latitude,
						latLng.longitude, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (addresses != null && addresses.size() > 0) {
				return addresses.get(0);
			}

			return new Address(Locale.getDefault());
		}

		@Override
		protected void onPostExecute(Address result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			imap.processFinished(result);
		}
	}
}
