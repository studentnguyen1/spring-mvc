package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product/create")

    public String getCreateProduct(Model model) {

        model.addAttribute("newProduct", new Product());
        return "admin/product/create";

    }

    @RequestMapping("/admin/product")

    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("products", products);

        return "admin/product/show";
    }

    @PostMapping(value = "/admin/product/create")

    public String createProduct(Model model, @ModelAttribute("newProduct") @Valid Product product1,
            BindingResult newProductbindingResult,
            @RequestParam("productFile") MultipartFile productFile) {

        List<FieldError> errors = newProductbindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }

        // Validate

        if (newProductbindingResult.hasErrors()) {
            return "/admin/product/create";
        }

        String productAvatar = this.uploadService.handleSaveProductFile(productFile, "product");
        product1.setImage(productAvatar);
        this.productService.handleSaveProduct(product1);

        return "redirect:/admin/product";

    }

}
