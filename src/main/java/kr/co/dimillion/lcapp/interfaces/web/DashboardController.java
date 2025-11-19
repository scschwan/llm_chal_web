package kr.co.dimillion.lcapp.interfaces.web;

import kr.co.dimillion.lcapp.application.*;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final ResponseHistoryRepository responseHistoryRepository;
    private final DefectTypeRepository defectTypeRepository;

    public DashboardController(ProductRepository productRepository, ImageRepository imageRepository, SearchHistoryRepository searchHistoryRepository, ResponseHistoryRepository responseHistoryRepository, DefectTypeRepository defectTypeRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.responseHistoryRepository = responseHistoryRepository;
        this.defectTypeRepository = defectTypeRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(defaultValue = "0") int productPageNumber,
                            @RequestParam(defaultValue = "10") int productPageSize,
                            @RequestParam(defaultValue = "0") int searchPageNumber,
                            @RequestParam(defaultValue = "10") int searchPageSize,
                            @RequestParam(defaultValue = "0") int responsePageNumber,
                            @RequestParam(defaultValue = "10") int responsePageSize) {
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

        Page<Product> productPage = productRepository.findAll(PageRequest.of(productPageNumber, productPageSize));
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

        Page<SearchHistory> searchPage = searchHistoryRepository.findByOrderByIdDesc(PageRequest.of(searchPageNumber, searchPageSize));
        model.addAttribute("searchPage", searchPage);
        model.addAttribute("searches", searchPage.getContent());

        Page<ResponseHistory> responsePage = responseHistoryRepository.findByOrderByIdDesc(PageRequest.of(responsePageNumber, responsePageSize));
        model.addAttribute("responsePage", responsePage);
        List<ResponseDto> responses = responsePage.stream()
                .map(r -> {
                    Product product = productRepository.findTop1ByCode(r.getProductCode())
                            .orElseThrow();
                    DefectType defectType = defectTypeRepository.findTop1ByCode(r.getDefectCode())
                            .orElseThrow();
                    return new ResponseDto(r, product, defectType);
                }).toList();
        model.addAttribute("responses", responses);


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
        LocalDate lastSunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        return lastSunday.atStartOfDay();
    }

    @Data
    public static class ResponseDto {
        private LocalDateTime executedAt;
        private String productName;
        private String defectName;
        private Double similarityScore;
        private Double anomalyScore;
        private String modelType;
        private String guideContent;
        private LocalDateTime guideGeneratedAt;
        private Double processingTime;
        private Integer feedbackRating;
        private String feedbackText;
        private String feedbackUser;
        private LocalDateTime feedbackAt;

        public ResponseDto(ResponseHistory response, Product product, DefectType defectType) {
            executedAt = response.getExecutedAt();
            productName = product.getName();
            defectName = defectType.getNameKo();
            similarityScore = response.getSimilarityScore();
            anomalyScore = response.getAnomalyScore();
            modelType = response.getModelType();
            guideContent = response.getGuideContent();
            guideGeneratedAt = response.getGuideGeneratedAt();
            processingTime = response.getProcessingTime();
            feedbackRating = response.getFeedbackRating();
            feedbackText = response.getFeedbackText();
            feedbackUser = response.getFeedbackUser();
            feedbackAt = response.getFeedbackAt();
        }
    }
}
