package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateCategory(){
        Category category = new Category("Electronics");
        Category savedCategory =  categoryRepository.save(category);

        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void createSubCategory(){
        Category parent = new Category(2);
        Category subCategory = new Category("A ndroid",parent);

        Category savedCat = categoryRepository.save(subCategory);

        Assertions.assertThat(savedCat.getId()).isGreaterThan(0);

        testPrintHierarchicalCategories();

    }

    @Test
    public void testGetCategory(){
        Category category = categoryRepository.findById(2).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory: children){
            System.out.println(subCategory.getName());
        }

        Assertions.assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category: categories){
            if (category.getParent() == null){
                System.out.println(category.getName());

                Set<Category> children  = category.getChildren();

                for (Category subCategory: children){
                    System.out.println("--" + subCategory.getName());

                    printChildren(subCategory,1);

                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel+1;

        Set<Category> children = parent.getChildren();
        for (Category subCategory: children){
            for (int i = 0; i < newSubLevel; i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void listRootCategories(){
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(cat -> System.out.println(cat.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computers";
        Category category = categoryRepository.findByName(name);

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String alias = "bag";
        Category category = categoryRepository.findByAlias(alias);

        Assertions.assertThat(category).isNotNull();
        Assertions.assertThat(category.getAlias()).isEqualTo(alias);
    }
}
