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
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final FileSystem fileSystem;
    private final ManualService manualService;
    private final ManualRepository manualRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final DefectTypeService defectTypeService;

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
    public String manualManagement(@ModelAttribute ManualUploadForm manualUploadForm, @PageableDefault Pageable pageable, Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        Page<Manual> manualPage = manualRepository.findByUsed(true, pageable);
        model.addAttribute("manualPage", manualPage);

        List<ManualDto> manuals = manualPage.getContent().stream().map(ManualDto::new)
                .toList();
        model.addAttribute("manuals", manuals);

        return "manual-management";
    }

    @Data
    public static class ManualDto {
        private Integer id;
        private String productName;
        private String originalName;
        private String name;
        private String path;
        private String size;
        private boolean indexed;
        private LocalDateTime createdAt;

        public ManualDto(Manual manual) {
            this.id = manual.getId();
            this.productName = manual.getProduct().getName();
            this.originalName = manual.getName();
            String[] split = manual.getPath().split("/");
            this.name = split[split.length - 1];
            this.size = getSizeMB(manual.getSize());
            this.indexed = manual.isIndexed();
            this.createdAt = manual.getCreatedAt();
        }

        public String getSizeMB(Long size) {
            if (size == null) return "0";
            double mb = size / 1024.0 / 1024.0;
            return String.format("%.2f", mb);
        }
    }

    @PostMapping("/manual-management/manual")
    public String manual(@ModelAttribute ManualUploadForm manualUploadForm,
                         @RequestParam("file") MultipartFile file) {
        String filePath = fileSystem.uploadFile(file, "menual_store");
        manualService.save(manualUploadForm.getProductId(), file.getOriginalFilename(), filePath, file.getSize());
        return "redirect:/admin/manual-management";
    }

    @Data
    public static class ManualUploadForm {
        private Integer productId;
    }

    @DeleteMapping("/manual-management/manual")
    public String manual(@RequestParam Integer id) {
        manualService.deactivate(id);
        return "redirect:/admin/manual-management";
    }

    @GetMapping("/defect-type-management")
    public String defectManagement(Model model,
                                   @ModelAttribute DefectTypeCreateForm defectTypeCreateForm,
                                   @PageableDefault Pageable pageable,
                                   @RequestParam(required = false) Integer productId) {
        List<Product> products = productRepository.findAll();
        defectTypeCreateForm.setProducts(products);
        model.addAttribute("defectTypeInquiryForm", new DefectTypeInquiryForm(products, productId));

        Page<DefectType> defectTypePage;
        if (productId == null) {
            defectTypePage = defectTypeRepository.findByUsed(true, pageable);
        } else {
            Product product = productRepository.findById(productId).orElseThrow();
            defectTypePage = defectTypeRepository.findByProductAndUsed(product, true, pageable);
        }
        model.addAttribute("defectTypes", defectTypePage.getContent());
        model.addAttribute("page", defectTypePage);

        return "defect-type-management";
    }

    @Data
    public static class DefectTypeInquiryForm {
        private List<Product> products;
        private Integer productId;

        public DefectTypeInquiryForm(List<Product> products, Integer productId) {
            this.products = products;
            this.productId = productId;
        }
    }

    @PostMapping("/defect-type-management/defect-type")
    public String createDefectType(@ModelAttribute DefectTypeCreateForm defectTypeCreateForm) {
        Product product = productRepository.findById(defectTypeCreateForm.getProductId()).orElseThrow();
        defectTypeRepository.save(
                new DefectType(product, defectTypeCreateForm.getCode(), defectTypeCreateForm.getNameKo(),
                        defectTypeCreateForm.getNameEn(), defectTypeCreateForm.nameKo, defectTypeCreateForm.getDescription()));
        defectTypeCreateForm.setProductId(null);
        return "redirect:/admin/defect-type-management";
    }

    @PutMapping("/defect-type-management/defect-type")
    public String createDefectType(@RequestParam Integer id, @RequestParam String newNameKo) {
        defectTypeService.update(id, newNameKo);
        return "redirect:/admin/defect-type-management";
    }

    @DeleteMapping("/defect-type-management/defect-type")
    public String createDefectType(@RequestParam Integer id) {
        defectTypeService.delete(id);
        return "redirect:/admin/defect-type-management";
    }

    @Data
    public static class DefectTypeCreateForm {
        List<Product> products;
        private Integer productId;
        private String nameKo;
        private String code;
        private String nameEn;
        private String description;

    }
}
