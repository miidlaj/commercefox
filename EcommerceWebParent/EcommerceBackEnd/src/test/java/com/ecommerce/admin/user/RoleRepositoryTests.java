package com.ecommerce.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecommerce.admin.user.RoleRepository;
import com.ecommerce.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("ADMIN","Manage Everything");
        Role savedRole = repo.save(roleAdmin);

        System.out.println(savedRole);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role roleSalesPerson = new Role("SalesPerson", "Manage Product Price, Customers, Shipping Orders, Sale Report");
        Role roleEditor = new Role("EDITOR", "Manage Categories, Brands, Products,Articles and Menus");
        Role roleShipper = new Role("SHIPPER", "View Products,View Orders and Update Order Status");
        Role roleAssistant = new Role("ASSISTANT", "Manage Questions and Reviews of Products");

        repo.saveAll(List.of(roleSalesPerson,roleEditor,roleShipper,roleAssistant));
    }
}
