package com.ecommerce.admin.review;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.product.Product;
import com.ecommerce.common.entity.review.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository repo;

    @Test
    public void testCreateReview() {
        Integer productId = 45;
        Product product = new Product(productId);

        Integer customerId = 38;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewTime(new Date());
        review.setHeadline("As Excepted Quality");
        review.setComment("Worth for the price. Don't expect higher");
        review.setRating(2);

        Review savedReview = repo.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testListReviews() {
        List<Review> listReviews = repo.findAll();

        Assertions.assertThat(listReviews.size()).isGreaterThan(0);

        listReviews.forEach(System.out::println);
    }

    @Test
    public void testGetReview() {
        Integer id = 1;
        Review review = repo.findById(id).get();

        Assertions.assertThat(review).isNotNull();

        System.out.println(review);
    }

    @Test
    public void testUpdateReview() {
        Integer id = 1;
        String headline = "An awesome camera at an awesome price";
        String comment = "Overall great camera and is highly capable...";

        Review review = repo.findById(id).get();
        review.setHeadline(headline);
        review.setComment(comment);

        Review updatedReview = repo.save(review);

        Assertions.assertThat(updatedReview.getHeadline()).isEqualTo(headline);
        Assertions.assertThat(updatedReview.getComment()).isEqualTo(comment);
    }

    @Test
    public void testDeleteReview() {
        Integer id = 1;
        repo.deleteById(id);

        Optional<Review> findById = repo.findById(id);

        Assertions.assertThat(findById).isNotPresent();
    }
}