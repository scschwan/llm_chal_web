package kr.co.dimillion.lcapp.interfaces.api;

import kr.co.dimillion.lcapp.application.DefectType;
import kr.co.dimillion.lcapp.application.DefectTypeRepository;
import kr.co.dimillion.lcapp.application.Product;
import kr.co.dimillion.lcapp.application.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DefectTypeRestController {
    private final DefectTypeRepository defectTypeRepository;
    private final ProductRepository productRepository;

    public DefectTypeRestController(DefectTypeRepository defectTypeRepository, ProductRepository productRepository) {
        this.defectTypeRepository = defectTypeRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/v1/defect-types")
    public List<DefectType> getDefectTypes(@RequestParam Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow();
        return defectTypeRepository.findByProductAndUsed(product, true);
    }
}
