package com.ecommerce.admin.banners;

import com.ecommerce.admin.AmazonS3Util;
import com.ecommerce.common.entity.Banner;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class BannerController {

    @Autowired
    private BannerService bannerService;


    @GetMapping("/banners")
    public String bannerPage(Model model){
        Banner banner1 = bannerService.findById(1);
        Banner banner2 = bannerService.findById(2);
        Banner banner3 = bannerService.findById(3);

        model.addAttribute("banner1",banner1);
        model.addAttribute("banner2",banner2);
        model.addAttribute("banner3",banner3);
        model.addAttribute("pageTitle","Banners");
        return "banner/banners";
    }

    @PostMapping("/banners/save_banner1")
    public String saveBanner1(Banner banner1, @RequestParam(name = "id_image1") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes) throws IOException {

        redirectAttributes.addFlashAttribute("message","The Banner 1 has been saved Successfully!");
        return checkMultipartAndSave(banner1, multipartFile, redirectAttributes);
    }

    @PostMapping("/banners/save_banner2")
    public String saveBanner2(Banner banner2, @RequestParam(name = "id_image2") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes) throws IOException {

        redirectAttributes.addFlashAttribute("message","The Banner 2 has been saved Successfully!");
        return checkMultipartAndSave(banner2, multipartFile, redirectAttributes);
    }

    @PostMapping("/banners/save_banner3")
    public String saveBanner3(Banner banner3, @RequestParam(name = "id_image3") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes) throws IOException {


        return checkMultipartAndSave(banner3, multipartFile, redirectAttributes);
    }

    @NotNull
    private String checkMultipartAndSave(Banner banner,MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
        Integer id = banner.getId();
        System.out.println(id);
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            banner.setBanner(fileName);
            Banner savedBanner = bannerService.save(banner);
            String uploadDir = "banners/"+savedBanner.getId();

            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }else{
            bannerService.save(banner);
        }

        redirectAttributes.addFlashAttribute("message","The Banner " + id + " has been saved Successfully!");
        return "redirect:/banners";
    }
}
