package com.hcpt.multirestaurants.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.MainTabActivity;
import com.hcpt.multirestaurants.util.GPSTracker;
import com.hcpt.multirestaurants.util.NetworkUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private GPSTracker gps;

    List<String> listPermission = new ArrayList<>();
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT>=23){
            if (checkAndRequestPermissions()){
                NetworkUtil.enableStrictMode();
                getAppKeyHash();
                gps = new GPSTracker(self);
                checkBaseCondition();
            }
        }else {
            NetworkUtil.enableStrictMode();
            getAppKeyHash();
            gps = new GPSTracker(self);
            checkBaseCondition();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    private void checkBaseCondition() {
        if (NetworkUtil.checkNetworkAvailable(this)) {

            if (!gps.canGetLocation()) {
                gps.showSettingsAlert();
            } else {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        gotoActivity(MainTabActivity.class);
                        finish();
                    }
                }, 3000);
            }
        } else {
            showWifiSetting(this);
        }
    }

    public void showWifiSetting(final Context act) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);

        // Setting Dialog Title
        alertDialog.setTitle("Wifi is settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("Wifi is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_SETTINGS);
                        act.startActivity(intent);
                        dialog.dismiss();
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hcpt.multirestaurants", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", "HASH KEY : " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {

        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {
        }

    }

    private boolean checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        for ( String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                listPermission.add(permission);
            }
        }

        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray
                    (new String[listPermission.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                Boolean allPermissionsGranted  = true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            allPermissionsGranted  = false;
                            break;
                        }
                    }
                    if (!allPermissionsGranted) {
                        boolean somePermissionsForeverDenied = false;
                        boolean checkAllPermissonAllAlowed= true;
                        for(String permission: permissions){
                            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                                //denied
                                checkAndRequestPermissions();
                                checkAllPermissonAllAlowed = false;
                                Log.e("denied", permission);
                            }else{
                                if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                                    //allowed
                                    Log.e("allowed", permission);
                                } else{
                                    //set to never ask again
                                    Log.e("set to never ask again", permission);
                                    somePermissionsForeverDenied = true;
                                    checkAllPermissonAllAlowed = false;
                                    break;
                                }
                            }
                        }

                        if(somePermissionsForeverDenied){
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("You have forcefully denied some of the required permissions " +
                                            "for this action. Please open settings, go to permissions and allow them.")
                                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(1);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                        if (checkAllPermissonAllAlowed){
                            NetworkUtil.enableStrictMode();
                            getAppKeyHash();
                            gps = new GPSTracker(self);
                            checkBaseCondition();
                        }
                    } else {
                        NetworkUtil.enableStrictMode();
                        getAppKeyHash();
                        gps = new GPSTracker(self);
                        checkBaseCondition();
                    }
                }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkAndRequestPermissions();
    }
}
