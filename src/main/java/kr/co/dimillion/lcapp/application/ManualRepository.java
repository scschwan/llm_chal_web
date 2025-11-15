package kr.co.dimillion.lcapp.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManualRepository extends JpaRepository<Manual, Integer> {
    Optional<Manual> findByProductAndUsed(Product product, boolean used);

    Page<Manual> findByUsed(boolean used, Pageable pageable);
}
