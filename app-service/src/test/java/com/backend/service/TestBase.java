package com.backend.service;

import com.backend.bo.PlaceOrdersRequest;
import com.backend.bo.PlaceOrdersResponse;
import com.backend.bo.TakeOrderRequest;
import com.backend.bo.data.OrdersEntity;

import java.util.Arrays;
import java.util.List;

public class TestBase {

    public static String ORDER_ID = "2a66c78d-87fe-4302-b079-c151945ad554";
    public static Integer DISTANCE = 616600;
    public static String UNASSIGNED = "UNASSIGNED";
    public static String TAKEN = "TAKEN";

    protected PlaceOrdersRequest getPlaceOrdersRequest() {
        PlaceOrdersRequest placeOrdersRequest = new PlaceOrdersRequest();
        List<String> origin = Arrays.asList("37.7749", "-122.4194");
        List<String> destination = Arrays.asList("34.0522", "-118.2437");
        placeOrdersRequest.setOrderOrigin(origin);
        placeOrdersRequest.setOrderDestination(destination);
        return placeOrdersRequest;
    }

    protected PlaceOrdersResponse getPlaceOrdersResponse() {
        return new PlaceOrdersResponse(ORDER_ID, DISTANCE, UNASSIGNED);
    }

    protected OrdersEntity getOrderEntity() {
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setStatus("TAKEN");
        ordersEntity.setOrderId(ORDER_ID);
        ordersEntity.setDistance(DISTANCE);
        return ordersEntity;
    }

    protected TakeOrderRequest getTakeOrderRequest(){
        TakeOrderRequest takeOrderRequest = new TakeOrderRequest();
        takeOrderRequest.setStatus(TAKEN);
        return takeOrderRequest;
    }
}
