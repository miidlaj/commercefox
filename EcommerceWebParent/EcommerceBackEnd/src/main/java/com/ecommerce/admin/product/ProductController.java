package com.ecommerce.admin.product;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.brand.BrandService;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.admin.paging.PagingAndSortingHelper;
import com.ecommerce.admin.paging.PagingAndSortingParam;
import com.ecommerce.admin.security.EcommerceUserDetails;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(){

       return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listProducts", moduleURL = "/products")PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum, Model model,
            Integer categoryId
    ){

         productService.listByPage(pageNum,helper, categoryId);

         List<Category> listCategories = categoryService.listCategoriesUsedInForm();

         if (categoryId != null) model.addAttribute("categoryId",categoryId);
         model.addAttribute("listCategories",listCategories);

         return "product/products";

    }

    @GetMapping("/products/inventory")
    public String inventoryManagement(){

        return "redirect:/products/inventory/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/inventory/page/{pageNum}")
    public String inventoryManagement(
            @PagingAndSortingParam(listName = "listProducts", moduleURL = "/products/inventory")PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum, Model model,
            Integer categoryId
    ){

        productService.listByPage(pageNum,helper, categoryId);

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        if (categoryId != null) model.addAttribute("categoryId",categoryId);
        model.addAttribute("listCategories",listCategories);

        return "product/product_inventory";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){
        List<Brand> listBrand = brandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("listBrand",listBrand);
        model.addAttribute("product",product);
        model.addAttribute("pageTitle","New Product");
        model.addAttribute("numberOfExistingExtraImages",0);


        return "product/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal EcommerceUserDetails loggedUser
                              ) throws IOException {

        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")){
                productService.saveProductPrice(product);
                redirectAttributes.addFlashAttribute("message","The product has been saved successfully");
                return "redirect:/products";
            }
        }

        ProductSaveHelper.setMainImageName(mainImageMultipart,product);
        ProductSaveHelper.setExistingExtraImageNames(imageIDs,imageNames,product);
        ProductSaveHelper.setNewExtraImageName(extraImageMultiparts, product);
        ProductSaveHelper.setProductDetails(detailIDs,detailNames,detailValues,product);

        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(mainImageMultipart,extraImageMultiparts, savedProduct);

        ProductSaveHelper.deleteExtraImagesThatRemovedFromForm(product);

        redirectAttributes.addFlashAttribute("message","The product has been saved successfully");

        return "redirect:/products";
    }


    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled,
                                             RedirectAttributes redirectAttributes){

        productService.updateCategoryEnabledStatus(id,enabled);

        String status = enabled ? "Enabled" : "Disabled";
        String message = "The Product " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id")Integer id,
                                RedirectAttributes redirectAttributes,
                                Model model){
        try {
            productService.delete(id);
            String productExtraImagesDir = "product-images/"+ id +"/extras";
            String productImagesDir = "product-images/"+ id;
            FileUploadUtil.cleanDir(productExtraImagesDir);
            FileUploadUtil.cleanDir(productImagesDir);

            redirectAttributes.addFlashAttribute("message","The product with ID "+id+ " has been deleted Successfully.");
        }catch (ProductNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id")Integer id,
                              RedirectAttributes redirectAttributes,
                              Model model){
        try {
            List<Brand> listBrand = brandService.listAll();
            Product product = productService.get(id);
            Integer numberOfExistingExtraImages = product.getImages().size();


            model.addAttribute("product",product);
            model.addAttribute("listBrand",listBrand);
            model.addAttribute("pageTitle","Edit Product( ID: "+id+" )");
            model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);

            return "product/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());

            return "redirect:/products";
        }

    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id")Integer id,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        try {
            Product product = productService.get(id);

            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product( ID: " + id + " )");

            return "product/product_detail_modal";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/products";
        }

    }
}
