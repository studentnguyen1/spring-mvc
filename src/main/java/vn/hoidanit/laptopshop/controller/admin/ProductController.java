package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/admin/product/{id}")

    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("id", id);
        model.addAttribute("product", product);
        return "admin/product/detail";

    }

    @RequestMapping("/admin/product/update/{id}")

    public String updateProduct(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.getProductById(id);
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";

    }

    @PostMapping("/admin/product/update")

    public String postUpdateProduct(Model model, @ModelAttribute("newProduct") Product currentPrd,
            BindingResult newProductbindingResult,
            @RequestParam("productFile") MultipartFile productFile) {

        if (newProductbindingResult.hasErrors()) {
            return "/admin/product/create";
        }
        Product currentProduct = this.productService.getProductById(currentPrd.getId());
        String productAvatar = this.uploadService.handleSaveProductFile(productFile, "product");
        if (currentProduct != null) {
            currentProduct.setName(currentPrd.getName());
            currentProduct.setPrice(currentPrd.getPrice());
            currentProduct.setDetailDesc(currentPrd.getDetailDesc());
            currentProduct.setShortDesc(currentPrd.getShortDesc());
            currentProduct.setQuantity(currentPrd.getQuantity());
            currentProduct.setFactory(currentPrd.getFactory());
            currentProduct.setTarget(currentPrd.getTarget());
            currentProduct.setImage(productAvatar);
            this.productService.handleSaveProduct(currentProduct);
        }
        return "redirect:/admin/product";

    }

    @RequestMapping("/admin/product/delete/{id}")

    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")

    public String postDeleteProduct(Model model, @ModelAttribute("newUser") Product currentPrd) {
        this.productService.deleteById((currentPrd.getId()));
        return "redirect:/admin/product";
    }

}
