package com.backend.controller;

import com.backend.bo.AppException;
import com.backend.bo.ErrorResponse;
import com.backend.bo.PlaceOrdersRequest;
import com.backend.bo.TakeOrderRequest;
import com.backend.service.GetOrdersService;
import com.backend.service.PlaceOrderService;
import com.backend.service.TakeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private PlaceOrderService placeOrderService;

    @Autowired
    private TakeOrderService takeOrderService;

    @Autowired
    private GetOrdersService getOrdersService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrdersRequest placeOrdersRequest) {
        try {
            LOGGER.info("PlaceOrdersRequest received: " + placeOrdersRequest.toString());
            return ResponseEntity.ok(placeOrderService.placeOrder(placeOrdersRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid request: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Request failed due to internal server error: " + e.getMessage()));
        }
    }

    @PatchMapping("/orders/{id}")
    @Transactional
    public ResponseEntity<?> takeOrder(@PathVariable String id, @RequestBody TakeOrderRequest takeOrderRequest) {
        try {
            LOGGER.info("TakeOrderRequest: {}, received for id: {}", takeOrderRequest, id);
            return ResponseEntity.ok(takeOrderService.takeOrder(id, takeOrderRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid request: " + e.getMessage()));
        } catch (AppException appException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error occurred while processing order: " + appException.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Request failed due to internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam(name = "page") String page, @RequestParam(name = "limit") String limit) {
        try {
            LOGGER.info("getOrders page: {} and limit: {}", page, limit);
            return ResponseEntity.ok(getOrdersService.getOrders(page, limit));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid request: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Request failed due to internal server error: " + e.getMessage()));
        }
    }
}
