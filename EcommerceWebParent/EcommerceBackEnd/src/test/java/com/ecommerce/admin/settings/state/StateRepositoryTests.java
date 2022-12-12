package com.ecommerce.admin.settings.state;

import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {

    @Autowired
    private StateRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateStatesInIndia() {
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);

		State state1 = repo.save(new State("Karnataka", country));
		State state2 = repo.save(new State("Punjab", country));
		State state3 = repo.save(new State("Uttar Pradesh", country));
        State state4 = repo.save(new State("West Bengal", country));

//        Assertions.assertThat(state).isNotNull();
//        Assertions.assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateStatesInUS() {
        Integer countryId = 3;
        Country country = entityManager.find(Country.class, countryId);

		State state1 = repo.save(new State("California", country));
		State state2 = repo.save(new State("Texas", country));
		State state3 = repo.save(new State("New York", country));
        State state4 = repo.save(new State("Washington", country));

//        Assertions.assertThat(state).isNotNull();
//        Assertions.assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testListStatesByCountry() {
        Integer countryId = 2;
        Country country = entityManager.find(Country.class, countryId);
        List<State> listStates = repo.findByCountryOrderByNameAsc(country);

        listStates.forEach(System.out::println);

        Assertions.assertThat(listStates.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState() {
        Integer stateId = 3;
        String stateName = "Tamil Nadu";
        State state = repo.findById(stateId).get();

        state.setName(stateName);
        State updatedState = repo.save(state);

        Assertions.assertThat(updatedState.getName()).isEqualTo(stateName);
    }

    @Test
    public void testGetState() {
        Integer stateId = 1;
        Optional<State> findById = repo.findById(stateId);
        Assertions.assertThat(findById.isPresent());
    }

    @Test
    public void testDeleteState() {
        Integer stateId = 8;
        repo.deleteById(stateId);

        Optional<State> findById = repo.findById(stateId);
        Assertions.assertThat(findById.isEmpty());
    }
}
