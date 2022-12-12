package com.ecommerce.review.vote;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.review.Review;
import com.ecommerce.common.entity.review.ReviewVote;
import com.ecommerce.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ReviewVoteService {

    @Autowired private ReviewRepository reviewRepository;

    @Autowired private  ReviewVoteRepository voteRepository;

    public VoteResult undoVote(ReviewVote vote, Integer reviewId, VoteType voteType){
        voteRepository.delete(vote);
        reviewRepository.updateVoteCount(reviewId);

        Integer voteCount = reviewRepository.getVoteCount(reviewId);

        return VoteResult.success("You have unvoted " + voteType + " that review.",voteCount);
    }

    public VoteResult doVote(Integer reviewId, Customer customer, VoteType voteType){

        Review review = null;
        try {
            review = reviewRepository.findById(reviewId).get();
        }catch (NoSuchElementException e){
            return VoteResult.fail("The review Id "+ reviewId + " no longer exists.");
        }

        ReviewVote vote = voteRepository.findByReviewAndCustomer(reviewId, customer.getId());

        if (vote != null){
            if (vote.isUpvoted() && voteType.equals(VoteType.UP) ||
                    vote.isDownvoted() && voteType.equals(VoteType.DOWN)){

                return undoVote(vote, reviewId, voteType);
            } else if (vote.isUpvoted() && voteType.equals(VoteType.DOWN)){
                vote.voteDown();
            } else if (vote.isDownvoted() && voteType.equals(VoteType.UP)){
                vote.voteUp();
            }
        } else {
            vote = new ReviewVote();
            vote.setCustomer(customer);
            vote.setReview(review);

            if (voteType.equals(VoteType.UP)){
                vote.voteUp();
            } else {
                vote.voteDown();
            }
        }

        voteRepository.save(vote);
        reviewRepository.updateVoteCount(reviewId);
        Integer voteCount = reviewRepository.getVoteCount(reviewId);

        return VoteResult.success("You successfully voted "+ voteType + " that review.", voteCount);
    }

    public void markReviewVotedForProductByCustomer(List<Review> listReviews, Integer productId, Integer customerId){
        List<ReviewVote> listVotes = voteRepository.findByProductAndCustomer(productId, customerId);

        for (ReviewVote vote : listVotes){
            Review votedReview = vote.getReview();

            if (listReviews.contains(votedReview)){
                int index = listReviews.indexOf(votedReview);
                Review review = listReviews.get(index);

                if (vote.isUpvoted()){
                    review.setUpvotedByCurrentCustomer(true);
                } else {
                    review.setDownvotedByCurrentCustomer(true);
                }
            }
        }
    }
}
