package kr.co.dimillion.lcapp.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Page<Image> findByTypeAndUsed(String type, boolean used, Pageable pageable);
    Page<Image> findByProductAndTypeAndUsed(Product product, String type, boolean used, Pageable pageable);
    Page<Image> findByProductAndDefectTypeAndTypeAndUsed(Product product, DefectType defectType, String type, boolean used, Pageable pageable);

    long countByTypeAndUsed(String type, boolean used);
    long countByTypeAndUsedAndUploadedAtGreaterThanEqual(String type, boolean used, LocalDateTime uploadedAt);
    long countByProductAndTypeAndUsed(Product product, String type, boolean used);
}
