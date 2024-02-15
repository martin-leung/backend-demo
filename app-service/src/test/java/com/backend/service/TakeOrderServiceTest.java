package com.backend.service;

import com.backend.bo.AppException;
import com.backend.bo.TakeOrderResponse;
import com.backend.bo.data.OrdersEntity;
import com.backend.bo.data.OrdersRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class TakeOrderServiceTest extends TestBase {

    @InjectMocks
    private TakeOrderService takeOrderService;

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
    public void testTakeOrderHappyPath() throws AppException {
        OrdersEntity ordersEntity = getOrderEntity();
        ordersEntity.setStatus(UNASSIGNED);

        when(ordersRepository.findByOrderId(ORDER_ID)).thenReturn(ordersEntity);

        TakeOrderResponse response = takeOrderService.takeOrder(ORDER_ID, getTakeOrderRequest());

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());

        verify(ordersRepository, times(1)).save(any(OrdersEntity.class));
    }
}
