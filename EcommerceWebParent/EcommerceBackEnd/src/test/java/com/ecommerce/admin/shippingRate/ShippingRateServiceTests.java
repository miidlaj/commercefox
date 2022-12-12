package com.ecommerce.admin.shippingRate;

import com.ecommerce.admin.product.ProductRepository;
import com.ecommerce.admin.shippingrate.ShippingRateNotFoundException;
import com.ecommerce.admin.shippingrate.ShippingRateRepository;
import com.ecommerce.admin.shippingrate.ShippingRateService;
import com.ecommerce.common.entity.ShippingRate;
import com.ecommerce.common.entity.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTests {

    @MockBean
    private ShippingRateRepository shipRepo;
    @MockBean
    private ProductRepository productRepo;

    @InjectMocks
    private ShippingRateService shipService;

    @Test
    public void testCalculateShippingCost_NoRateFound() {
        Integer productId = 1;
        Integer countryId = 234;
        String state = "ABCDE";

        Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(null);

        Assertions.assertThrows(ShippingRateNotFoundException.class, new Executable() {

            @Override
            public void execute() throws Throwable {
                shipService.calculateShippingCost(productId, countryId, state);
            }
        });
    }

    @Test
    public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
        Integer productId = 1;
        Integer countryId = 234;
        String state = "New York";

        ShippingRate shippingRate = new ShippingRate();
        shippingRate.setRate(10);

        Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(shippingRate);

        Product product = new Product();
        product.setWeight(5);
        product.setWidth(4);
        product.setHeight(3);
        product.setLength(8);

        Mockito.when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        float shippingCost = shipService.calculateShippingCost(productId, countryId, state);

        System.out.println(shippingCost);
    }
}