package com.ecommerce.checkout;

import com.ecommerce.setting.PaymentSettingBag;
import com.ecommerce.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class PayPalService {

    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    @Autowired private SettingService settingService;

    @Autowired private RestTemplate restTemplate;

    public boolean validateOrder(String orderId) throws PayPalApiException {
        PayPalOrderResponse orderResponse = getOrderDetails(orderId);

        return orderResponse.validate(orderId);
    }

    private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalApiException {

        ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);

        HttpStatus statusCode = response.getStatusCode();

        if (!statusCode.equals(HttpStatus.OK)){
            throwExceptionForNoneOkResponse(statusCode);
        }


        return response.getBody();
    }

    private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String baseURL = paymentSettings.getURL();
        String requestURL = baseURL + GET_ORDER_API + orderId;
        String clientId = paymentSettings.getClientId();
        String clientSecret = paymentSettings.getClientSecretCode();


        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language","en_US");
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                requestURL, HttpMethod.GET, request,PayPalOrderResponse.class);
    }

    private static void throwExceptionForNoneOkResponse(HttpStatus statusCode) throws PayPalApiException {

        String message = null;

        switch (statusCode){

            case NOT_FOUND :
                message = "Order Id not found";
            case BAD_REQUEST:
                message = "Bad Request to PayPal Checkout Api";
            case INTERNAL_SERVER_ERROR:
                message = "PayPal Server Error";
            default:
                message = "PayPal returned non-OK status code";
        }

        throw new PayPalApiException(message);
    }
}
