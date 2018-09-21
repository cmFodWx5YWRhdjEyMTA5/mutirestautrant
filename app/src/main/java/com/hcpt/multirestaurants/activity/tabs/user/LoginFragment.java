package com.hcpt.multirestaurants.activity.tabs.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.hcpt.multirestaurants.BaseFragment;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.activity.tabs.MainUserActivity;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Account;
import com.hcpt.multirestaurants.util.CustomToast;
import com.hcpt.multirestaurants.util.Logger;
import com.hcpt.multirestaurants.util.MySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hcpt.multirestaurants.util.NetworkUtil;

@SuppressLint("NewApi")
public class LoginFragment extends BaseFragment implements OnClickListener {

    private View view;
    private EditText txtUser, txtPass;
    private TextView btnLogin, btnRegister, btnLoginFacebook;
    private String userName = "";
    private String passWord = "";
    private Account account = null;


    // ======= LOGIN BY FACEBOOK ===========

    private CallbackManager callbackManager;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        // Init UI
        // Prepare service
        self = getActivity();
        FacebookSdk.sdkInitialize(self.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        initUI(view);
        initControls();
        refreshContent();
        return view;
    }

    private void initUI(View view) {
        txtUser = (EditText) view.findViewById(R.id.txtUser);
        txtPass = (EditText) view.findViewById(R.id.txtPassWord);
        btnLogin = (TextView) view.findViewById(R.id.btnLogin);
        btnRegister = (TextView) view.findViewById(R.id.btnRegister);
        btnLoginFacebook = (TextView) view.findViewById(R.id.btnLoginFacebook);
    }

    private void initControls() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLoginFacebook.setOnClickListener(this);
    }

    @Override
    public void refreshContent() {
        txtUser.setText("");
        txtPass.setText("");
//        txtUser.setText("mango1");
//        txtPass.setText("123456");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnLogin) {
            if (txtUser.getText().toString().isEmpty()
                    || txtPass.getText().toString().isEmpty()) {
                CustomToast.showCustomAlert(
                        getCurrentActivity(),
                        getCurrentActivity().getString(
                                R.string.message_input_user_pass),
                        Toast.LENGTH_SHORT);
            } else {
                userName = txtUser.getText().toString();
                passWord = txtPass.getText().toString();
                // check network
                if (NetworkUtil.checkNetworkAvailable(getCurrentActivity())) {
                    login(userName, passWord);
                } else {
                    Toast.makeText(getCurrentActivity(),
                            R.string.message_network_is_unavailable,
                            Toast.LENGTH_LONG).show();
                }

            }
        } else if (v == btnRegister) {
            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
            activity.gotoFragment(new RegisterFragment());
        } else if (v == btnLoginFacebook) {
            loginFacebook();
        }
    }

    private void login(String userName2, String passWord2) {

        ModelManager.login(getCurrentActivity(), userName2, passWord2, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        if (ParserUtility.isSuccess(json)) {
                            Account account = ParserUtility.parseAccount(json);
                            if (account != null) {
                                GlobalValue.myAccount = account;
                                GlobalValue.myAccount.setPassword(passWord);
                                GlobalValue.myAccount.setType(Account.LOGIN_TYPE_NORMAL);
                                new MySharedPreferences(getCurrentActivity()).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                                MainUserActivity activity = (MainUserActivity) getCurrentActivity();
                                activity.refreshContent();
                            } else {
                                CustomToast.showCustomAlert(
                                        getCurrentActivity(),
                                        getCurrentActivity().getString(
                                                R.string.message_login_false),
                                        Toast.LENGTH_SHORT);

                            }
                        } else {
                            Toast.makeText(self, getErrorMessage(json), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        ErrorNetworkHandler.processError(error);
                    }
                });
    }

    // ======= LOGIN BY FACEBOOK ===========

    // Call login Facebook
    public void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance()
                .registerCallback(callbackManager, fbCallback);
        LoginManager.getInstance().logOut();
        if (AccessToken.getCurrentAccessToken() != null) {
            getUserFacebookData(AccessToken.getCurrentAccessToken());
        }
    }

    private final FacebookCallback<LoginResult> fbCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            getUserFacebookData(loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private void getUserFacebookData(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        try {
                            account = new Account();
                            account.setUserName(object.getString("email"));
                            account.setPassword("");
                            account.setEmail(object.getString("email"));
                            account.setPhone("");
                            account.setFull_name(object.getString("first_name") + " " + object.getString("first_name"));
                            account.setAddress("");
                            register(account);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,first_name,last_name,email,picture.type(large),gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 android.content.Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    private void register(Account acc) {
        acc.setType(Account.LOGIN_TYPE_SOCIAL);
        String data = createAccountJson(acc);
        Logger.e("ACCOUNT INFO", "ACCOUNT INFO : " + data);
        ModelManager.register(getCurrentActivity(), data, true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        String json = (String) object;
                        if (ParserUtility.isSuccess(json)) {
                            Account account = ParserUtility.parseAccount(json);
                            GlobalValue.myAccount = account;
                            GlobalValue.myAccount.setType(Account.LOGIN_TYPE_SOCIAL);
                            new MySharedPreferences(getCurrentActivity()).setCacheUserInfo(ParserUtility.convertAccountToJsonString(GlobalValue.myAccount));
                            MainUserActivity activity = (MainUserActivity) getCurrentActivity();
                            activity.refreshContent();
                        } else {
                            Toast.makeText(self, getErrorMessage(json), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String createAccountJson(Account acc) {
        JSONObject json = new JSONObject();
        try {
            json.put(WebServiceConfig.KEY_ACCOUNT_USER_NAME, acc.getUserName());
            json.put(WebServiceConfig.KEY_ACCOUNT_PASSWORD, acc.getPassword());
            json.put(WebServiceConfig.KEY_ACCOUNT_EMAIL, acc.getEmail());
            json.put(WebServiceConfig.KEY_ACCOUNT_FULL_NAME, acc.getFull_name());
            json.put(WebServiceConfig.KEY_ACCOUNT_PHONE, acc.getPhone());
            json.put(WebServiceConfig.KEY_ACCOUNT_ADDRESS, acc.getAddress());
            json.put("type", acc.getType());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json.toString();
    }

    private String getErrorMessage(String strjson) {
        JSONObject json = null;
        String message = "";
        try {
            json = new JSONObject(strjson);
            message = json.getString(WebServiceConfig.KEY_MESSAGE);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            message = getString(R.string.message_server_error);
        }

        return message;
    }
}
