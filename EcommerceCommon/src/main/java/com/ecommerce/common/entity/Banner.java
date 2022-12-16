package com.ecommerce.common.entity;

import com.ecommerce.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "banners")
public class Banner extends IdBasedEntity{

    @Column(nullable = false, length = 256)
    String banner;

    @Column(nullable = false, length = 512)
    String title;

    @Column(length = 512, nullable = false)
    String description;

    @Column(nullable = false, length = 8)
    String price;

    @Column(length = 512, nullable = false)
    String link;

    public Banner() {
    }

    public Banner(Integer id) {
        this.id = id;
    }

    public Banner(Integer id, String banner, String title, String description, String price, String link) {
        this.id = id;
        this.banner = banner;
        this.title = title;
        this.description = description;
        this.price = price;
        this.link = link;
    }



    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Transient
    public String getBannerPath(){
        if (id == null || banner == null) return "/images/default_banner.png";
        return Constants.S3_BASE_URI + "/banners/" + this.id + "/" + this.banner;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "banner='" + banner + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", link='" + link + '\'' +
                ", id=" + id +
                '}';
    }
}
