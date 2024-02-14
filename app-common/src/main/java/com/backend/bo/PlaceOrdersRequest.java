package com.backend.bo;

import java.util.List;

public class PlaceOrdersRequest {

    private List<String> orderOrigin;

    private List<String> orderDestination;

    public PlaceOrdersRequest() {
    }

    public PlaceOrdersRequest(List<String> origin, List<String> destination) {
        this.orderOrigin = orderOrigin;
        this.orderDestination = orderDestination;
    }

    public List<String> getOrderOrigin() {
        return orderOrigin;
    }

    public void setOrderOrigin(List<String> orderOrigin) {
        this.orderOrigin = orderOrigin;
    }

    public List<String> getOrderDestination() {
        return orderDestination;
    }

    public void setOrderDestination(List<String> orderDestination) {
        this.orderDestination = orderDestination;
    }

    @Override
    public String toString() {
        return "PlaceOrdersRequest{" +
                "orderOrigin=" + orderOrigin +
                ", orderDestination=" + orderDestination +
                '}';
    }
}
