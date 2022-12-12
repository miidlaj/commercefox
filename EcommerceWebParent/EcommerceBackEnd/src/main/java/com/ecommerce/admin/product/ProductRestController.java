package com.ecommerce.admin.product;

import com.ecommerce.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products/check_unique")
    public String checkUnique(Integer id, String name){
        return productService.checkUnique(id,name);
    }

    @GetMapping("/products/get/{id}")
    public ProductDTO getProductInfo(@PathVariable("id") Integer id)
            throws ProductNotFoundException {
        Product product = productService.get(id);
        return new ProductDTO(product.getName(), product.getMainImagePath(),
                product.getDiscountPrice(), product.getCost());
    }

    @GetMapping("/products/inventory/{productId}/{stock}")
    public String addStockIn(@PathVariable Integer stock, @PathVariable Integer productId){
        try {
            return productService.updateStockCount(productId,stock);
        } catch (ProductNotFoundException e) {
            return e.getMessage();
        }
    }


}
