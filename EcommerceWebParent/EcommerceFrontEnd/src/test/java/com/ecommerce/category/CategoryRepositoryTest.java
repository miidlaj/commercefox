package com.ecommerce.category;

import com.ecommerce.category.CategoryRepository;
import com.ecommerce.common.entity.Category;
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
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testListEnabledCategoriesTest(){
        List<Category> categories = categoryRepository.findAllEnabled();

        categories.forEach(category -> {
            System.out.println(category.getName() + " " + category.isEnabled());
        });
    }

    @Test
    public void testFindCategoryByAlias(){
        String alias = "electronics";
        Category category = categoryRepository.findByAliasEnabled(alias);
        Assertions.assertThat(category).isNotNull();
    }
}
