package com.medicaldata.rest.data.model;

public class Users {
    private String id;
    private String address;
    private long appId;

    public Users(String id, String address, long appId) {
        this.id = id;
        this.address = address;
        this.appId = appId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", appId=" + appId +
                '}';
    }
}
