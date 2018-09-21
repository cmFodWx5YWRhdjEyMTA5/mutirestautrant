package com.hcpt.multirestaurants.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.util.NetworkUtil;

public class AddReviewActivity extends BaseActivity implements OnClickListener {

    private ImageView mBtnBack;
    private RatingBar mRtb;
    private EditText mTxtName, mTxtReview;
    private TextView mBtnAdd, lblNote;

    private String mShopId = "";
    private String mFoodId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_review);
        initUI();
        getExtraValues();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mBtnBack) {
            onBackPressed();
        } else if (v == mBtnAdd) {
            if (!NetworkUtil.checkNetworkAvailable(this)) {
                Toast.makeText(this, R.string.message_network_is_unavailable, Toast.LENGTH_LONG).show();
            } else {
                addReview();
            }
        }
    }

    private void getExtraValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(GlobalValue.KEY_SHOP_ID)) {
                mShopId = bundle.getInt(GlobalValue.KEY_SHOP_ID) + "";
            }

            if (bundle.containsKey(GlobalValue.KEY_FOOD_ID)) {
                mFoodId = bundle.getInt(GlobalValue.KEY_FOOD_ID) + "";
            }
        }

        if (GlobalValue.myAccount != null) {
            mTxtName.setText(GlobalValue.myAccount.getUserName());
            lblNote.setVisibility(View.GONE);
        } else {
            lblNote.setVisibility(View.VISIBLE);
        }
    }

    private void addReview() {

        String mUser;
        String mRate;
        String mContent;
        // Get values.

        mUser = mTxtName.getText().toString().trim();
        mRate = (mRtb.getProgress() * 2) + "";
        mContent = mTxtReview.getText().toString().trim();

        // Call add api.
        if (mUser.isEmpty()) {
            Toast.makeText(self,
                    getResources().getString(R.string.please_enter_your_name),
                    Toast.LENGTH_SHORT).show();
            mTxtName.requestFocus();
        } else if (mContent.isEmpty()) {
            Toast.makeText(
                    self,
                    getResources().getString(
                            R.string.please_leave_your_messages),
                    Toast.LENGTH_SHORT).show();
            mTxtReview.requestFocus();
        } else {
            ModelManager.addFoodReview(self, mShopId, mFoodId, mRate, mUser,
                    mContent, false, new ModelManagerListener() {

                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(Object object) {
                            try {
                                String json = (String) object;
                                if (!json.isEmpty()) {
                                    JSONObject obj = new JSONObject(json);
                                    if (obj.getString(WebServiceConfig.KEY_STATUS).equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                                        Toast.makeText(
                                                self,
                                                R.string.message_adding_new_comment_successfully,
                                                Toast.LENGTH_SHORT).show();
                                        AddReviewActivity.this.finish();
                                    } else {
                                        Toast.makeText(
                                                self,
                                                R.string.error_adding_new_comment,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(
                                            self,
                                            R.string.error_adding_new_comment,
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void initUIControls() {
        // TODO Auto-generated method stub
        mBtnBack.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }

    private void initUI() {
        // TODO Auto-generated method stub
        mBtnBack = (ImageView) findViewById(R.id.btnBack);
        mRtb = (RatingBar) findViewById(R.id.rtbRating);
        mTxtName = (EditText) findViewById(R.id.txtFullname);
        mTxtReview = (EditText) findViewById(R.id.txtReview);
        mBtnAdd = (TextView) findViewById(R.id.btnAdd);
        lblNote = (TextView) findViewById(R.id.lblNote);

        // Should call this method at the end of declaring UI.
        initUIControls();
    }
}
