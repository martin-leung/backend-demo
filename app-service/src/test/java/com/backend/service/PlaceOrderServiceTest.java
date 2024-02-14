package com.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.backend.bo.PlaceOrdersRequest;
import com.backend.bo.PlaceOrdersResponse;
import com.backend.bo.data.OrdersEntity;
import com.backend.bo.data.OrdersRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceOrderServiceTest extends TestBase {

    @InjectMocks
    private PlaceOrderService placeOrderService;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private GoogleService googleService;

    @BeforeTest
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeMethod
    public void setupMocks() {
        Mockito.reset(ordersRepository, googleService);
    }

    @Test
    public void testPlaceOrderHappyPath() throws Exception {
        int expectedDistance = 100;
        when(googleService.calculateDistance(anyList(), anyList())).thenReturn(expectedDistance);

        OrdersEntity savedEntity = new OrdersEntity();
        when(ordersRepository.save(any(OrdersEntity.class))).thenReturn(savedEntity);

        PlaceOrdersResponse response = placeOrderService.placeOrder(getPlaceOrdersRequest());

        assertNotNull(response);
        assertEquals("UNASSIGNED", response.getStatus());
        assertEquals(expectedDistance, response.getDistance());
        assertNotNull(response.getId());

        verify(googleService, times(1)).calculateDistance(anyList(), anyList());
        verify(ordersRepository, times(1)).save(any(OrdersEntity.class));
    }

    @Test(dataProvider = "invalidRequests")
    public void testPlaceOrderValidationTest(PlaceOrdersRequest placeOrdersRequest) {
        try {
            PlaceOrdersResponse response = placeOrderService.placeOrder(placeOrdersRequest);
            fail();
        } catch(IllegalArgumentException exception) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DataProvider(name = "invalidRequests")
    public Object[][] invalidRequests() {
        PlaceOrdersRequest emptyOrigin = getPlaceOrdersRequest();
        emptyOrigin.setOrderOrigin(new ArrayList<>());

        PlaceOrdersRequest invalidOriginLong = getPlaceOrdersRequest();
        List<String> invalidLong = Arrays.asList("37.7749", "-190.4194");
        invalidOriginLong.setOrderOrigin(invalidLong);

        PlaceOrdersRequest invalidOriginLat = getPlaceOrdersRequest();
        List<String> invalidLat = Arrays.asList("190.7749", "-176.4194");
        invalidOriginLat.setOrderOrigin(invalidLat);

        PlaceOrdersRequest emptyDestination = getPlaceOrdersRequest();
        emptyDestination.setOrderDestination(new ArrayList<>());

        PlaceOrdersRequest invalidDestLong = getPlaceOrdersRequest();
        invalidDestLong.setOrderDestination(invalidLong);

        PlaceOrdersRequest invalidDestLat = getPlaceOrdersRequest();
        invalidDestLat.setOrderDestination(invalidLat);

        return new Object[][] {
                { emptyOrigin },
                { invalidOriginLong },
                { invalidOriginLat },
                { emptyDestination },
                { invalidDestLong },
                { invalidDestLat }
        };
    }
}
