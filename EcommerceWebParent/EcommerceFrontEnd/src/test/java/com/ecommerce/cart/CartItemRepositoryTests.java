package com.ecommerce.cart;

import com.ecommerce.common.entity.CartItem;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTests {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testSaveItem(){
        Integer customerId = 1;
        Integer productId = 1;

        Customer customer = testEntityManager.find(Customer.class, customerId);
        Product product = testEntityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setCustomer(customer);
        newItem.setQuantity(1);

        CartItem savedItem = cartItemRepository.save(newItem);

        Assertions.assertThat(savedItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSave2Items(){
        Integer customerId = 5;
        Integer productId = 10;

        Customer customer = testEntityManager.find(Customer.class, customerId);
        Product product = testEntityManager.find(Product.class, productId);

        CartItem item1 = new CartItem();
        item1.setProduct(product);
        item1.setCustomer(customer);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setCustomer(new Customer(customerId));
        item2.setProduct(new Product(8));
        item2.setQuantity(3);

        Iterable<CartItem> iterable = cartItemRepository.saveAll(List.of(item1,item2));

        Assertions.assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 5;
        List<CartItem> listItems = cartItemRepository.findByCustomer(new Customer(customerId));

        listItems.forEach(System.out::println);

        Assertions.assertThat(listItems.size()).isEqualTo(2);
    }

    @Test
    public void testFindByCustomerAndProduct() {
        Integer customerId = 1;
        Integer productId = 1;

        CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        Assertions.assertThat(item).isNotNull();

        System.out.println(item);
    }

    @Test
    public void testUpdateQuantity() {
        Integer customerId = 1;
        Integer productId = 1;
        Integer quantity = 4;

        cartItemRepository.updateQuantity(quantity, customerId, productId);

        CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        Assertions.assertThat(item.getQuantity()).isEqualTo(4);
    }

    @Test
    public void testDeleteByCustomerAndProduct() {
        Integer customerId = 5;
        Integer productId = 8;

        cartItemRepository.deleteByCustomerAndProduct(customerId, productId);

        CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        Assertions.assertThat(item).isNull();
    }

}
