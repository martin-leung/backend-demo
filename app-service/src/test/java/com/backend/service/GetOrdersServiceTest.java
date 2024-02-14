package com.backend.service;

import com.backend.bo.Orders;
import com.backend.bo.OrdersListData;
import com.backend.bo.data.OrdersEntity;
import com.backend.bo.data.OrdersRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetOrdersServiceTest extends TestBase {

    @InjectMocks
    private GetOrdersService getOrdersService;

    @Mock
    private OrdersRepository ordersRepository;

    @BeforeTest
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeMethod
    public void setupMocks() {
        Mockito.reset(ordersRepository);
    }

    @Test
    public void testGetOrdersList() throws Exception {
        List<OrdersEntity> ordersList = new ArrayList<>();
        ordersList.add(getOrderEntity());

        when(ordersRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(ordersList));
        OrdersListData response = getOrdersService.getOrders("1","1");

        assertNotNull(response);
        assertNotNull(response.getOrdersList().get(0));
        Orders order = response.getOrdersList().get(0);
        assertEquals(order.getDistance(), DISTANCE);
        assertEquals(order.getId(), ORDER_ID);
        assertEquals(order.getStatus(), "TAKEN");

        verify(ordersRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetOrdersEmptyList() throws Exception {
        when(ordersRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        OrdersListData response = getOrdersService.getOrders("1","1");

        assertNotNull(response);
        assertEquals(response.getOrdersList().size(), 0);
        verify(ordersRepository, times(1)).findAll(any(Pageable.class));
    }
}
