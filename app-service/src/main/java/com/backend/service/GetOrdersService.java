package com.backend.service;


import com.backend.bo.AppException;
import com.backend.bo.Orders;
import com.backend.bo.OrdersListData;
import com.backend.bo.data.OrdersEntity;
import com.backend.bo.data.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GetOrdersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetOrdersService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    public OrdersListData getOrders(String page, String limit) throws AppException {
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);

        if (pageInt < 1 || limitInt < 1) {
            throw new AppException("Page and limit must be positive integers");
        }

        List<OrdersEntity> orders = getOrdersByPage(pageInt, limitInt);
        if (orders.isEmpty()) {
            return new OrdersListData(new ArrayList<>());
        } else {
            return new OrdersListData(transformData(orders));
        }
    }

    public List<OrdersEntity> getOrdersByPage(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<OrdersEntity> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.getContent();
    }

    private List<Orders> transformData(List<OrdersEntity> ordersEntityList) {
        List<Orders> ordersList = new ArrayList<>();
        for (OrdersEntity ordersEntity : ordersEntityList) {
            Orders order = new Orders();
            order.setId(ordersEntity.getOrderId());
            order.setDistance(ordersEntity.getDistance());
            order.setStatus(ordersEntity.getStatus());
            ordersList.add(order);
        }
        return ordersList;
    }
}
