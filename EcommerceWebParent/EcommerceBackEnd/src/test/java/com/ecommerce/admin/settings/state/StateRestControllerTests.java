package com.ecommerce.admin.settings.state;

import com.ecommerce.admin.settings.country.CountryRepository;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.State;
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
public class StateRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    StateRepository stateRepo;

    @Test
    @WithMockUser(username = "mumidlaj@gmail.com", password = "midlaj123", roles = "Admin")
    public void testListByCountries() throws Exception {
        Integer countryId = 1;
        String url = "/states/list_by_country/" + countryId;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        State[] states = objectMapper.readValue(jsonResponse, State[].class);

        Assertions.assertThat(states).hasSizeGreaterThan(1);
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testCreateState() throws Exception {
        String url = "/states/save";
        Integer countryId = 2;
        Country country = countryRepo.findById(countryId).get();
        State state = new State("Arizona", country);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        Optional<State> findById = stateRepo.findById(stateId);

        Assertions.assertThat(findById.isPresent());
    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testUpdateState() throws Exception {
        String url = "/states/save";
        Integer stateId = 9;
        String stateName = "Alaska";

        State state = stateRepo.findById(stateId).get();
        state.setName(stateName);

        mockMvc.perform(MockMvcRequestBuilders.post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(stateId)));

        Optional<State> findById = stateRepo.findById(stateId);
        Assertions.assertThat(findById.isPresent());

        State updatedState = findById.get();
        Assertions.assertThat(updatedState.getName()).isEqualTo(stateName);

    }

    @Test
    @WithMockUser(username = "nam", password = "something", roles = "Admin")
    public void testDeleteState() throws Exception {
        Integer stateId = 6;
        String uri = "/states/delete/" + stateId;

        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isOk());

        Optional<State> findById = stateRepo.findById(stateId);

        Assertions.assertThat(findById).isNotPresent();
    }
}