package com.ecommerce.order;

import com.ecommerce.Utility;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.customer.CustomerNotFoundException;
import com.ecommerce.customer.CustomerService;
import com.ecommerce.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;

    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
                                                      HttpServletRequest servletRequest
                                                      ){
        Customer customer = null;
        try {
            customer = getAuthenticatedCustomer(servletRequest);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>("Authentication Required", HttpStatus.BAD_REQUEST);
        }

        try {
            orderService.setOrderReturnRequested(returnRequest, customer);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request)
            throws CustomerNotFoundException {
        String email = Utility.getEmailAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }

        return customerService.getCustomerByEmail(email);
    }
}
