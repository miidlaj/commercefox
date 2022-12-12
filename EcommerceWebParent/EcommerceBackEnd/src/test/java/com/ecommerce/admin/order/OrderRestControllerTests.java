package com.ecommerce.admin.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {

    @Autowired private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1", password = "pass1", authorities = {"Shipper"})
    public void testUpdateOrderStatus() throws Exception {
        Integer orderId = 14;
        String status = "SHIPPING";
        String requestUrl = "/orders_shipper/update/" + orderId + "/" + status;

        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
