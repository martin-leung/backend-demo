package com.backend.service;

import com.backend.bo.AppException;
import com.backend.bo.TakeOrderRequest;
import com.backend.bo.TakeOrderResponse;
import com.backend.bo.data.OrdersEntity;
import com.backend.bo.data.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TakeOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TakeOrderService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Transactional
    public TakeOrderResponse takeOrder(String id, TakeOrderRequest takeOrderRequest) throws AppException {
        OrdersEntity ordersEntity = ordersRepository.findByOrderId(id);

        if (ordersEntity == null) {
            throw new AppException("Order not found");
        }
        if (!ordersEntity.getStatus().equals("UNASSIGNED")) {
            throw new AppException("Order is already taken or being processed");
        }

        ordersEntity.setStatus(takeOrderRequest.getStatus());
        try {
            ordersRepository.save(ordersEntity);
        } catch (OptimisticLockingFailureException ex) {
            throw new AppException("Failed to take order due to concurrent update", ex);
        }

        TakeOrderResponse takeOrderResponse = new TakeOrderResponse();
        takeOrderResponse.setStatus("SUCCESS");
        return takeOrderResponse;
    }
}
