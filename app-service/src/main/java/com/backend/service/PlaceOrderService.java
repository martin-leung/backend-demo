package com.backend.service;

import com.backend.bo.GoogleApiBo.Distance;
import com.backend.bo.GoogleApiBo.Element;
import com.backend.bo.GoogleApiBo.GoogleApiResponse;
import com.backend.bo.PlaceOrdersRequest;
import com.backend.bo.PlaceOrdersResponse;
import com.backend.bo.data.OrdersEntity;
import com.backend.bo.data.OrdersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class PlaceOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceOrderService.class);



    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private GoogleService googleService;

    public PlaceOrdersResponse placeOrder(PlaceOrdersRequest placeOrdersRequest) throws Exception {
        // Validate request coordinates
        validateRequest(placeOrdersRequest);

        // Generate unique order id
        String orderId = UUID.randomUUID().toString();
        LOGGER.info("Unique ID generate: {}", orderId);

        // Calculate distance
        int distance = googleService.calculateDistance(placeOrdersRequest.getOrderOrigin(), placeOrdersRequest.getOrderDestination());

        // save to repository
        OrdersEntity ordersEntity = transformToEntity(orderId, distance, placeOrdersRequest);
        ordersRepository.save(ordersEntity);

        // Create response
        PlaceOrdersResponse placeOrdersResponse = new PlaceOrdersResponse();
        placeOrdersResponse.setId(orderId);
        placeOrdersResponse.setDistance(distance);
        placeOrdersResponse.setStatus("UNASSIGNED");
        return placeOrdersResponse;
    }

    private void validateRequest(PlaceOrdersRequest placeOrdersRequest) {
        List<String> origin = placeOrdersRequest.getOrderOrigin();
        List<String> destination = placeOrdersRequest.getOrderDestination();

        if (origin == null || destination == null || origin.size() != 2 || destination.size() != 2) {
            throw new IllegalArgumentException("Invalid Request Received - Wrong Inputs");
        }

        double originLat, originLong, destinationLat, destinationLong;
        try {
            originLat = Double.parseDouble(origin.get(0));
            originLong = Double.parseDouble(origin.get(1));
            destinationLat = Double.parseDouble(destination.get(0));
            destinationLong = Double.parseDouble(destination.get(1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid Request Received - Wrong Type");
        }
        validateCoordinates(originLat, originLong);
        validateCoordinates(destinationLat, destinationLong);
    }

    private void validateCoordinates(double latitude, double longitude) {
        try {
            if (!(latitude >= -90 && latitude <= 90)) {
                throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
            }
            if (!(longitude >= -180 && longitude <= 180)) {
                throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Latitude and Longitude must be a valid numerical value");
        }
    }

    private OrdersEntity transformToEntity(String orderId, int distance, PlaceOrdersRequest placeOrdersRequest) {
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setOrderId(orderId);
        ordersEntity.setDistance(distance);
        ordersEntity.setOriginLatitude(placeOrdersRequest.getOrderOrigin().get(0));
        ordersEntity.setOriginLongitude(placeOrdersRequest.getOrderOrigin().get(1));
        ordersEntity.setDestinationLatitude(placeOrdersRequest.getOrderDestination().get(0));
        ordersEntity.setDestinationLongitude(placeOrdersRequest.getOrderDestination().get(1));
        ordersEntity.setStatus("UNASSIGNED");
        return ordersEntity;
    }
}
