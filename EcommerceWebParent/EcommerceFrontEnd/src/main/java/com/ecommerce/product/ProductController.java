package com.ecommerce.product;

import com.ecommerce.ControllerHelper;
import com.ecommerce.category.CategoryNotFoundException;
import com.ecommerce.category.CategoryService;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.entity.product.Product;
import com.ecommerce.common.entity.review.Review;
import com.ecommerce.review.ReviewService;
import com.ecommerce.review.vote.ReviewVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ControllerHelper controllerHelper;

    @Autowired
    private ReviewVoteService reviewVoteService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                     Model model) throws CategoryNotFoundException {

        return viewCategoryByPage(alias,1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                               @PathVariable("pageNum") int pageNum,
                               Model model) throws CategoryNotFoundException {

        try {
            Category category = categoryService.getCategory(alias);

            List<Category> listCategoryParents = categoryService.getCategoryParents(category);
            Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
            List<Product> listProducts = pageProducts.getContent();

            long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
            long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

            if (endCount > pageProducts.getTotalElements()) {
                endCount = pageProducts.getTotalElements();
            }

            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("category", category);


            return "products/products_by_category";

        }catch (CategoryNotFoundException e){
            return "error/404";

        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model, HttpServletRequest request){
        try {
            Product product = productService.getProduct(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
            Page<Review> listReviews = reviewService.list3MostVotedReviewsByProduct(product);

            Customer customer = controllerHelper.getAuthenticatedCustomer(request);

            if (customer != null){
                boolean  customerReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());
                reviewVoteService.markReviewVotedForProductByCustomer(listReviews.getContent(),product.getId(), customer.getId());

                if (customerReviewed){
                    model.addAttribute("customerReviewed", customerReviewed);
                } else{
                    boolean customerCanReview =  reviewService.canCustomerReviewProduct(customer, product.getId());
                    model.addAttribute("customerCanReview", customerCanReview);
                }
            }

            for (Review review : listReviews){
                System.out.println(review.isUpvotedByCurrentCustomer());
            }


            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product",product);
            model.addAttribute("listReviews", listReviews);

            return "products/product_details";
        }catch (ProductNotFoundException e){
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(String keyword, Model model){

        return searchByPage(keyword, 1, model);
    }

    @GetMapping("/search/{pageNum}")
    public String searchByPage(String keyword,
                         @PathVariable("pageNum") int pageNum,
                         Model model){
        Page<Product> pageProducts = productService.search(keyword, pageNum);
        List<Product> listResults = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.SEARCH_RESULT_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULT_PER_PAGE - 1;

        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("listProducts", listResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle","Search Result");

        return "products/search_result";
    }

}
