package com.ecommerce.admin.banners;

import com.ecommerce.common.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Integer> {

}
