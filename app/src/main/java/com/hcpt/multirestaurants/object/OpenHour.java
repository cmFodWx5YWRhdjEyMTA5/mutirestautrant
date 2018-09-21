package com.hcpt.multirestaurants.object;

import java.util.Calendar;

public class OpenHour {

    private int dateId;
    private String dateName;
    private int shopId;
    private String openAM;
    private String closeAM;
    private String openPM;
    private String closePM;


    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getOpenAM() {
        return openAM;
    }

    public void setOpenAM(String openAM) {
        this.openAM = openAM;
    }

    public String getCloseAM() {
        return closeAM;
    }

    public void setCloseAM(String closeAM) {
        this.closeAM = closeAM;
    }

    public String getOpenPM() {
        return openPM;
    }

    public void setOpenPM(String openPM) {
        this.openPM = openPM;
    }

    public String getClosePM() {
        return closePM;
    }

    public void setClosePM(String closePM) {
        this.closePM = closePM;
    }

    public boolean isCloseInNextDay() {
        Calendar calOpen = Calendar.getInstance();
        calOpen.setTimeInMillis(Long.parseLong(this.openAM) * 1000);

        Calendar calClose = Calendar.getInstance();
        calClose.setTimeInMillis(Long.parseLong(this.closePM) * 1000);
        return calClose.get(Calendar.DAY_OF_MONTH) > calOpen.get(Calendar.DAY_OF_MONTH);
    }
}


