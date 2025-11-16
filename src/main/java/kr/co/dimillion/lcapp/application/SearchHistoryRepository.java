package kr.co.dimillion.lcapp.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
    long countBySearchedAtGreaterThanEqual(LocalDateTime searchedAt);
}
