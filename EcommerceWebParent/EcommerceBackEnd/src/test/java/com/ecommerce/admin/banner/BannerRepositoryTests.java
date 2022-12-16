package com.ecommerce.admin.banner;

import com.ecommerce.admin.banners.BannerRepository;
import com.ecommerce.common.entity.Banner;
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
public class BannerRepositoryTests {

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    public void testCreateBanner(){
        Banner banner = new Banner();

        banner.setTitle("HOT DEAL");
        banner.setDescription("NEW Mobile");
        banner.setLink("shop.commercefox.ml/c/laptop_computers");
        banner.setBanner("shop.commercefox.ml/assets/img/416X420/img1.png");
        banner.setPrice("10999");

        Banner savedBanner = bannerRepository.save(banner);

        System.out.println(savedBanner);
        Assertions.assertThat(savedBanner).isNotNull();
        Assertions.assertThat(savedBanner.getId()).isEqualTo(2);

    }

    @Test
    public void FindAllBanner(){

        List<Banner> bannerList = bannerRepository.findAll();

        bannerList.forEach(System.out::println);

    }

    @Test
    public void findById(){

        Integer id = 1;

        Banner banner = bannerRepository.findById(id).get();

        System.out.println(banner);

    }

    @Test
    public void updateBanner(){
        Integer id = 1;
        Banner banner = bannerRepository.findById(id).get();
        banner.setPrice("109");
        bannerRepository.save(banner);
        System.out.println(banner);

    }
}
