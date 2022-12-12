package com.ecommerce.order;

import com.ecommerce.common.entity.order.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderDetailRepositoryTests {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void testCountByProductAndCustomerAndOrderStatus(){
        Integer productId = 1;
        Integer customerId = 5;

        Long count =orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId, customerId, OrderStatus.DELIVERED);
        assertThat(count).isGreaterThan(0);
    }
}
