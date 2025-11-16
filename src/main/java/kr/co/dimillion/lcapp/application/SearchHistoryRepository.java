package kr.co.dimillion.lcapp.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
    long countBySearchedAtGreaterThanEqual(LocalDateTime searchedAt);
    long countByProductCode(String productCode);

    List<SearchHistory> findTop20ByOrderByIdDesc();
}
