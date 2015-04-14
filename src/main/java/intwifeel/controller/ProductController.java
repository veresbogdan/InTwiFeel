package intwifeel.controller;

import intwifeel.model.ProductEntity;
import intwifeel.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize(value = "isAuthenticated()")
    public @ResponseBody ProductEntity addProduct(@RequestBody ProductEntity productEntity) {
        return productService.addProduct(productEntity);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize(value = "isAuthenticated()")
    public @ResponseBody List<ProductEntity> listProducts() {
        return productService.listProductsForUser();
    }

    @RequestMapping(value = "/remove/{name}", method = RequestMethod.DELETE)
    @PreAuthorize(value = "isAuthenticated()")
    public @ResponseBody void removeProduct(@PathVariable String name) {
        productService.removeProduct(name);
    }
}
