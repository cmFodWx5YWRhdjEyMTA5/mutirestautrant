package com.hcpt.multirestaurants.object;

import android.graphics.Bitmap;

import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.config.Constant;

import java.util.ArrayList;

public class Shop {
    private int shopId;
    private String shopName;
    private int cityId;
    private String address;
    private String image;
    private String description;
    private String phone;
    private Double longitude;
    private Double latitude;
    private OpenHour openHour;// open hour of today
    private ArrayList<OpenHour> arrOpenHour;// all open hour in week
    private ArrayList<Banner> arrBanner;
    private ArrayList<Offer> arrOffer;
    private int status;
    private Bitmap bmImage;
    private boolean isOpen = true;

    private String isVerified;
    private String isFeatured;
    private String facebook;
    private String twitter;
    private String email;
    private String live_chat;

    private double shopVAT = 0;
    private double deliveryPrice = 0;
    private double minPriceForDelivery = 0;
    // rate
    private float rateValue = 0;
    private int rateNumber = 0;
    // process order
    private ArrayList<Menu> arrOrderFoods;
    private double totalPrice = 0;
    private int currentItemsNumber = 0;

    public Shop() {
        arrOrderFoods = new ArrayList<Menu>();
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public int getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(int rateNumber) {
        this.rateNumber = rateNumber;
    }

    public ArrayList<Menu> getArrOrderFoods() {
        return arrOrderFoods;
    }

    public void setArrOrderFoods(ArrayList<Menu> arrOrderFoods) {
        this.arrOrderFoods = arrOrderFoods;
    }

    public double getShopVAT() {
        return shopVAT;
    }

    public void setShopVAT(double shopVAT) {
        this.shopVAT = shopVAT;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getMinPriceForDelivery() {
        return minPriceForDelivery;
    }

    public void setMinPriceForDelivery(double minPriceForDelivery) {
        this.minPriceForDelivery = minPriceForDelivery;
    }

    public int getShopId() {
        return shopId;
    }

    public ArrayList<OpenHour> getArrOpenHour() {
        return arrOpenHour;
    }

    public void setArrOpenHour(ArrayList<OpenHour> arrOpenHour) {
        this.arrOpenHour = arrOpenHour;
    }

    public ArrayList<Banner> getArrBanner() {
        return arrBanner;
    }

    public void setArrBanner(ArrayList<Banner> arrBanner) {
        this.arrBanner = arrBanner;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public OpenHour getOpenHour() {
        return openHour;
    }

    public void setOpenHour(OpenHour openHour) {
        this.openHour = openHour;
    }

    public ArrayList<Offer> getArrOffer() {
        return arrOffer;
    }

    public void setArrOffer(ArrayList<Offer> arrOffer) {
        this.arrOffer = arrOffer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Bitmap getBmImage() {
        return bmImage;
    }

    public void setBmImage(Bitmap bmImage) {
        this.bmImage = bmImage;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void addFoodOrder(Menu food) {
        arrOrderFoods.add(food);
        updateTotalPriceWithoutVATandDelivery();
    }

    public void removeFoodOrder(Menu food) {
        int size = arrOrderFoods.size();
        Menu food1 = null;
        for (int i = size - 1; i >= 0; i--) {
            food1 = arrOrderFoods.get(i);
            if (food1.getId() == food.getId()) {
                arrOrderFoods.remove(i);
                break;
            }
        }
        updateTotalPriceWithoutVATandDelivery();
    }

    public void updateQuantityOfFood(int indexFood, int quantity) {
        arrOrderFoods.get(indexFood).setOrderNumber(quantity);
        updateTotalPriceWithoutVATandDelivery();
    }

    public void updateFood(int food, ArrayList<Relish> relish) {
        arrOrderFoods.get(food).setRelishArrayList(relish);
        updateTotalPriceWithoutVATandDelivery();
    }

    public void updateNote(int food, String addnote) {
        arrOrderFoods.get(food).setAddnote(addnote);
        updateTotalPriceWithoutVATandDelivery();
    }


    public double getCurrentTotalVAT() {
        return totalPrice * shopVAT / 100;
    }

    public int getNumberItems() {
        return currentItemsNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getcurrentShipping() {
        if (totalPrice > minPriceForDelivery)
            return 0;
        else
            return deliveryPrice;
    }

    public void updateTotalPriceWithoutVATandDelivery() {
        totalPrice = 0;
        currentItemsNumber = 0;
        for (Menu food : arrOrderFoods) {
            Double priceTopping = 0.0;
            for (Relish relish : food.getRelishArrayList()) {
                if (relish.getRelishOptionChoise() != null && !relish.getRelishOptionChoise().getName().equals(Constant.NONE)) {
                        Debug.e(relish.getRelishOptionChoise().getName() + "HUY");
                        priceTopping = priceTopping + relish.getRelishPrice();

                }
            }
            totalPrice += ((food.getPrice() + priceTopping - (food.getPrice()
                    * food.getPercentDiscount() / 100)))
                    * food.getOrderNumber();
            currentItemsNumber += food.getOrderNumber();
        }
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLive_chat() {
        return live_chat;
    }

    public void setLive_chat(String live_chat) {
        this.live_chat = live_chat;
    }

}