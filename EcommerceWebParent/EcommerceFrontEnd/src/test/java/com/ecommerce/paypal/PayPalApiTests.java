package com.ecommerce.paypal;

import com.ecommerce.checkout.PayPalOrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class PayPalApiTests {

    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "Ad3O5iL8vxWTUogujbzD7_Fh2a2nTbg2luyO5l3wn1LE5NoaCJKHfR0NkbYlnrdNHNBsT26qzvBRVUfx";
    private static final String CLIENT_SECRET = "EF_g0nwwqNuvvZFc2k-1LznlofGf3cHolTHKULZpQTpNW8nJdg1ONOcGAKPpN8Q8BBQHj9c_rli4FHs0";

    @Test
    public void testOrderDetails() {
        String orderId = "4A7578984G8992139";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language","en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request,PayPalOrderResponse.class);
        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println("OrderId: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));

    }

    @Test
    public void testGetOrderDetails() {
        String orderId = "4A7578984G8992139";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                requestURL, HttpMethod.GET, request, String.class);

        System.out.println(response);

    }


}
