package com.ecommerce.admin.settings;

import com.ecommerce.common.entity.Currency;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTests {

    @Autowired
    private CurrencyRepository repo;

    @Test
    public void testCreateCurrencies() {
        List<Currency> listCurrencies = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("British Pound", "£", "GPB"),
                new Currency("Japanese Yen", "¥", "JPY"),
                new Currency("Euro", "€", "EUR"),
                new Currency("Russian Ruble", "₽", "RUB"),
                new Currency("South Korean Won", "₩", "KRW"),
                new Currency("Chinese Yuan", "¥", "CNY"),
                new Currency("Brazilian Real", "R$", "BRL"),
                new Currency("Australian Dollar", "$", "AUD"),
                new Currency("Canadian Dollar", "$", "CAD"),
                new Currency("Vietnamese đồng ", "₫", "VND"),
                new Currency("Indian Rupee", "₹", "INR")
        );

        repo.saveAll(listCurrencies);

        Iterable<Currency> iterable = repo.findAll();

        Assertions.assertThat(iterable).size().isEqualTo(12);
    }

    @Test
    public void testListAllOrderByNameAsc() {
        List<Currency> currencies = repo.findAllByOrderByNameAsc();

        currencies.forEach(System.out::println);

        Assertions.assertThat(currencies.size()).isGreaterThan(0);
    }
}
