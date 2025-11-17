package kr.co.dimillion.lcapp.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponseHistoryRepository extends JpaRepository<ResponseHistory, Integer> {
    Optional<ResponseHistory> findTop1BySearchHistory(SearchHistory searchHistory);
}
