package kr.co.dimillion.lcapp.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
    long countBySearchedAtGreaterThanEqual(LocalDateTime searchedAt);
    long countByProductCode(String productCode);

    Page<SearchHistory> findByOrderByIdDesc(Pageable pageable);
}
