package com.hcpt.multirestaurants.activity.tabs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.FrameLayout;

import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.user.AccountFragment;
import com.hcpt.multirestaurants.activity.tabs.user.LoginFragment;
import com.hcpt.multirestaurants.config.GlobalValue;

//import android.support.v7.internal.view.ContextThemeWrapper;
public class MainUserActivity extends FragmentActivity {
    private FrameLayout frameLayout;

    private boolean isLoadNew = true;

    public boolean isLoadNew() {
        return isLoadNew;
    }

    public void setLoadNew(boolean isLoadNew) {
        this.isLoadNew = isLoadNew;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_user);
        initControl();
        frameLayout = findViewById(R.id.frameLayout);
    }

    private void initControl() {
        refreshContent();
    }

    public void refreshContent() {
        if (GlobalValue.myAccount == null) {
            showFragment(new LoginFragment());
        } else {
            showFragment(new AccountFragment());
        }
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void gotoFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_left_in,
                R.anim.push_left_out);
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void backFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_right_in,
                R.anim.push_right_out);
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
    }

    public void showLogoutConfirmDialog() {

        AlertDialog.Builder build = new Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog)).setTitle("Log out")
                .setMessage("Do you want to log out?")
                .setNegativeButton("Yes", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (GlobalValue.myAccount != null)
                            GlobalValue.myAccount = null;
                        refreshContent();
                    }
                }).setPositiveButton("No", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

        build.show();
    }
}
