package com.hcpt.multirestaurants.object;

public class RelishOption {
    private String id;
    private String name;

    public RelishOption() {
    }

    public RelishOption(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private boolean isChecker = false;

    public boolean getChecker() {
        return isChecker;
    }

    public void setChecker(boolean checker) {
        isChecker = checker;
    }

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

}
