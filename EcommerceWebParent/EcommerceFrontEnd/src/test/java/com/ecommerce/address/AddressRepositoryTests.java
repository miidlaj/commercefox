package com.ecommerce.address;

import com.ecommerce.addresses.AddressRepository;
import com.ecommerce.common.entity.Address;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTests {

    @Autowired
    private AddressRepository repo;

    @Test
    public void testAddNew() {
        Integer customerId = 1;
        Integer countryId = 106;//USA

        Address newAddress = new Address();
        newAddress.setCustomer(new Customer(customerId));
        newAddress.setCountry(new Country(countryId));
        newAddress.setFirstName("Joel");
        newAddress.setLastName("Abel");
        newAddress.setPhoneNumber("19094644165");
        newAddress.setAddressLine1("4213 Gordon Street");
        newAddress.setAddressLine2("Novak Building");
        newAddress.setCity("Chino");
        newAddress.setState("California");
        newAddress.setPostalCode("91710");

        Address savedAddress = repo.save(newAddress);

        Assertions.assertThat(savedAddress).isNotNull();
        Assertions.assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 1;
        List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
        Assertions.assertThat(listAddresses.size()).isGreaterThan(0);

        listAddresses.forEach(System.out::println);
    }

    @Test
    public void testFindByIdAndCustomer() {
        Integer addressId = 1;
        Integer customerId = 1;

        Address address = repo.findByIdAndCustomer(addressId, customerId);

        Assertions.assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testUpdate() {
        Integer addressId = 3;
        String phoneNumber = "646-232-3932";

        Address address = repo.findById(addressId).get();
        address.setDefaultForShipping(true);

        Address updatedAddress = repo.save(address);
//        Assertions.assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    public void testDeleteByIdAndCustomer() {
        Integer addressId = 1;
        Integer customerId = 1;

        repo.deleteByIdAndCustomer(addressId, customerId);

        Address address = repo.findByIdAndCustomer(addressId, customerId);
        Assertions.assertThat(address).isNull();
    }

    @Test
    public void testSetDefault() {
        Integer addressId = 15;
        repo.setDefaultAddress(addressId);

        Address address = repo.findById(addressId).get();
        Assertions.assertThat(address.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testSetNonDefaultAddresses() {
        Integer addressId = 14;
        Integer customerId = 1;
        repo.setNonDefaultForOthers(addressId, customerId);
    }

    @Test
    public void testGetDefault() {
        Integer customerId = 1;
        Address address = repo.findDefaultByCustomer(customerId);
        Assertions.assertThat(address).isNotNull();
        System.out.println(address);
    }
}
