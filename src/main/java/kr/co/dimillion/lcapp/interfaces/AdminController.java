package kr.co.dimillion.lcapp.interfaces;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String admin() {
        return "admin";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/product-management")
    public String productManagement(@ModelAttribute ProductCreateForm productCreateForm) {
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
}
