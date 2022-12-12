package com.ecommerce.admin.settings.country;

import com.ecommerce.common.entity.Country;
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

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepository;

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com" , password = "midlaj123" ,roles = "Admin")
    public void testListCountries() throws Exception {
        String url = "/countries/list";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        Assertions.assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com" , password = "midlaj123" ,roles = "Admin")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "United Arab Emirates";
        String countryCode = "UAE";

        Country country = new Country(countryName,countryCode);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(country)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(response);

        Optional<Country> findById = countryRepository.findById(countryId);
        Assertions.assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        Assertions.assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com" , password = "midlaj123" ,roles = "Admin")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";
        Integer countryId = 7;
        String countryName = "China";
        String countryCode = "CN";

        Country country = new Country(countryId,countryName,countryCode);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(country)).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(countryId)));

        Optional<Country> findById = countryRepository.findById(countryId);
        Assertions.assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        Assertions.assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com" , password = "midlaj123" ,roles = "Admin")
    public void testDeleteCountry() throws Exception {
        Integer countryId = 7;
        String url = "/countries/delete/" + countryId;
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Optional<Country> findById = countryRepository.findById(countryId);
        Assertions.assertThat(findById).isNotPresent();

    }

}
