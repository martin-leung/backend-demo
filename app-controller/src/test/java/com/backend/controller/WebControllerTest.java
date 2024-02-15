package com.backend.controller;

import com.backend.bo.*;
import com.backend.service.GetOrdersService;
import com.backend.service.PlaceOrderService;
import com.backend.service.TakeOrderService;
import org.aspectj.weaver.ast.Or;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class WebControllerTest {

    @Mock
    private PlaceOrderService placeOrderService;

    @Mock
    private TakeOrderService takeOrderService;

    @Mock
    private GetOrdersService getOrdersService;

    @InjectMocks
    private WebController webController;

    @BeforeMethod
    public void init(){
        MockitoAnnotations.openMocks(this);
        reset(placeOrderService, takeOrderService, getOrdersService);
    }

    @Test
    public void testPlaceOrder_Success() throws Exception {
        PlaceOrdersRequest request = new PlaceOrdersRequest();
        when(placeOrderService.placeOrder(request)).thenReturn(new PlaceOrdersResponse("orderId", 100, "UNASSIGNED"));
        ResponseEntity<?> responseEntity = webController.placeOrder(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(placeOrderService, times(1)).placeOrder(any());
    }

    @Test
    public void testPlaceOrder_InvalidRequest() throws Exception {
        PlaceOrdersRequest request = new PlaceOrdersRequest();
        doThrow(new IllegalArgumentException("Invalid request")).when(placeOrderService).placeOrder(request);
        ResponseEntity<?> responseEntity = webController.placeOrder(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid request: Invalid request", ((ErrorResponse) responseEntity.getBody()).getError());
        verify(placeOrderService, times(1)).placeOrder(any());
    }

    @Test
    public void testPlaceOrder_InternalServerError() throws Exception {
        PlaceOrdersRequest request = new PlaceOrdersRequest();
        doThrow(new RuntimeException("Internal Server Error")).when(placeOrderService).placeOrder(request);
        ResponseEntity<?> responseEntity = webController.placeOrder(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Request failed due to internal server error: Internal Server Error", ((ErrorResponse) responseEntity.getBody()).getError());
        verify(placeOrderService, times(1)).placeOrder(any());
    }

    @Test
    public void testTakeOrder_Success() throws AppException {
        String orderId = "123";
        TakeOrderRequest request = new TakeOrderRequest();
        when(takeOrderService.takeOrder(orderId, request)).thenReturn(new TakeOrderResponse());

        ResponseEntity<?> responseEntity = webController.takeOrder(orderId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(takeOrderService, times(1)).takeOrder(any(), any());
    }

    @Test
    public void testTakeOrder_InvalidRequest() throws AppException {
        String orderId = "123";
        TakeOrderRequest request = new TakeOrderRequest();
        doThrow(new IllegalArgumentException("Invalid request")).when(takeOrderService).takeOrder(orderId, request);

        ResponseEntity<?> responseEntity = webController.takeOrder(orderId, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid request: Invalid request", ((ErrorResponse) responseEntity.getBody()).getError());
        verify(takeOrderService, times(1)).takeOrder(any(), any());
    }

    @Test
    public void testTakeOrder_InternalServerError() throws AppException {
        String orderId = "123";
        TakeOrderRequest request = new TakeOrderRequest();
        doThrow(new RuntimeException("Internal Server Error")).when(takeOrderService).takeOrder(orderId, request);

        ResponseEntity<?> responseEntity = webController.takeOrder(orderId, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Request failed due to internal server error: Internal Server Error", ((ErrorResponse) responseEntity.getBody()).getError());
        verify(takeOrderService, times(1)).takeOrder(any(), any());
    }

    @Test
    public void testGetOrders_Success() throws AppException {
        String page = "1";
        String limit = "10";
        when(getOrdersService.getOrders(page, limit)).thenReturn(new OrdersListData());
        ResponseEntity<?> responseEntity = webController.getOrders(page, limit);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(getOrdersService, times(1)).getOrders(any(), any());
    }

    @Test
    public void testGetOrders_InvalidRequest() throws AppException {
        String page = "-1";
        String limit = "1";
        doThrow(new IllegalArgumentException("Page and limit must be positive integers")).when(getOrdersService).getOrders(page, limit);
        ResponseEntity<?> responseEntity = webController.getOrders(page, limit);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid request: Page and limit must be positive integers", ((ErrorResponse) responseEntity.getBody()).getError());
        verify(getOrdersService, times(1)).getOrders(any(), any());
    }

    @Test
    public void testGetOrders_InternalServerError() throws AppException {
        String page = "1";
        String limit = "10";
        doThrow(new RuntimeException("Internal Server Error")).when(getOrdersService).getOrders(page, limit);
        ResponseEntity<?> responseEntity = webController.getOrders(page, limit);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Request failed due to internal server error: Internal Server Error", ((ErrorResponse) responseEntity.getBody()).getError());
        verify(getOrdersService, times(1)).getOrders(any(), any());
    }
}