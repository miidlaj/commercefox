package com.ecommerce.shipping;

import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.ShippingRate;
import com.ecommerce.common.entity.State;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    public ShippingRate findByCountryAndState(Country country, String state);
}
