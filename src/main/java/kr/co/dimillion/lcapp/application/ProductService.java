package kr.co.dimillion.lcapp.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(String code, String name, String description) {
        return productRepository.save(new Product(code, name, description));
    }
}
