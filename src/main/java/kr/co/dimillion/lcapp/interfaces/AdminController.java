package kr.co.dimillion.lcapp.interfaces;

import kr.co.dimillion.lcapp.application.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final FileUploadService fileUploadService;
    private final ManualService manualService;

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

    @Data
    public static class ProductCreateForm {
        private String code;
        private String name;
        private String description;
    }

    @GetMapping("/manual-management")
    public String manualManagement(Model model, @ModelAttribute ManualUploadForm manualUploadForm) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "manual-management";
    }

    @PostMapping("/manual-management/manual")
    public String manual(@ModelAttribute ManualUploadForm manualUploadForm,
                         @RequestParam("file") MultipartFile file) {
        String filePath = fileUploadService.uploadFile(file, "menual_store");

        String[] split = filePath.split("/");
        String filename = split[split.length - 1];
        manualService.save(manualUploadForm.getProductId(), filename, filePath);

        return "redirect:/admin/manual-management";
    }

    @Data
    public static class ManualUploadForm {
        private Integer productId;
    }


}
