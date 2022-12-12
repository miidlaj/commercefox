package com.ecommerce.admin.brand;

import com.ecommerce.common.entity.Brand;
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
public class BrandServiceTests {

    @MockBean private BrandRepository brandRepository;

    @InjectMocks private BrandService brandService;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate(){
        Integer id = null;
        String name = "Apple";

        Brand brand = new Brand(name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result  = brandService.checkUnique(id,name);
        Assertions.assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOk(){
        Integer id = null;
        String name = "Samsung";

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result  = brandService.checkUnique(id,name);
        Assertions.assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate(){
        Integer id = 1;
        String name = "AMD";

        Brand brand = new Brand(name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

        String result  = brandService.checkUnique(id,name);
        Assertions.assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK(){
        Integer id = null;
        String name = "Samsung";

        Brand brand = new Brand(id,name);

        Mockito.when(brandRepository.findByName(name)).thenReturn(null);

        String result  = brandService.checkUnique(id,name);
        Assertions.assertThat(result).isEqualTo("OK");
    }
}
