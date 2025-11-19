package kr.co.dimillion.lcapp.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    long countByCreatedAtGreaterThanEqual(LocalDateTime createdAt);
    Optional<Product> findTop1ByCode(String code);
}
