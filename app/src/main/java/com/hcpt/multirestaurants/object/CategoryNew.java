package com.hcpt.multirestaurants.object;

import java.util.ArrayList;

public class CategoryNew {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private ArrayList<Category> categoryArrayList;
    private ArrayList<Menu> menuArrayList;

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public ArrayList<Menu> getMenuArrayList() {
        return menuArrayList;
    }

    public void setMenuArrayList(ArrayList<Menu> menuArrayList) {
        this.menuArrayList = menuArrayList;
    }
}
