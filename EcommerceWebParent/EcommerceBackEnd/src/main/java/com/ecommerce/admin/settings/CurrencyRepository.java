package com.ecommerce.admin.settings;

import com.ecommerce.common.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency,Integer> {

    public List<Currency> findAllByOrderByNameAsc();
}
