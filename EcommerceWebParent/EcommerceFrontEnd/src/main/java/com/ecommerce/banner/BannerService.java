package com.ecommerce.banner;

import com.ecommerce.common.entity.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;


    public List<Banner> getAllBanners(){
        List<Banner> banners = bannerRepository.findAll();

        return banners;
    }
}
