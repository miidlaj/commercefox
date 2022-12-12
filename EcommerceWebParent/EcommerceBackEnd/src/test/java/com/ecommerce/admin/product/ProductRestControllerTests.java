package com.ecommerce.admin.product;

import com.ecommerce.common.entity.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductRestControllerTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser(username = "mumidlaj@gmail.com" , password = "midlaj123" ,authorities = "Admin")
    public void testAddStock() throws Exception {
        Integer productId = 7;
        Integer stock = 1;
        String url = "/products/inventory/" + productId + "/" + stock;
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
