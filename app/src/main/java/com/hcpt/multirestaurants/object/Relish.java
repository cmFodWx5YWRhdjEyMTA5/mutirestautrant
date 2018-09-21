package com.hcpt.multirestaurants.object;

import java.util.ArrayList;

public class Relish {
    private String relishId;
    private String relishName;
    private Double relishPrice;
    private ArrayList<RelishOption> optionArrayList;
    private RelishOption relishOptionChoise;

    public RelishOption getRelishOptionChoise() {
        return relishOptionChoise;
    }

    public void setRelishOptionChoise(RelishOption relishOptionChoise) {
        this.relishOptionChoise = relishOptionChoise;
    }

    public ArrayList<RelishOption> getOptionArrayList() {
        return optionArrayList;
    }

    public void setOptionArrayList(ArrayList<RelishOption> optionArrayList) {
        this.optionArrayList = optionArrayList;
    }

    private boolean isChecker = false;

    public boolean getChecker() {
        return isChecker;
    }

    public void setChecker(boolean checker) {
        isChecker = checker;
    }

    public String getRelishId() {
        return relishId;
    }

    public void setRelishId(String relishId) {
        this.relishId = relishId;
    }

    public String getRelishName() {
        return relishName;
    }

    public void setRelishName(String relishName) {
        this.relishName = relishName;
    }

    public Double getRelishPrice() {
        return relishPrice;
    }

    public void setRelishPrice(Double relishPrice) {
        this.relishPrice = relishPrice;
    }
}
