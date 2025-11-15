package kr.co.dimillion.lcapp.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualRepository extends JpaRepository<Manual, Integer> {
    void deleteByProduct(Product productId);
}
