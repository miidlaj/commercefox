package com.ecommerce.admin.shippingrate;

public class ShippingRateAlreadyExistsException extends Exception {
    ShippingRateAlreadyExistsException(String message){
        super(message);
    }
}
