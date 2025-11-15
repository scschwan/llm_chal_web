package kr.co.dimillion.lcapp.interfaces;

import kr.co.dimillion.lcapp.application.Product;
import kr.co.dimillion.lcapp.application.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String admin() {
        return "admin";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/product-management")
    public String productManagement(@ModelAttribute ProductCreateForm productCreateForm, Model model) {
        List<ProductDto> productDtoList = productRepository.findAll()
                .stream()
                .map(ProductDto::from)
                .toList();
        model.addAttribute("products", productDtoList);
        return "product-management";
    }

    @PostMapping("/product-management/product")
    public String product(@ModelAttribute ProductCreateForm productCreateForm) {
        System.out.println("productCreateForm = " + productCreateForm);
        return "redirect:/admin/product-management";
    }

    @Data
    public static class ProductCreateForm {
        private String code;
        private String name;
        private String description;
    }

    public record ProductDto(Integer id, String code, String name, LocalDateTime createdAt, boolean active) {
        public static ProductDto from(Product product) {
            return new ProductDto(product.getId(), product.getCode(), product.getName(), product.getCreatedAt(), product.isActive());
        }
    }
}
