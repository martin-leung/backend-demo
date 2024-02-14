package com.backend.service;

import com.backend.bo.GoogleApiBo.Distance;
import com.backend.bo.GoogleApiBo.Element;
import com.backend.bo.GoogleApiBo.GoogleApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class GoogleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleService.class);


    @Value("${api.key:null}")
    private String API_KEY;

    private static String url = "https://maps.googleapis.com/maps/api/distancematrix/json" +
            "?origins=%s,%s" +
            "&destinations=%s,%s" +
            "&key=%s";

    public Integer calculateDistance(List<String> origin, List<String> destination) throws URISyntaxException, IOException, InterruptedException {
        String originLat = origin.get(0);
        String originLong = origin.get(1);
        String destLat = destination.get(0);
        String destLong = destination.get(1);
        String formattedUrl = String.format(url, originLat, originLong, destLat, destLong, API_KEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(formattedUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LOGGER.info("Response status code: " + response.statusCode());
        LOGGER.info("Response body: " + response.body());
        return transformJsonIntoObject(response.body());
    }

    private Integer transformJsonIntoObject(String jsonResponse) {
        int distanceValue = 0;
        try {
            ObjectMapper mapper = new ObjectMapper();
            GoogleApiResponse response = mapper.readValue(jsonResponse, GoogleApiResponse.class);

            if (!response.getRows().isEmpty()) {
                Element element = response.getRows().get(0).getElements().get(0);
                Distance distance = element.getDistance();
                distanceValue = distance.getValue();
                LOGGER.info("Distance value: " + distanceValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distanceValue;
    }

}
