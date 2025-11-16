package kr.co.dimillion.lcapp.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelSettingsRepository extends JpaRepository<ModelSettings, Integer> {
    Optional<ModelSettings> findFirstByOrderByIdDesc();
}
