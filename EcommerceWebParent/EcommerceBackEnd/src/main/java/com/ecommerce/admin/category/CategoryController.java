package com.ecommerce.admin.category;

import com.ecommerce.admin.AmazonS3Util;
import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@Transactional
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(Model model, String sortDir){

        return listByPage(1,model,null,sortDir);
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum")int pageNum,
                             Model model,
                             String keyword,
                             String sortDir){

        if (sortDir == null || sortDir.isEmpty()){
            sortDir = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listCategories = categoryService.listByPage(pageInfo,pageNum,sortDir,keyword);



        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        long startCount = (pageNum -1 ) * categoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + categoryService.ROOT_CATEGORIES_PER_PAGE -1;
        if (endCount > pageInfo.getTotalElements()){
            endCount = pageInfo.getTotalElements();
        }

        model.addAttribute("sortField","name");
        model.addAttribute("keyword",keyword);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("totalPages",pageInfo.getTotalPages());
        model.addAttribute("totalItems",pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("moduleURL", "/categories");

        return "category/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Category> listCategories =  categoryService.listCategoriesUsedInForm();
        model.addAttribute("category",new Category());
        model.addAttribute("pageTitle", "Create Category");
        model.addAttribute("listCategories", listCategories);

        return "category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage")MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes
    ) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory = categoryService.save(category);
            String uploadDir = "category-images/"+savedCategory.getId();

            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }else{
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message","The category has been added Successfully!");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category",category);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("pageTitle","Edit Category (ID: "+id + ")");

            return "category/category_form";

        }catch (CategoryNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes
    ){
        categoryService.updateCategoryEnabledStatus(id,enabled);
        String status = enabled ? "Enabled" : "Disabled";
        String message = "The user Id " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes,
                                 Model model
    ) throws CategoryNotFoundException {
        try {
            categoryService.delete(id);
            String categoryDir = "category-images/"+id;

            AmazonS3Util.removeFolder(categoryDir);
            redirectAttributes.addFlashAttribute("message", "The category with ID " + id + " deleted Successfully.");
            return "redirect:/categories";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }

    }

}
