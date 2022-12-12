package com.ecommerce.cart;

import com.ecommerce.Utility;
import com.ecommerce.addresses.AddressService;
import com.ecommerce.common.entity.Address;
import com.ecommerce.common.entity.CartItem;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.ShippingRate;
import com.ecommerce.customer.CustomerNotFoundException;
import com.ecommerce.customer.CustomerService;
import com.ecommerce.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired private ShoppingCartService cartService;

    @Autowired private CustomerService customerService;

    @Autowired private AddressService addressService;

    @Autowired private ShippingRateService shippingRateService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        boolean isCartIsUpdatedByStock = true;

        float estimatedTotal = 0.0F;
        float estimatedPriceTotal = 0.0F;
        for (CartItem Item : cartItems){
            estimatedTotal += Item.getSubtotal();
            estimatedPriceTotal += Item.getPriceSubTotal();

//            if (Integer.parseInt(Item.getProduct().getStock()) > 0 || Item.getProduct().isInStock() == false){
//                isCartIsUpdatedByStock = false;
//            }
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if (defaultAddress != null){
            shippingRate  = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            usePrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported", shippingRate != null);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);
        model.addAttribute("estimatedPriceTotal", estimatedPriceTotal);
        model.addAttribute("isCartIsUpdatedByStock", isCartIsUpdatedByStock);


        return "/cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }
}
