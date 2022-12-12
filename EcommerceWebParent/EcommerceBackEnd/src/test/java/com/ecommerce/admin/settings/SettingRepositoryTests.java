package com.ecommerce.admin.settings;

import com.ecommerce.common.entity.setting.Setting;
import com.ecommerce.common.entity.setting.SettingCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTests {

    @Autowired
    SettingRepository settingRepository;

    @Test
    public void testCreateGeneralSettings(){
        //Setting siteName = new Setting("SITE_NAME","CommerceFox", SettingCategory.GENERAL);
        Setting siteLogo = new Setting("SITE_LOGO","Commerce Fox.png", SettingCategory.GENERAL);
        Setting copyRight = new Setting("COPYRIGHT","Copyright (c) 2022 Commerce Fox Ltd.", SettingCategory.GENERAL);

       settingRepository.saveAll(List.of(siteLogo,copyRight));

       Iterable<Setting> iterable = settingRepository.findAll();

       Assertions.assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testCurrencySettings(){
        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "₹", SettingCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType,
                decimalDigits, thousandsPointType));
    }

    @Test
    public void testListSettingsByCategory(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);

        settings.forEach(System.out::println);
    }

    @Test
    public void testPaymentSettings(){
        Setting currencyId = new Setting("PAYPAL_API_BASE_URL", "https://ap", SettingCategory.PAYMENT);
        Setting symbol = new Setting("PAYPAL_API_CLIENT_ID", "₹", SettingCategory.PAYMENT);
        Setting symbolPosition = new Setting("PAYPAL_API_CLIENT_SECRET", "before", SettingCategory.PAYMENT);

        settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition));
    }
}
