# backend-demo

Port is 8080.

To provide Google apiKey please input key in command line after running start.sh. 

Example:
![image](https://github.com/martin-leung/backend-demo/assets/26888677/178b7557-be63-47da-a564-74b335539f86)

Example URLs used to hit application:

API - Place Order

http://localhost:8080/orders

cURL: 
curl --location 'http://localhost:8080/orders/:616600' \
--header 'Content-Type: application/json' \
--data '{
    "orderOrigin": [
        "37.7749",
        "-122.4194"
    ],
    "orderDestination": [
        "34.0522",
        "-118.2437"
    ]
}'

API - Take Order

http://localhost:8080/orders/74186f0f-f3a7-42c9-992e-d727c8c96d04

cURL: 
curl --location --request PATCH 'http://localhost:8080/orders/74186f0f-f3a7-42c9-992e-d727c8c96d04' \
--header 'Content-Type: application/json' \
--data '{
    "status": "TAKEN"
}'

API - Order List

http://localhost:8080/orders?page=1&limit=1

cURL:
curl --location 'http://localhost:8080/orders?page=1&limit=1'
