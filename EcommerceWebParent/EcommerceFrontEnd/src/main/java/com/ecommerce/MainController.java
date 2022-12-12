package com.ecommerce;

import com.ecommerce.cart.ShoppingCartService;
import com.ecommerce.category.CategoryService;
import com.ecommerce.common.entity.CartItem;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class MainController {

	@Autowired private CategoryService categoryService;

	@Autowired private ShoppingCartService shoppingCartService;

	@Autowired private ControllerHelper controllerHelper;

	@GetMapping("")
	public String viewHomePage(Model model, HttpServletRequest request) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);

		List<Category> listCategories = categoryService.listNoChildrenCategories();

		List<Category> listNoParentCategories = categoryService.listNoParentCategories();

		List<CartItem> cartItems = shoppingCartService.getCart(customer);


		float estimatedPriceTotal = 0.0F;
		int numberofItem = 0;
		for (CartItem Item : cartItems){
			estimatedPriceTotal += Item.getPriceSubTotal();
			numberofItem++;
		}

		List<Category> bigDealCategory = new ArrayList<>();
		for (int i=0; i < 4; i++){
			int rnd = new Random().nextInt(listCategories.size());
			bigDealCategory.add(listCategories.get(rnd));
		}



		model.addAttribute("cartItems",cartItems);
		model.addAttribute("estimatedPriceTotal",estimatedPriceTotal);
		model.addAttribute("numberofItem",numberofItem);
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("bigDealCategory",bigDealCategory);
		model.addAttribute("listNoParentCategories",listNoParentCategories);

		return "index";
	}

	@GetMapping("/login")
	public String viewLoginPage(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication instanceof AnonymousAuthenticationToken){

			return "login";
		}

		return "redirect:/";
	}


}
