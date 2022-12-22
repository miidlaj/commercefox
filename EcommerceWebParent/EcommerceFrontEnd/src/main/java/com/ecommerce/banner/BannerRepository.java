package com.ecommerce.banner;

import com.ecommerce.common.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {

    @Query("SELECT b FROM Banner b WHERE b.id <= 3")
    public List<Banner> findFirst3Banners();
}
