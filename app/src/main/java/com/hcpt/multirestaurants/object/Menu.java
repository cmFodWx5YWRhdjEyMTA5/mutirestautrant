package com.hcpt.multirestaurants.object;

import com.hcpt.multirestaurants.Debug;
import com.hcpt.multirestaurants.config.Constant;

import java.util.ArrayList;

public class Menu {

    private int id;
    private String name, localName;
    private String code;
    private int shopId;
    private int categoryId;
    private double price, percentDiscount;
    private String image, vat, ship;
    private String description;
    private int orderNumber;
    private float rateValue = 0;
    private int rateNumber = 0;
    private String category;
    private String shop_address;
    private String shop_phone;
    private String categoryProduct;
    private ArrayList<Relish> relishArrayList;
    private String addnote = "";

    public String getAddnote() {
        return addnote;
    }

    public void setAddnote(String addnote) {
        this.addnote = addnote;
    }

    public ArrayList<Relish> getRelishArrayList() {
        return relishArrayList;
    }

    public void setRelishArrayList(ArrayList<Relish> relishArrayList) {
        this.relishArrayList = relishArrayList;
    }

    public String getCategoryProduct() {
        return categoryProduct;
    }

    public void setCategoryProduct(String categoryProduct) {
        this.categoryProduct = categoryProduct;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public double getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(double percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalPrice() {
        return orderNumber * price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getCurrentPrice() {
        return price - (price * percentDiscount / 100);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    public double getTotalPrices() {
        double priceTopping = 0;
        for (int i = 0; i < relishArrayList.size(); i++) {
            if (relishArrayList.get(i).getRelishOptionChoise() != null && !relishArrayList.get(i).getRelishOptionChoise().equals(Constant.NONE)) {
                Debug.e(relishArrayList.get(i).getRelishOptionChoise().getName());
                if (!relishArrayList.get(i).getRelishOptionChoise().getName().equals("none")) {
                    Debug.e(relishArrayList.get(i).getRelishOptionChoise().getName()+"HUY");
                    priceTopping = priceTopping + relishArrayList.get(i).getRelishPrice();
                }
            }
        }
        return (price + priceTopping - ((price + priceTopping) * percentDiscount) / 100);
    }
}
