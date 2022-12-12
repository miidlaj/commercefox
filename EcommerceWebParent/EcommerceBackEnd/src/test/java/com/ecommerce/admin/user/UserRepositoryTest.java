package com.ecommerce.admin.user;

import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 1);
        User midlaj = new User("mumidlaj@gmail.com","123","Midlaj","Muhammed");
        midlaj.addRole(roleAdmin);

        User savedUser = repo.save(midlaj);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles(){
        User salim = new User("salim@gmail.com","000","Salim","Muhammed");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        salim.addRole(roleEditor);
        salim.addRole(roleAssistant);

        User savedUser = repo.save(salim);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUser(){
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById(){
        User user = repo.findById(1).get();
        System.out.println(user);
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User user  = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("midlaj@gmail.com");

        repo.save(user);
    }

    @Test
    public void testUpdateUserRoles(){
        User user = repo.findById(2).get();

        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(2);

        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesPerson);

        repo.save(user);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 8;
        repo.deleteById(userId);

    }

    @Test
    public void testGetUserByEmail(){
        String email = "mumidlaj@gmail.com";
        User user = repo.getUserByEmail(email);

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 1;
        Long countById = repo.countById(id);

        Assertions.assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 34;
        repo.updateEnabledStatus(id,false);


    }

    @Test
    public void testEnableUser(){
        Integer id = 34;
        repo.updateEnabledStatus(id,true);

    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 1;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = repo.findAll(pageable);
        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        Assertions.assertThat(listUsers.size()).isEqualTo(pageSize);

    }

    @Test
    public void testSearchUser(){
        String keyword = "bruce";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = repo.findAll(keyword,pageable);
        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        Assertions.assertThat(listUsers.size()).isGreaterThan(0);
    }
}
