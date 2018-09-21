package com.hcpt.multirestaurants.object;

import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class Account {

    public static final String ROLE_USER = "0";
    public static final String ROLE_SHOP_OWNER = "1";
    public static final String ROLE_ADMIN = "2";
    public static final String ROLE_CHEF = "3";
    public static final String ROLE_DELIVERY = "4";
    public static final String ROLE_MODERATOR = "5";
    public static final String LOGIN_TYPE_NORMAL = "0";
    public static final String LOGIN_TYPE_SOCIAL = "1";

    private String id = "";
    private String userName = "";
    private String email = "";
    private String full_name = "";
    private String phone = "";
    private String address = "";
    private String password = "";
    private String role = "0";
    private boolean isRequestShopOwner = false;
    private String redirectLink = "";
    private String type = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public boolean isUser() {
        return redirectLink.isEmpty();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isRequestShopOwner() {
        return isRequestShopOwner;
    }

    public void setIsRequestShopOwner(boolean isRequestShopOwner) {
        this.isRequestShopOwner = isRequestShopOwner;
    }

    public boolean isUserRole() {
        return role == ROLE_USER;
    }

    public boolean isSocialLogin() {
        return type.equals(LOGIN_TYPE_SOCIAL);
    }

}
