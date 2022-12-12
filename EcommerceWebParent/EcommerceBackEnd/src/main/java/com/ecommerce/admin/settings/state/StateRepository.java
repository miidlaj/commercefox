package com.ecommerce.admin.settings.state;

import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State,Integer> {
    public List<State> findByCountryOrderByNameAsc(Country country);


}
