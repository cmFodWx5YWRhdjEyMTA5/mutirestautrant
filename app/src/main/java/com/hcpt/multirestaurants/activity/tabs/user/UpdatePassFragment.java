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
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.util.MySharedPreferences;
import com.hcpt.multirestaurants.util.NetworkUtil;

public class UpdatePassFragment extends BaseFragment implements OnClickListener {
    public static final String MESSAGE_SUCCESS = "success";
    private View view;
    private TextView btnUpdatePass;
    private TextView lblUserName, lblPassword;
    private EditText txtPassOld, txtPassNew, txtPassComfirm;
    private ImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_updatepass,
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
            clearForm();
        } else {
            refreshContent();
        }
    }

    private void initUI(View view) {
        btnUpdatePass = (TextView) view.findViewById(R.id.btnUpdatePass);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
        txtPassNew = (EditText) view.findViewById(R.id.edtPassNew);
        txtPassComfirm = (EditText) view.findViewById(R.id.edtPassComfirm);
        txtPassOld = (EditText) view.findViewById(R.id.edtPassOld);
        lblUserName = (TextView) view.findViewById(R.id.lblUserName);
        lblPassword = (TextView) view.findViewById(R.id.lblPassword);
        btnUpdatePass.setOnClickListener(this);

        btnBack.setOnClickListener(this);
    }

    private void clearForm() {
        txtPassComfirm.setText("");
        txtPassNew.setText("");
        txtPassOld.setText("");
    }

    private void setData() {

        String pass = GlobalValue.myAccount.getPassword().substring(
                GlobalValue.myAccount.getPassword().length() - 2,
                GlobalValue.myAccount.getPassword().length());
        lblUserName.setText(GlobalValue.myAccount.getUserName());
        lblPassword.setText("*****" + pass);

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

        if (v == btnUpdatePass) {
            String message = validateForm();
            if (!message.equals(MESSAGE_SUCCESS)) {
                CustomToast.showCustomAlert(getCurrentActivity(), message,
                        Toast.LENGTH_SHORT);
            } else {
                if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
                    update();
                } else {
                    Toast.makeText(getCurrentActivity(),
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
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

    private void update() {
        // TODO Auto-generated method stub
        String password = txtPassNew.getText().toString().trim();
        ModelManager.updatePassUser(getCurrentActivity(),
                GlobalValue.myAccount.getId() + "",password, true, new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub

                        MainUserActivity activity = (MainUserActivity) getCurrentActivity();
                        GlobalValue.myAccount.setPassword(txtPassNew.getText()
                                .toString());
                        activity.onBackPressed();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String validateForm() {
        String message = MESSAGE_SUCCESS;

        String passComfirm = txtPassComfirm.getText().toString();
        String passNew = txtPassNew.getText().toString();
        String passOld = txtPassOld.getText().toString();

        // password
        if (passOld.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Password_empty);
            return message;
        }
        if (!passOld.equals(GlobalValue.myAccount.getPassword())) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_Password_Wrong);
            return message;
        }
        // repassword
        if (passNew.isEmpty()) {
            message = getCurrentActivity().getResources().getString(
                    R.string.error_PasswordNew_empty);
            return message;
        } else {
            if (!passNew.equals(passComfirm)) {
                message = getCurrentActivity().getResources().getString(
                        R.string.error_RePassword_empty);
                return message;
            }
        }

        return message;
    }
}
