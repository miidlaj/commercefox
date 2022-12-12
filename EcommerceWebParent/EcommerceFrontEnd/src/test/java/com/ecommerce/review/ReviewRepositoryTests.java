package com.ecommerce.review;

import com.ecommerce.common.entity.product.Product;
import com.ecommerce.common.entity.review.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.data.domain.Pageable;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository repo;

    @Test
    public void testFindByCustomerNoKeyword() {
        Integer customerId = 5;
        Pageable pageable = PageRequest.of(1, 5);

        Page<Review> page = repo.findByCustomer(customerId, pageable);
        long totalElements = page.getTotalElements();

        Assertions.assertThat(totalElements).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomerWithKeyword() {
        Integer customerId = 5;
        String keyword = "GPS";
        Pageable pageable = PageRequest.of(1, 5);

        Page<Review> page = repo.findByCustomer(customerId, keyword, pageable);
        long totalElements = page.getTotalElements();

        Assertions.assertThat(totalElements).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomerAndId() {
        Integer customerId = 5;
        Integer reviewId = 4;

        Review review = repo.findByCustomerAndId(customerId, reviewId);
        Assertions.assertThat(review).isNotNull();
    }

    @Test
    public void testFindByProduct() {
        Product product = new Product(6);
        Pageable pageable = PageRequest.of(0, 3);
        Page<Review> page = repo.findByProduct(product, pageable);

        Assertions.assertThat(page.getTotalElements()).isGreaterThan(0);

        List<Review> content = page.getContent();
        content.forEach(System.out::println);
    }

    @Test
    public void testCountByCustomerAndProduct() {
        Integer customerId = 5;
        Integer productId = 6;
        Long count = repo.countByCustomerAndProduct(customerId, productId);

        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    public void testUpdateVoteCount() {
        Integer reviewId = 13;
        repo.updateVoteCount(reviewId);
        Review review = repo.findById(reviewId).get();

        Assertions.assertThat(review.getVotes()).isEqualTo(1);
    }

    @Test
    public void testGetVoteCount() {
        Integer reviewId = 13;
        Integer voteCount = repo.getVoteCount(reviewId);

        Assertions.assertThat(voteCount).isEqualTo(1);
    }
}