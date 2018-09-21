package com.hcpt.multirestaurants.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.FoodOfShopOrderAdapter;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.object.Menu;

@SuppressLint("NewApi")
public class DetailFoodOfShopOrderActivity extends BaseActivity implements
		OnClickListener {

	private ImageView btnBack;
	private ListView lsvShops;
	private TextView lblSum, lblVAT, lblShip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_shop_order);
		initUI();
		initUIControls();
		updateOrderValue();

	}

	private void initUI() {
		btnBack = (ImageView) findViewById(R.id.btnBack);
		lsvShops = (ListView) findViewById(R.id.lsvShop);
		lblSum = (TextView) findViewById(R.id.lblSum);
		lblShip = (TextView) findViewById(R.id.lblShip);
		lblVAT = (TextView) findViewById(R.id.lblVAT);
	}

	private void initUIControls() {
		ArrayList<Menu> arrShopOrders = GlobalValue.currentShopOrder.getArrFoods();
		FoodOfShopOrderAdapter shopAdapter = new FoodOfShopOrderAdapter(self, arrShopOrders);
		lsvShops.setAdapter(shopAdapter);
		btnBack.setOnClickListener(this);
	}

	private void updateOrderValue() {
		double totalVAT = 0, totalShip = 0, totalPrice = 0;

		totalPrice = GlobalValue.currentShopOrder.getGrandTotal();
		totalShip = GlobalValue.currentShopOrder.getShipping();
		totalVAT = GlobalValue.currentShopOrder.getVAT();

		lblVAT.setText(getString(R.string.vat)+" " + getString(R.string.currency)
				+ String.format("%.1f", totalVAT));
		lblShip.setText(getString(R.string.ship)+" " + getString(R.string.currency)
				+ String.format("%.1f", totalShip));
		lblSum.setText(getString(R.string.currency)
				+ String.format("%.1f", totalPrice));
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnBack) {
			onBackPressed();
		}
	}
}
