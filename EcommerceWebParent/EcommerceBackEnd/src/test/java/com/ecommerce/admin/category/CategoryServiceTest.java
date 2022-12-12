package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCheckUniqueInNewModelReturnDuplicateName(){
        Integer id= null;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(id,name,alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);


        String result = categoryService.checkUnique(id,name,alias);

        Assertions.assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInNewModelReturnDuplicateAlias(){
        Integer id= null;
        String name = "unique";
        String alias = "digital-watch";

        Category category = new Category(id,name,alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);


        String result = categoryService.checkUnique(id,name,alias);

        Assertions.assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInNewModelReturnOk(){
        Integer id= null;
        String name = "unique";
        String alias = "unique-alias";

        Category category = new Category(id,name,alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);


        String result = categoryService.checkUnique(id,name,alias);

        Assertions.assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModelReturnDuplicateName(){
        Integer id= 1;
        String name = "Computers";
        String alias = "unique-alias";

        Category category = new Category(2,name,alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);


        String result = categoryService.checkUnique(id,name,alias);

        Assertions.assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInEditModelReturnDuplicateAlias(){
        Integer id= 1;
        String name = "unique";
        String alias = "digital-watch";

        Category category = new Category(2,name,alias);

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);


        String result = categoryService.checkUnique(id,name,alias);

        Assertions.assertThat(result).isEqualTo("DuplicateAlias");
    }
}
