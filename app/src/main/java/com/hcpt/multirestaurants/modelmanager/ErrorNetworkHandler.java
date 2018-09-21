package com.hcpt.multirestaurants.modelmanager;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by HUY on 11/28/2015.
 */
public class ErrorNetworkHandler {

    public static String processError(VolleyError error) {
        String message = "Opp! Server error is undefined. Try again please!";
        if (error != null) {
            if (error instanceof TimeoutError) {
                message = "Your request is bad! Over request time out. Try again please!";
            } else if (error instanceof NoConnectionError) {
                message = "No connection! Try again please!";
            } else if (error instanceof AuthFailureError) {
                Log.d("Error", "AuthFailureError");
            } else if (error instanceof ServerError) {
                message = "Server error! Try again please!";
            } else if (error instanceof NetworkError) {
                message = "Your network has problem! Try again please!";
            } else if (error instanceof ParseError) {
                Log.d("Error", "ParseError");
            }
        }
        return message;
    }
}
