package com.ecommerce.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("mumidlaj@gmail.com")
    public void testSendOrderReturnRequestFailed() throws Exception {
        Integer orderId = 111;
        OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, "", "");

        String requestUrl = "/orders/return";

        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(returnRequest))
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithUserDetails("commercefoxofficial@gmail.com")
    public void testSendOrderReturnRequestSuccess() throws Exception {
        Integer orderId = 15;
        String reason = "I bought the wrong items";
        String note = "Please return my money";
        OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, reason, note);

        String requestUrl = "/orders/return";

        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl).with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(returnRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
