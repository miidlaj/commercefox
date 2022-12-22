package com.ecommerce.category;

import com.ecommerce.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/c/0")
    public String viewAllCategory(Model model){
        List<Category> listCategories = categoryService.listNoChildrenCategories();


        model.addAttribute("listCategories", listCategories);
        return "category/all_categories";
    }
}
