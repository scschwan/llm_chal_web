package kr.co.dimillion.lcapp.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManualService {
    private final ProductRepository productRepository;
    private final ManualRepository manualRepository;

    @Transactional
    public Manual save(Integer productId, String filename, String filepath) {
        Product product = productRepository.findById(productId).orElseThrow();
        manualRepository.deleteByProduct(product);
        return manualRepository.save(new Manual(product, filename, filepath));
    }
}
