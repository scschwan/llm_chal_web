package kr.co.dimillion.lcapp.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;

    @Transactional
    public void delete(Integer id) {
        defectTypeRepository.findById(id)
                .ifPresent(defectTypeRepository::delete);
    }
}
