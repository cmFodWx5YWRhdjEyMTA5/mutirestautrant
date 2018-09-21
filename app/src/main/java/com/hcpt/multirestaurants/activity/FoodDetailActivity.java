package com.hcpt.multirestaurants.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.BaseActivity;
import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.adapter.AdapterOptionTopping;
import com.hcpt.multirestaurants.adapter.AdapterRelishOptionNew;
import com.hcpt.multirestaurants.adapter.CommentAdapter;
import com.hcpt.multirestaurants.config.Constant;
import com.hcpt.multirestaurants.config.GlobalValue;
import com.hcpt.multirestaurants.modelmanager.ErrorNetworkHandler;
import com.hcpt.multirestaurants.modelmanager.ModelManager;
import com.hcpt.multirestaurants.modelmanager.ModelManagerListener;
import com.hcpt.multirestaurants.network.ParserUtility;
import com.hcpt.multirestaurants.object.Category;
import com.hcpt.multirestaurants.object.Comment;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.object.Relish;
import com.hcpt.multirestaurants.object.RelishOption;
import com.hcpt.multirestaurants.object.Shop;
import com.hcpt.multirestaurants.util.CustomToast;

import java.util.ArrayList;

import static com.hcpt.multirestaurants.activity.ListCategoryActivity.self;

public class FoodDetailActivity extends BaseActivity implements OnClickListener, AdapterOptionTopping.OnCheckListerner, AdapterOptionTopping.OnSetDataListerner {

    private TextView lblShopName, lblMenuName;
    private ImageView btnAddtoMenu;
    private TextView btnAddReview;
    private ImageView btnBack;
    private int foodId = -1;
    private Menu food;
    private Shop shop;
    private Category category;
    private String shopName = "", categoryName = "";
    public static BaseActivity self;
    private boolean isFastSearch = false;
    private boolean checkclear = false;

