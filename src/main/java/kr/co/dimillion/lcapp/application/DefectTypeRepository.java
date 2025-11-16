package kr.co.dimillion.lcapp.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefectTypeRepository extends JpaRepository<DefectType, Integer> {
    Page<DefectType> findByUsed(boolean used, Pageable pageable);
    List<DefectType> findByProductAndUsed(Product product, boolean used);
    Page<DefectType> findByProductAndUsed(Product product, boolean used, Pageable pageable);
}
