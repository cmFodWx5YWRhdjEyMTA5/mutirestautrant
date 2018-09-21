package com.hcpt.multirestaurants.activity.tabs.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcpt.multirestaurants.BaseFragment;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.MainUserActivity;
import com.hcpt.multirestaurants.config.GlobalValue;

public class InfoFragment extends BaseFragment implements OnClickListener {
    private View view;
    private TextView btnUpdateInfor, btnUpdatePass;
    private TextView lblUserName, lblPassword, lblFullname, lblEmail, lblPhone,
            lblAddress;
    private ImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_info, container,
                false);
        initUI(view);
        refreshContent();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshContent();
        }
    }

    private void initUI(View view) {
        btnUpdatePass = (TextView) view.findViewById(R.id.btnUpdatePass);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        btnUpdateInfor = (TextView) view.findViewById(R.id.btnUpdateInfor);
        lblUserName = (TextView) view.findViewById(R.id.lblUserName);
        lblPassword = (TextView) view.findViewById(R.id.lblPassword);
        lblFullname = (TextView) view.findViewById(R.id.lblFullName);
        lblEmail = (TextView) view.findViewById(R.id.lblEmail);
        lblPhone = (TextView) view.findViewById(R.id.lblPhone);
        lblAddress = (TextView) view.findViewById(R.id.lblAddress);
        btnUpdatePass.setOnClickListener(this);
        btnUpdateInfor.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void setData() {


        if (!GlobalValue.myAccount.getPassword().isEmpty() && GlobalValue.myAccount.getPassword().length()>=2) {
            String pass = GlobalValue.myAccount.getPassword().substring(
                    GlobalValue.myAccount.getPassword().length() - 2,
                    GlobalValue.myAccount.getPassword().length());
            lblPassword.setText("*****" + pass);
        } else {
            lblPassword.setText("*******");
        }
        lblUserName.setText(GlobalValue.myAccount.getUserName());
        lblFullname.setText(GlobalValue.myAccount.getFull_name());
        lblEmail.setText(GlobalValue.myAccount.getEmail());
        lblPhone.setText(GlobalValue.myAccount.getPhone());
        lblAddress.setText(GlobalValue.myAccount.getAddress());

        //check login type
        if (GlobalValue.myAccount.isSocialLogin())
            btnUpdatePass.setVisibility(View.GONE);
        else
            btnUpdatePass.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshContent() {
        if (GlobalValue.myAccount != null) {
            setData();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnUpdateInfor) {

            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.gotoFragment(new UpdateInforFragment());
        }
        if (v == btnUpdatePass) {
            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.gotoFragment(new UpdatePassFragment());
        }
        if (v == btnBack) {
            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.backFragment(new AccountFragment());
        }
    }
}
