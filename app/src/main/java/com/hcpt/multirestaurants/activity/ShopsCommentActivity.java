package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.CommentAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Comment;
import com.hcpt.multirestaurants.object.Shop;

public class ShopsCommentActivity extends BaseActivity implements
		OnClickListener {

	private Shop shop;
	private TextView lblShopName, lblPhoneNumber;
	private LinearLayout btnPhoneNumber;
	private ImageView btnBack;
	private int shopId = -1;
	public static BaseActivity self;

	private ListView mLsvComment;
	private ArrayList<Comment> mArrComment;
	private CommentAdapter mCommentAdapter;

	private Button mBtnLoadmore;
	private int page = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_shops_comment);
		initUI();
		initData();
	}

	private void initUI() {
		lblShopName = (TextView) findViewById(R.id.lblShopName);
		btnPhoneNumber = (LinearLayout) findViewById(R.id.btnPhone);
		btnBack = (ImageView) findViewById(R.id.btnBack);
		lblPhoneNumber = (TextView) findViewById(R.id.lblPhoneNumber);
		mLsvComment = (ListView) findViewById(R.id.lsv_comment);
		// Add loadmore button
		mBtnLoadmore = (Button) getLayoutInflater().inflate(
				R.layout.loadmore_button, null);
		mLsvComment.addFooterView(mBtnLoadmore);

		// Should call this method at the end of declaring UI.
		initControl();
	}

	private void initControl() {
		btnPhoneNumber.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		mBtnLoadmore.setOnClickListener(this);
	}

	private void initData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			if (b.containsKey(GlobalValue.KEY_SHOP_ID)) {
				shopId = b.getInt(GlobalValue.KEY_SHOP_ID);
			}
		}
		if (shopId != -1) {
			ModelManager.getShopById(self, shopId, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(Object object) {

							String json = (String) object;
							shop = ParserUtility.parseShop(json);
							if (shop != null) {
								lblShopName.setText(shop.getShopName());
								lblPhoneNumber.setText(shop.getPhone());
							}

						}

						@Override
						public void onError(VolleyError error) {
							// TODO Auto-generated method stub
							Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
						}
					});

			// Get shop comments
			getComments(shopId + "");
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnPhoneNumber) {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + shop.getPhone()));
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					Toast.makeText(self, "Call Phone Permission is disable! Please Enable to use this feature.", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			startActivity(callIntent);
		} else if (v == mBtnLoadmore) {
			loadMore();
		} else if (v == btnBack) {
			onBackPressed();
			return;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		self = null;
	}

	private void getComments(String id) {
		ModelManager.getShopsComments(self, id, page,false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(Object object) {
						// TODO Auto-generated method stub
						String json = (String) object;
						mArrComment = ParserUtility.parseComments(json);

						// Show/hide 'Load more' button
						if (mArrComment.size() % GlobalValue.COMMENT_PAGE == 0) {
//							mBtnLoadmore.setVisibility(View.VISIBLE);
						} else {
							mLsvComment.removeFooterView(mBtnLoadmore);
						}

						if (mArrComment != null && mArrComment.size() > 0) {
							mCommentAdapter = new CommentAdapter(self,
									mArrComment,true);
							mCommentAdapter.commentOnly = true;
							mLsvComment.setAdapter(mCommentAdapter);
						}
					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void loadMore() {
		page++;
		ModelManager.getShopsComments(self, shopId + "", page,false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(Object object) {
						// TODO Auto-generated method stub
						String json = (String) object;
						ArrayList<Comment> arrTemp = ParserUtility
								.parseComments(json);

						// Show/hide 'Load more' button
						if (arrTemp != null
								&& arrTemp.size() % GlobalValue.COMMENT_PAGE == 0) {
//							mBtnLoadmore.setVisibility(View.VISIBLE);
						} else {
							mLsvComment.removeFooterView(mBtnLoadmore);
						}

						if (arrTemp != null && arrTemp.size() > 0) {
							mArrComment.addAll(arrTemp);
							mCommentAdapter.notifyDataSetChanged();
						} else {
							Toast.makeText(self,
									"You had all of comments already.",
									Toast.LENGTH_SHORT).show();
						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
	}
}
