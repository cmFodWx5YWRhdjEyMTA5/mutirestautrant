package com.hcpt.multirestaurants.activity.tabs.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseFragment;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.MainUserActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Account;
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.util.MySharedPreferences;
import com.hcpt.multirestaurants.util.NetworkUtil;
import com.hcpt.multirestaurants.util.StringUtility;

public class UpdateInforFragment extends BaseFragment implements
        OnClickListener {
    public static final String MESSAGE_SUCCESS = "success";
    private View view;
    private TextView btnUpdate;
    private EditText lblFullname, lblEmail, lblPhone, lblAddress;
    private TextView lblUserName;
    private ImageView btnBack;
    private String id;
    private Account account = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_updateinfor,
                container, false);
        initUI(view);
        refreshContent();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            setData();
        }
    }

    private void initUI(View view) {
        btnUpdate = (TextView) view.findViewById(R.id.btnUpdateInfor);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        lblUserName = (TextView) view.findViewById(R.id.lblUserName);
        lblFullname = (EditText) view.findViewById(R.id.edtFullName);
        lblEmail = (EditText) view.findViewById(R.id.edtEmail);
        lblPhone = (EditText) view.findViewById(R.id.edtPhone);
        lblAddress = (EditText) view.findViewById(R.id.edtAddress);
        btnUpdate.setOnClickListener(this);

        btnBack.setOnClickListener(this);
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;

        String email = lblEmail.getText().toString();
        String fullname = lblFullname.getText().toString();
        String phone = lblPhone.getText().toString();
        String address = lblAddress.getText().toString();

        // email
        if (email.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Email_empty);
            return message;
        } else {
            if (!StringUtility.isEmailValid(email)) {
                message = getCurrentActivity().getResources().getString(
                        R.string.error_Email_wrong);
                return message;
            }
        }
        // fullname
        if (fullname.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_FullName);
            return message;
        }

        // phone
        if (phone.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Phone);
            return message;
        }

        // address
        if (address.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Address);
            return message;
        }

        id = GlobalValue.myAccount.getId() + "";

        return message;
    }

    private void setData() {
        //check if myAccount is released
        if (GlobalValue.myAccount == null)
            GlobalValue.myAccount = ParserUtility.convertJsonStringtoAccount(new MySharedPreferences(getCurrentActivity()).getCachUserInfo());
        //set data
        lblUserName.setText(GlobalValue.myAccount.getUserName());
        lblFullname.setText(GlobalValue.myAccount.getFull_name());
        lblEmail.setText(GlobalValue.myAccount.getEmail());
        lblPhone.setText(GlobalValue.myAccount.getPhone());
        lblAddress.setText(GlobalValue.myAccount.getAddress());

    }

    @Override
    public void refreshContent() {
        if (GlobalValue.myAccount == null)
            GlobalValue.myAccount = ParserUtility.convertJsonStringtoAccount(new MySharedPreferences(getCurrentActivity()).getCachUserInfo());

        if (GlobalValue.myAccount != null) {
            setData();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnUpdate) {

            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                CustomToast.showCustomAlert(getCurrentActivity(), message,
                        Toast.LENGTH_SHORT);
            } else {
                if (GlobalValue.myAccount != null) {
                    // check network
                    if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
                        update(account);
                    } else {
                        Toast.makeText(getCurrentActivity(),
                                R.string.message_network_is_unavailable,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        }

        if (v == btnBack) {
            // hidden keyboard :
            hiddenKeyboard();
            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.onBackPressed();
        }
    }

    private void update(Account account) {
        // TODO Auto-generated method stub
        ModelManager.updateInforUser(getCurrentActivity(), id, lblEmail
                        .getText().toString(), lblFullname.getText().toString(),
                lblPhone.getText().toString(), lblAddress.getText().toString(),
                true, new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        MainUserActivity activity = (MainUserActivity) getCurrentActivity();
                        GlobalValue.myAccount.setAddress(lblAddress.getText()
                                + "");
                        GlobalValue.myAccount.setEmail(lblEmail.getText() + "");
                        GlobalValue.myAccount.setFull_name(lblFullname
                                .getText() + "");
                        GlobalValue.myAccount.setPhone(lblPhone.getText() + "");
                        new MySharedPreferences(getCurrentActivity()).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                        activity.onBackPressed();

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
