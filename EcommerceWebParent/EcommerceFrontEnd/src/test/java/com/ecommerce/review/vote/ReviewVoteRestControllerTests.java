package com.ecommerce.review.vote;

import com.ecommerce.common.entity.review.Review;
import com.ecommerce.review.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewVoteRestControllerTests {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testVoteNotLogin() throws Exception {
        String requestURl = "/vote_review/1/up";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);

        Assertions.assertThat(voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage().contains("You must login"));
    }

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com", password = "midlaj123")
    public void testVoteNonExistReview() throws Exception {
        String requestURl = "/vote_review/123/up";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);

        Assertions.assertThat(voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage().contains("no longer exists"));
    }

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com", password = "midlaj123")
    public void testVoteUp() throws Exception {
        Integer reviewId = 10;
        String requestURl = "/vote_review/" + reviewId +"/up";

        Review review = reviewRepository.findById(reviewId).get();
        int voteCountBefore = review.getVotes();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);

        Assertions.assertThat(voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage().contains("nothing"));

        int voteCountAfter = voteResult.getVoteCount();

        org.junit.jupiter.api.Assertions.assertEquals(voteCountBefore + 1, voteCountAfter);
    }

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com", password = "midlaj123")
    public void testUndoVoteUp() throws Exception {
        Integer reviewId = 10;
        String requestURl = "/vote_review/" + reviewId +"/up";

        Review review = reviewRepository.findById(reviewId).get();
        int voteCountBefore = review.getVotes();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);

        Assertions.assertThat(voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage().contains("unvoted Up"));

        int voteCountAfter = voteResult.getVoteCount();

        org.junit.jupiter.api.Assertions.assertEquals(voteCountBefore - 1, voteCountAfter);
    }
}
