package kr.co.dimillion.lcapp.interfaces.web;

import kr.co.dimillion.lcapp.application.ImageRepository;
import kr.co.dimillion.lcapp.application.Product;
import kr.co.dimillion.lcapp.application.ProductRepository;
import kr.co.dimillion.lcapp.application.SearchHistoryRepository;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    public DashboardController(ProductRepository productRepository, ImageRepository imageRepository, SearchHistoryRepository searchHistoryRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @PageableDefault Pageable pageable) {
        long count = productRepository.count();
        model.addAttribute("productCount", count);

        LocalDateTime startOfWeek = getLocalDateTime();

        long thisWeekProductCount = productRepository.countByCreatedAtGreaterThanEqual(startOfWeek);
        model.addAttribute("thisWeekProductCount", thisWeekProductCount);

        long normalImageCount = imageRepository.countByTypeAndUsed("normal", true);
        model.addAttribute("normalImageCount", normalImageCount);

        long thisWeekNormalImageCount = imageRepository.countByTypeAndUsedAndUploadedAtGreaterThanEqual("normal", true, startOfWeek);
        model.addAttribute("thisWeekNormalImageCount", thisWeekNormalImageCount);

        long defectImageCount = imageRepository.countByTypeAndUsed("defect", true);
        model.addAttribute("defectImageCount", defectImageCount);

        long thisWeekDefectImageCount = imageRepository.countByTypeAndUsedAndUploadedAtGreaterThanEqual("defect", true, startOfWeek);
        model.addAttribute("thisWeekDefectImageCount", thisWeekDefectImageCount);

        long searchCount = searchHistoryRepository.count();
        model.addAttribute("searchCount", searchCount);

        long thisWeekSearchCount = searchHistoryRepository.countBySearchedAtGreaterThanEqual(startOfWeek);
        model.addAttribute("thisWeekSearchCount", thisWeekSearchCount);

        Page<Product> productPage = productRepository.findAll(pageable);
        model.addAttribute("productPage", productPage);
        List<ProductDto> productDtoList = productPage.getContent()
                .stream()
                .map(p -> {
                    long normalCount = imageRepository.countByProductAndTypeAndUsed(p, "normal", true);
                    long defectCount = imageRepository.countByProductAndTypeAndUsed(p, "defect", true);
                    return new ProductDto(p.getId(), p.getName(), normalCount, defectCount, 0);
                }).toList();
        model.addAttribute("products", productDtoList);

        return "dashboard";
    }

    @Data
    public static class ProductDto {
        private Integer id;
        private String name;
        private long normalImageCount;
        private long defectImageCount;
        private long searchCount;

        public ProductDto(Integer id, String name, long normalImageCount, long defectImageCount, long searchCount) {
            this.id = id;
            this.name = name;
            this.normalImageCount = normalImageCount;
            this.defectImageCount = defectImageCount;
            this.searchCount = searchCount;
        }
    }

    private static LocalDateTime getLocalDateTime() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.SUNDAY);
        LocalDateTime startOfWeek = monday.atStartOfDay();
        return startOfWeek;
    }
}
