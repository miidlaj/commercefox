package com.ecommerce.customer;

import com.ecommerce.common.entity.AuthenticationType;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        Integer countryId = 234; // USA
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Muhammed");
        customer.setLastName("Midlaj");
        customer.setPassword("midlaj123");
        customer.setEmail("mumidlaj@gmail.com");
        customer.setPhoneNumber("312-462-7518");
        customer.setAddressLine1("1927  West Drive");
        customer.setCity("Sacramento");
        customer.setState("California");
        customer.setPostalCode("95867");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2() {
        Integer countryId = 106; // India
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Salim");
        customer.setLastName("Muhammed");
        customer.setPassword("midlaj123");
        customer.setEmail("salim@gmail.com");
        customer.setPhoneNumber("02224928052");
        customer.setAddressLine1("173 , A-, Shah & Nahar Indl.estate, Sunmill Road");
        customer.setAddressLine2("Dhanraj Mill Compound, Lower Parel (west)");
        customer.setCity("Mumbai");
        customer.setState("Maharashtra");
        customer.setPostalCode("400013");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers() {
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(System.out::println);

        Assertions.assertThat(customers).hasSizeGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer() {
        Integer customerId = 1;
        String firstname = "King";

        Customer customer = repo.findById(customerId).get();
        customer.setFirstName(firstname);
        customer.setEnabled(true);

        Customer updatedCustomer = repo.save(customer);
        Assertions.assertThat(updatedCustomer.getFirstName()).isEqualTo(firstname);
    }

    @Test
    public void testGetCustomer() {
        Integer customerId = 2;
        Optional<Customer> findById = repo.findById(customerId);

        Assertions.assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Integer customerId = 2;
        repo.deleteById(customerId);

        Optional<Customer> findById = repo.findById(customerId);
        Assertions.assertThat(findById).isNotPresent();
    }

    @Test
    public void testFindByEmail() {
        String email = "mumidlaj@gmail.com";
        Customer customer = repo.findByEmail(email);

        Assertions.assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode() {
        String code = "code_123";
        Customer customer = repo.findByVerificationCode(code);

        Assertions.assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer() {
        Integer customerId = 1;
        repo.enable(customerId);

        Customer customer = repo.findById(customerId).get();
        Assertions.assertThat(customer.isEnabled()).isTrue();
    }

    @Test
    public void testUpdateAuthenticationType() {
        Integer id = 1;

        repo.updateAuthenticationType(id, AuthenticationType.DATABASE);

        Customer customer = repo.findById(id).get();

        Assertions.assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
    }
}
