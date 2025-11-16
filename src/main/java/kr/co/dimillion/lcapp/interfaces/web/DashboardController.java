package kr.co.dimillion.lcapp.interfaces.web;

import kr.co.dimillion.lcapp.application.*;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final ResponseHistoryRepository responseHistoryRepository;

    public DashboardController(ProductRepository productRepository, ImageRepository imageRepository, SearchHistoryRepository searchHistoryRepository, ResponseHistoryRepository responseHistoryRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.responseHistoryRepository = responseHistoryRepository;
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

        List<SearchHistory> searches = searchHistoryRepository.findTop20ByOrderByIdDesc();
        List<SearchDto> searchDtoList = searches.stream()
                .map(s -> {
                    ResponseHistory r = responseHistoryRepository.findBySearchHistory(s)
                            .orElse(null);
                    return new SearchDto(s, r);
                })
                .toList();
        model.addAttribute("searches", searchDtoList);

        Page<Product> productPage = productRepository.findAll(pageable);
        model.addAttribute("productPage", productPage);
        List<ProductDto> productDtoList = productPage.getContent()
                .stream()
                .map(p -> {
                    long nc = imageRepository.countByProductAndTypeAndUsed(p, "normal", true);
                    long dc = imageRepository.countByProductAndTypeAndUsed(p, "defect", true);
                    long sc = searchHistoryRepository.countByProductCode(p.getName());
                    return new ProductDto(p.getId(), p.getName(), nc, dc, sc);
                }).toList();
        model.addAttribute("products", productDtoList);

        return "dashboard";
    }

    @Data
    public static class SearchDto {
        private Integer id;
        private LocalDateTime searchedAt;
        private String productCode;
        private String defectCode;
        private Double similarityScore;
        private Double anomalyScore;

        public SearchDto(SearchHistory search, ResponseHistory response) {
            this.id = search.getId();
            this.searchedAt = search.getSearchedAt();
            this.productCode = response != null ? response.getProductCode() : null;
            this.defectCode = response != null ? response.getDefectCode() : null;
            this.similarityScore = response != null ? response.getSimilarityScore() : null;
            this.anomalyScore = response != null ? response.getAnomalyScore() : null;
        }
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
        LocalDate lastSunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return lastSunday.atStartOfDay();
    }
}
