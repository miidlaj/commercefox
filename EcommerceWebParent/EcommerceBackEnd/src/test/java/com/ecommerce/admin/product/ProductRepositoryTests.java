package com.ecommerce.admin.product;

import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class,38);
        Category category = entityManager.find(Category.class,6);

        Product product = new Product();
        product.setName("Lenovo idealpad slim 3");
        product.setAlias("ideapad slim 3");
        product.setShortDescription("Best value laptop from Lenovo");
        product.setFullDescription("i5 11 th gen laptop from Lenovo, value for money.");
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(58999);
        product.setCost(45699);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);

    }
    @Test
    public void testListAllProducts(){
        Iterable<Product> iterableProducts = productRepository.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 3;
        Product product = productRepository.findById(id).get();
        System.out.println(product);
        Assertions.assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 83;

        Product product = productRepository.findById(id).get();
        product.setInStock(false);

        productRepository.save(product);

        Product updatedProduct = entityManager.find(Product.class,id);

        Assertions.assertThat(updatedProduct.isInStock()).isEqualTo(false);

    }

    @Test
    public void testDeleteByid(){
        int id = 2;
        productRepository.deleteById(id);
    }

    @Test
    public void testSaveProductWithImages(){
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();

        product.setMainImage("main image.png");
        product.addExtraImage("extra1.png");
        product.addExtraImage("extra2.png");
        product.addExtraImage("extra3.png");

        Product savedProduct = productRepository.save(product);

        Assertions.assertThat(savedProduct.getImages().size()).isEqualTo(3);

    }

    @Test
    public void testSaveProductWithDetails() {
        Integer productId = 1;
        Product product = productRepository.findById(productId).get();

        product.addDetail("Device Memory", "128 GB");
        product.addDetail("CPU Model", "MediaTek");
        product.addDetail("OS", "Android 10");

        Product savedProduct = productRepository.save(product);
        Assertions.assertThat(savedProduct.getDetails()).isNotEmpty();
    }

    @Test
    public void testUpdateReviewCountAndAverageRating() {
        Integer productId = 100;
        productRepository.updateReviewCountAndAverageRating(productId);
    }

}
