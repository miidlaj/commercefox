package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository repository;

    @Test
    public void testCreateBrand(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");

        acer.getCategories().add(laptops);

        Brand savedBrand = repository.save(acer);


        Assertions.assertThat(savedBrand).isNotNull();
        Assertions.assertThat(savedBrand.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateBrand2() {
        Category cellphones = new Category(4);
        Category tablets = new Category(7);

        Brand apple = new Brand("Apple");
        apple.getCategories().add(cellphones);
        apple.getCategories().add(tablets);

        Brand savedBrand = repository.save(apple);

        Assertions.assertThat(savedBrand).isNotNull();
        Assertions.assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand3() {
        Brand samsung = new Brand("Samsung");

        samsung.getCategories().add(new Category(8));	// category memory
        samsung.getCategories().add(new Category(7));	// category internal hard drive

        Brand savedBrand = repository.save(samsung);

        Assertions.assertThat(savedBrand).isNotNull();
        Assertions.assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll() {
        Iterable<Brand> brands = repository.findAll();
        brands.forEach(System.out::println);

        Assertions.assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById() {
        Brand brand = repository.findById(1).get();

        Assertions.assertThat(brand.getName()).isEqualTo("Acer");
    }

    @Test
    public void testUpdateName() {
        String newName = "Samsung Electronics";
        Brand samsung = repository.findById(1).get();
        samsung.setName(newName);

        Brand savedBrand = repository.save(samsung);
        Assertions.assertThat(savedBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete() {
        Integer id = 2;
        repository.deleteById(id);

        Optional<Brand> result = repository.findById(id);

        Assertions.assertThat(result.isEmpty());
    }
}