    private ListView mLsvComment;
    private ArrayList<Comment> mArrComment;
    private CommentAdapter mCommentAdapter;
    private TextView mLblCountComment;
    private Button mBtnLoadmore;
    private int page = 1;
    private int pageTotal = 0;
    private Dialog dialogAddTopping;
    private Dialog dialogListToppings;
    private Dialog mDialog;
    private AdapterOptionTopping adapterOptionTopping;
    private ArrayList<Relish> relishArrayList;
    private ArrayList<RelishOption> relishOptions;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        self = this;
        initUI();
        initControl();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (foodId != -1) {
            // Get food detail
            ModelManager.getFoodById(self, foodId, true,
                    new ModelManagerListener() {

                        @Override
                        public void onSuccess(Object object) {
                            // TODO Auto-generated method stub
                            String json = (String) object;
                            food = ParserUtility.parseFood(json);
                            setDataToUI(food);
                            // Get food comments
                            getComments(foodId + "", food);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            // TODO Auto-generated method stub
                            Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void initUI() {
        btnAddtoMenu = (ImageView) findViewById(R.id.btnAddToMenu);
        btnAddReview = (TextView) findViewById(R.id.btnAddReview);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        lblShopName = (TextView) findViewById(R.id.lblShopName);
        lblMenuName = (TextView) findViewById(R.id.lblMenuName);
        mLsvComment = (ListView) findViewById(R.id.lsv_comment);
        // Add loadmore button
        mBtnLoadmore = (Button) getLayoutInflater().inflate(
                R.layout.loadmore_button, null);
        mLblCountComment = (TextView) findViewById(R.id.lblReviewNumber);
    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        btnAddtoMenu.setOnClickListener(this);
        lblShopName.setOnClickListener(this);
        btnAddReview.setOnClickListener(this);
        mBtnLoadmore.setOnClickListener(this);
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(GlobalValue.KEY_FOOD_ID)) {
                foodId = b.getInt(GlobalValue.KEY_FOOD_ID);
            }
            if (b.containsKey(GlobalValue.KEY_SHOP_NAME)) {
                shopName = b.getString(GlobalValue.KEY_SHOP_NAME);
                lblShopName.setText(shopName);
            }

            if (b.containsKey(GlobalValue.KEY_CATEGORY_NAME)) {
                categoryName = b.getString(GlobalValue.KEY_CATEGORY_NAME);
                lblMenuName.setText(categoryName);
            }
            if (b.containsKey(GlobalValue.KEY_NAVIGATE_TYPE)) {
                isFastSearch = true;
            }
        }
        // init list comments
        mArrComment = new ArrayList<>();
    }


    private void setDataToUI(Menu mfood) {
        mLblCountComment.setText(food.getRateNumber() + " comment(s)");
        ModelManager.getShopById(self, mfood.getShopId(), true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        shop = ParserUtility.parseShop(json);
                        if (shop != null && shopName.equals(""))
                            lblShopName.setText(shop.getShopName());
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });

        ModelManager.getCatgoryById(self, mfood.getCategoryId(), true,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        category = ParserUtility.parseCategory(json);
                        if (category != null && categoryName.equals(""))
                            lblMenuName.setText(category.getId() + "");
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnBack) {
            onBackPressed();
        } else if (v == btnAddtoMenu) {
            Debug.e("CLICK vào đây r");
            showDialogAddItem(food);
        } else if (v == lblShopName) {
            gotoShopDetail(shop.getShopId());
        } else if (v == mBtnLoadmore) {
            loadMore();
        } else if (v == btnAddReview) {
            Bundle bundle = new Bundle();
            bundle.putInt(GlobalValue.KEY_SHOP_ID, shop.getShopId());
            bundle.putInt(GlobalValue.KEY_FOOD_ID, foodId);
            gotoActivity(self, AddReviewActivity.class, bundle);
        }
    }

    private void loadMore() {
        page++;
        ModelManager.getFoodsComments(self, foodId + "", page, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        ArrayList<Comment> arrTemp = ParserUtility
                                .parseComments(json);

                        // Show/hide 'Load more' button
                        if (page >= pageTotal) {
                            mLsvComment.removeFooterView(mBtnLoadmore);
                        }

                        if (arrTemp.size() > 0) {
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
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void getComments(String id, final Menu mfood) {
        page = 1;
        ModelManager.getFoodsComments(self, id, page, false,
                new ModelManagerListener() {

                    @Override
                    public void onSuccess(Object object) {
                        // TODO Auto-generated method stub
                        String json = (String) object;
                        mArrComment.clear();
                        mArrComment.addAll(ParserUtility.parseComments(json));
                        pageTotal = ParserUtility.getAllPageCount(json);
                        // Show/hide 'Load more' button
                        if (page >= pageTotal) {
                            mLsvComment.removeFooterView(mBtnLoadmore);
                        } else {
                            mLsvComment.addFooterView(mBtnLoadmore);
                        }

                        if (mCommentAdapter == null) {
                            mCommentAdapter = new CommentAdapter(self,
                                    mArrComment, false);
                            mCommentAdapter.food = mfood;
                            mCommentAdapter.mfood = mfood;
                            mLsvComment.setAdapter(mCommentAdapter);
                        } else {
                            mCommentAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(self, ErrorNetworkHandler.processError(error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Made by Huy.
     * show dialogAddtopping
     */
    private void showDialogAddItem(final Menu food) {
        dialogAddTopping = new Dialog(self);
        dialogAddTopping.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddTopping.setContentView(R.layout.dialog_add_item);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogAddTopping.getWindow().getAttributes());
        lp.width = (int) (4.5 * (displaymetrics.widthPixels / 5));
        dialogAddTopping.getWindow().setAttributes(lp);
        dialogAddTopping.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAddTopping.setCancelable(false);

        ImageView imgFood = dialogAddTopping.findViewById(R.id.imgFood);
        TextView lblFoodName = dialogAddTopping.findViewById(R.id.lblFoodName);
        TextView lblCategory = dialogAddTopping.findViewById(R.id.lblCategory);
        TextView lblPrice = dialogAddTopping.findViewById(R.id.lblPrice);
        TextView txtNumberquantity = dialogAddTopping.findViewById(R.id.txtNumberquantity);
        TextView lblToppingPrice = dialogAddTopping.findViewById(R.id.lblToppingPrice);
        TextView btnAddTopping = dialogAddTopping.findViewById(R.id.btnAddTopping);
        TextView btnAddIntroduction = dialogAddTopping.findViewById(R.id.btnAddIntroduction);
        TextView lblSave = dialogAddTopping.findViewById(R.id.lblSave);
        TextView lblCancel = dialogAddTopping.findViewById(R.id.lblCancel);

        Glide.with(this).load(food.getImage()).into(imgFood);
        lblFoodName.setText(food.getName());
        lblCategory.setText(food.getCategoryProduct());
        lblPrice.setText("$" + food.getPrice());
        food.setOrderNumber(1);
        lblToppingPrice.setText("");

        btnAddTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Debug.e("CLICK ADD ITERM TO CART");
                showDialogListTopping(food);

            }
        });
        btnAddIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstructionDialog(food);

            }
        });
        txtNumberquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListQuantities(food);
            }
        });
        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food.getOrderNumber() == 0) {
                    food.setOrderNumber(1);
                }
                addToMyMenu(shop);
                dialogAddTopping.dismiss();
            }
        });
        lblCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddTopping.dismiss();
            }
        });

        dialogAddTopping.show();
    }

    private void addToMyMenu(Shop shop) {
        for (int i = 0; i < GlobalValue.arrMyMenuShop.size(); i++) {
            if (shop.getShopId() == GlobalValue.arrMyMenuShop.get(i).getShopId()) {
                GlobalValue.arrMyMenuShop.get(i).addFoodOrder(food);
                CustomToast.showCustomAlert(self, "add to card", Toast.LENGTH_SHORT);
                finish();
                return;
            }
        }
        shop.addFoodOrder(food);
        Debug.e(food.getTotalPrices()+"#" + food.getPrice() +"#"+ food.getTotalPrice() + "");
        GlobalValue.arrMyMenuShop.add(shop);
        CustomToast.showCustomAlert(self, "add to card", Toast.LENGTH_SHORT);
        finish();
    }

    private void showListQuantities(final Menu food) {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setquantity_new);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 4 * (displaymetrics.widthPixels / 5);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.setCancelable(false);

        ListView lsvCook = (ListView) dialog.findViewById(R.id.lsvCookMethod);
        Integer[] numberlist = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(self,
                android.R.layout.simple_list_item_single_choice, numberlist);
        lsvCook.setAdapter(adapter);
        lsvCook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String a = 1 + position + "";
                food.setOrderNumber(Integer.parseInt(a));
                updateContent(food);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //add note
    private void showInstructionDialog(final Menu food) {
        mDialog = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_instruction);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = 4 * (displaymetrics.widthPixels / 5);
        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        mDialog.setCancelable(false);

        // Get view
        final EditText edtInsTruction = (EditText) mDialog.findViewById(R.id.edtInsTructions);
        TextView btnCancel = (TextView) mDialog.findViewById(R.id.btnCancel);
        TextView btnSave = (TextView) mDialog.findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (edtInsTruction.getText().toString().length() > 0) {
                    food.setAddnote(edtInsTruction.getText().toString());
                }
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void updateContent(final Menu food) {
        Debug.e(String.valueOf(food.getOrderNumber()));
        TextView txtNumberquantity = dialogAddTopping.findViewById(R.id.txtNumberquantity);
        TextView tvPrice = dialogAddTopping.findViewById(R.id.lblPrice);
        TextView tvnameTopping = dialogAddTopping.findViewById(R.id.lblToppingPrice);
        txtNumberquantity.setText(food.getOrderNumber() + "");
        double price = 0;
        StringBuilder listTopping = new StringBuilder();
        for (int i = 0; i < food.getRelishArrayList().size(); i++) {
            if (food.getRelishArrayList().get(i).getRelishOptionChoise() != null && !food.getRelishArrayList().get(i).getRelishOptionChoise().getName().equals(Constant.NONE)) {
                price = price + food.getRelishArrayList().get(i).getRelishPrice();
                listTopping.append(", ").append(food.getRelishArrayList().get(i).getRelishName() + ":\t" + food.getRelishArrayList().get(i).getRelishOptionChoise().getName());
            }
        }
        if (!listTopping.equals(""))
            tvnameTopping.setText(listTopping.toString());
        tvPrice.setText("$" + food.getPrice() + "+" + "(" + "$" +
                ((food.getPrice() + price) * food.getOrderNumber() - food.getPrice()) + ")");

    }

    private void showDialogListTopping(final Menu food) {
        dialogListToppings = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
        dialogListToppings.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogListToppings.setContentView(R.layout.dialog_option);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogListToppings.getWindow().getAttributes());
        lp.width = 18 * (displaymetrics.widthPixels / 20);
        dialogListToppings.getWindow().setAttributes(lp);
        dialogListToppings.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogListToppings.setCancelable(false);
        RecyclerView lvToppings = dialogListToppings.findViewById(R.id.rclToppings);
        TextView btnCancle = dialogListToppings.findViewById(R.id.btnCancel);
        TextView btnSave = dialogListToppings.findViewById(R.id.btnSave);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvToppings.setLayoutManager(layoutManager);
        relishArrayList = food.getRelishArrayList();
        adapterOptionTopping = new AdapterOptionTopping(self, relishArrayList, this, this);
        lvToppings.setAdapter(adapterOptionTopping);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListToppings.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListToppings.dismiss();
                updateContent(food);
            }
        });
        dialogListToppings.show();
    }

    @Override
    public void onCheckChange(boolean isChecked, int position) {
        relishArrayList.get(position).setChecker(isChecked);
//        relishArrayList.get(position).setOptionArrayList(relishOptions);
    }

    @Override
    public void onCheckChange(Relish relish, int position, int p) {
        relishArrayList.set(position, relish);
        food.setRelishArrayList(relishArrayList);
//        adapterOptionTopping.notifyDataSetChanged();
    }
}
