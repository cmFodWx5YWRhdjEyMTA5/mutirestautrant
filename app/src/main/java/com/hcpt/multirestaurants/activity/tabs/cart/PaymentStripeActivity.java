package com.hcpt.multirestaurants.activity.tabs.cart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.config.WebServiceConfig;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentStripeActivity extends Activity {
    TextView btnSubmit;
    EditText cardNumberField, monthField, yearField, cvcField;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment);
        btnSubmit = (TextView) findViewById(R.id.submitButton);
        cardNumberField = (EditText) findViewById(R.id.cardNumber);
        monthField = (EditText) findViewById(R.id.month);
        yearField = (EditText) findViewById(R.id.year);
        cvcField = (EditText) findViewById(R.id.cvc);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard();
            }
        });
    }

    public void submitCard() {
        // TODO: replace with your own test key
        final String publishableApiKey = this.getString(R.string.stripe_publishable_key);
        Card card = new Card(cardNumberField.getText().toString(),
                Integer.valueOf(monthField.getText().toString()),
                Integer.valueOf(yearField.getText().toString()),
                cvcField.getText().toString());
        if (card.validateCard() && card.validateCVC() && card.validateExpMonth() && card.validateExpYear()) {
            Stripe stripe = new Stripe(getApplicationContext());
            stripe.createToken(card, publishableApiKey, new TokenCallback() {
                public void onSuccess(final Token token) {
                    // TODO: Send Token information to your backend to initiate a charge
                    Log.e("token", "token:" + token.getId());
                    String data = createOfferJson(GlobalValue.arrMyMenuShop, GlobalValue.myAccount.getFull_name(), GlobalValue.myAccount.getPhone(), GlobalValue.myAccount.getEmail(), GlobalValue.myAccount.getAddress());
                    sendListOrder(data, 3, token.getId());
                }

                public void onError(Exception error) {
                    Log.d("Stripe", error.getLocalizedMessage());
                }
            });
        } else {
            Toast.makeText(this, "Infomation invalid", Toast.LENGTH_SHORT).show();
        }


    }

    private String createOfferJson(ArrayList<Shop> arrShops, String name, String phone, String email, String address) {
        JSONObject jsonOrder = new JSONObject();
        JSONArray jsonFoods = new JSONArray();
        JSONObject jsonFood = null;
        Double price = null;

        try {
            for (Shop shop : arrShops) {

                for (Menu menu : shop.getArrOrderFoods()) {

                    price = menu.getPrice()
                            - (menu.getPrice() * menu.getPercentDiscount() / 100);

                    jsonFood = new JSONObject();
                    jsonFood.put(WebServiceConfig.KEY_ORDER_SHOP,
                            menu.getShopId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_FOOD, menu.getId());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_NUMBER_FOOD,
                            menu.getOrderNumber());
                    jsonFood.put(WebServiceConfig.KEY_ORDER_PRICE_FOOD,
                            round(price, 2));
                    jsonFoods.put(jsonFood);

                }
            }

            jsonOrder.put(WebServiceConfig.KEY_ORDER_ACCOUT_ID,
                    GlobalValue.myAccount.getId());
            jsonOrder.put(WebServiceConfig.KEY_ORDER_NAME, name);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_PHONE, phone);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_EMAIL, email);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_ADDRESS, address);
            jsonOrder.put(WebServiceConfig.KEY_ORDER_ITEM, jsonFoods);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonOrder.toString();
    }

    private void sendListOrder(String data, final int paymentMethod, String token) {
        ModelManager.sendListOrderStripe(PaymentStripeActivity.this, data, paymentMethod,
                true, token, new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String strJson = (String) object;
                        try {
                            JSONObject json = new JSONObject(strJson);
                            if (json.getString(WebServiceConfig.KEY_STATUS)
                                    .equals(WebServiceConfig.KEY_STATUS_SUCCESS)) {
                                CustomToast.showCustomAlert(
                                        PaymentStripeActivity.this,
                                        getString(
                                                R.string.message_success),
                                        Toast.LENGTH_SHORT);
                                //autoback
                                GlobalValue.autoBack = true;
                                // clear list
                                GlobalValue.arrMyMenuShop.clear();
                                // go back list shop cart
                                finish();
                            } else {
                                CustomToast.showCustomAlert(
                                        PaymentStripeActivity.this,
                                        json.getString(WebServiceConfig.KEY_MESSAGE),
                                        Toast.LENGTH_SHORT);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(PaymentStripeActivity.this, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static double round(double number, int digit) {
        if (digit > 0) {
            int temp = 1, i;
            for (i = 0; i < digit; i++)
                temp = temp * 10;
            number = number * temp;
            number = Math.round(number);
            number = number / temp;
            return number;
        } else
            return 0.0;
    }
}