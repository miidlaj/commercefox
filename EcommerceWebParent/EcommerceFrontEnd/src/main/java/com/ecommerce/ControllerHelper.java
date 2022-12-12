package com.ecommerce;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class ControllerHelper {
    @Autowired private CustomerService customerService;

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }
}
