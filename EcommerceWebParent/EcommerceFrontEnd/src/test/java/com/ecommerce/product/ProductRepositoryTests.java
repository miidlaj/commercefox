package com.ecommerce.product;

import com.ecommerce.common.entity.product.Product;
import com.ecommerce.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Test
    public  void testFindByAlias(){
        String alias = "canon-eos-m50";
        Product product = productRepository.findByAlias(alias);

        Assertions.assertThat(product).isNotNull();
    }
}
