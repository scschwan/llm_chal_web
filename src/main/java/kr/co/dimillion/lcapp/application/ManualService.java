package kr.co.dimillion.lcapp.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManualService {
    private final ProductRepository productRepository;
    private final ManualRepository manualRepository;

    @Transactional
    public Manual save(Integer productId, String filename, String filepath, long filesize) {
        Product product = productRepository.findById(productId).orElseThrow();
        Optional<Manual> maybeManual = manualRepository.findByProductAndUsed(product, true);
        maybeManual.ifPresent(Manual::delete);
        return manualRepository.save(new Manual(product, filename, filepath, filesize));
    }
}
