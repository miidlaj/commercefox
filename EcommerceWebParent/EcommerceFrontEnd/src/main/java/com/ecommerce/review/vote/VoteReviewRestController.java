package com.ecommerce.review.vote;

import com.ecommerce.ControllerHelper;
import com.ecommerce.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VoteReviewRestController {

    @Autowired
    private ReviewVoteService service;

    @Autowired
    private ControllerHelper controllerHelper;

    @PostMapping("/vote_review/{id}/{type}")
    public VoteResult voteReview(@PathVariable(name = "id") Integer reviewId, @PathVariable(name = "type") String type, HttpServletRequest request){

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        if (customer == null){
            return VoteResult.fail("You must login to vote the review.");
        } else{
            VoteType voteType = VoteType.valueOf(type.toUpperCase());

            return service.doVote(reviewId, customer, voteType);
        }
    }
}
