package com.ecommerce.review.vote;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.review.Review;
import com.ecommerce.common.entity.review.ReviewVote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewVoteRepositoryTests {

    @Autowired
    private ReviewVoteRepository repo;

    @Test
    public void testSaveVote() {
        Integer customerId = 38;
        Integer reviewId = 13;

        ReviewVote vote = new ReviewVote();
        vote.setCustomer(new Customer(customerId));
        vote.setReview(new Review(reviewId));
        vote.voteUp();

        ReviewVote savedVote = repo.save(vote);
        Assertions.assertThat(savedVote.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByReviewAndCustomer() {
        Integer customerId = 38;
        Integer reviewId = 13;

        ReviewVote vote = repo.findByReviewAndCustomer(reviewId, customerId);
        Assertions.assertThat(vote).isNotNull();

        System.out.println(vote);
    }

    @Test
    public void testFindByProductAndCustomer() {
        Integer customerId = 38;
        Integer productId = 6;

        List<ReviewVote> listVotes = repo.findByProductAndCustomer(productId, customerId);
        Assertions.assertThat(listVotes.size()).isGreaterThan(0);

        listVotes.forEach(System.out::println);
    }
}
