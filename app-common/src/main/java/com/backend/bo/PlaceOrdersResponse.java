package com.backend.bo;


public class PlaceOrdersResponse {

    private String id;

    private Integer distance;

    private String status;

    public  PlaceOrdersResponse() {

    }

    public PlaceOrdersResponse(String orderId, Integer distance, String status) {
        this.id = orderId;
        this.distance = distance;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
