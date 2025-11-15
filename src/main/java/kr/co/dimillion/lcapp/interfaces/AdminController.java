package kr.co.dimillion.lcapp.interfaces;

import kr.co.dimillion.lcapp.application.Product;
import kr.co.dimillion.lcapp.application.ProductRepository;
import kr.co.dimillion.lcapp.application.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping
    public String admin() {
        return "admin";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/product-management")
    public String productManagement(@ModelAttribute ProductCreateForm productCreateForm, @PageableDefault Pageable pageable, Model model) {
        Page<Product> productPage = productRepository.findAll(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        return "product-management";
    }

    @PostMapping("/product-management/product")
    public String product(@ModelAttribute ProductCreateForm productCreateForm) {
        productService.create(productCreateForm.getCode(), productCreateForm.getName(), productCreateForm.getDescription());
        return "redirect:/admin/product-management";
    }

    @GetMapping("/manual-management")
    public String manualManagement() {
        return "manual-management";
    }

    @Data
    public static class ProductCreateForm {
        private String code;
        private String name;
        private String description;
    }
}
