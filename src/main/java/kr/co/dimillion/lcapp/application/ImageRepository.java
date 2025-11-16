package kr.co.dimillion.lcapp.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Page<Image> findByTypeAndUsed(String type, boolean used, Pageable pageable);
    Page<Image> findByProductAndTypeAndUsed(Product product, String type, boolean used, Pageable pageable);
    Page<Image> findByProductAndDefectTypeAndTypeAndUsed(Product product, DefectType defectType, String type, boolean used, Pageable pageable);
}
