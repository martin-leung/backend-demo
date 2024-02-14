package com.backend.bo;

import java.util.List;

public class OrdersListData {

    List<Orders> ordersList;

    public OrdersListData() {
    }

    public OrdersListData(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }
}
