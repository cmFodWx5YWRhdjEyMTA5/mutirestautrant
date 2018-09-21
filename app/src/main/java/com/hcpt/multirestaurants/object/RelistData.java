package com.hcpt.multirestaurants.object;

import java.util.ArrayList;

public class RelistData {
    private String id;
    private String name;
    private ArrayList<Relish> relishArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Relish> getRelishArrayList() {
        return relishArrayList;
    }

    public void setRelishArrayList(ArrayList<Relish> relishArrayList) {
        this.relishArrayList = relishArrayList;
    }
}
